#include "event_handler.h"
#include "requests.h"
#include "json_parser.h"
#include "cert.h"
#include "private_key.h"
#include "sntp.h"
#include "ssl_conf.h"
#include "I2C.h"
#include "main.h"

bool ready_to_send = true;
bool is_connected = false;
bool wakeupFlag = false;

char server_ip_str[16];

esp_tcp *tcp_info;
struct ip_info ipconfig;

os_timer_t timer;
os_timer_t timeout_timer;

void setWakeupFlag(int flag)
{
    if (flag == 1)
    {
        wakeupFlag = true;
    }
}

void connection_success_handler(void *connection)
{
    os_timer_disarm(&timer);
    ready_to_send = true;
    is_connected = true;
    os_printf("Successful connection to server\n");

    espconn_regist_recvcb(connection, data_received_handler);
    espconn_regist_sentcb(connection, sent_success_handler);

    if (wakeupFlag)
    {
        handle_buffer_overflow();
    }
    else
    {
	    system_deep_sleep_instant(DEEPSLEEP_TIME);
    }
}

void set_ready_send_flag()
{
    ready_to_send = true;
}

void send_request(void *arg)
{
//    float x_test[32] = {3.9731,-3.9755,-3.5362,4.4597,-4.4597,4.4458,-2.7486,4.7772,1.8858,1.065,-4.7346,-1.831,-1.0421,1.713,4.93,-2.1733,2.1132,2.73,-1.6737,4.3646,2.8848,3.1478,1.7745,-2.2449,2.4057,4.502,3.0818,4.0011,-0.6778,-1.0867,-4.4014,-1.4495};
//    float y_test[32] = {4.0363,2.2414,-3.9316,2.9145,-1.5591,-3.3495,0.5483,-0.3572,4.1518,0.3522,-3.6412,-3.0122,-0.9259,-0.2487,-1.515,0.6406,2.698,-2.4465,1.9404,1.405,-1.741,-3.4153,0.7505,-1.5231,-2.0296,3.9891,-3.0606,0.8568,-4.4574,-1.2602,-0.5885,3.014};
//    float z_test[32] = {-0.8043,1.5321,0.1643,2.732,4.3203,-1.5976,4.3279,-0.8092,4.9929,2.9848,3.4345,-2.2532,-1.0425,-4.2129,0.0992,4.5467,-0.8925,0.927,-3.078,2.103,4.4798,-3.8996,-4.9509,-3.3094,-2.0349,0.8482,-4.7471,1.0873,1.6234,-4.3452,-0.955,-2.7885};

    ready_to_send = false;

    os_timer_disarm(&timeout_timer);
    os_timer_setfn(&timeout_timer, set_ready_send_flag, NULL);
    os_timer_arm(&timeout_timer, 5000, 0);

    char *data_to_send;

    os_printf("Allocating data buffer\n");

    data_to_send = (char *) os_zalloc(SSL_PACKET_SIZE);

    os_printf("Data buffer allocated\n");

    os_delay_us(32000);
    os_delay_us(32000);
    os_delay_us(32000);
    char *body = parse_request_body(vcc_measure, x_buffer, y_buffer, z_buffer, 32);
//      char *body = parse_request_body(x_test, y_test, z_test,32);
    os_delay_us(32000);
    os_delay_us(32000);
    os_delay_us(32000);

    int content_length = os_strlen(body);

    os_sprintf(data_to_send, POST_JSON_FORM,
               SENSOR_URL, server_ip_str,
               BASIC_HASH,
               content_length,
               body);


//    os_printf("DATA TO SEND: %s\n", data_to_send);

    os_printf("Sending secure post request");
    espconn_secure_send(connection, data_to_send, os_strlen(data_to_send));
    os_free(body);
    os_free(data_to_send);

    os_printf("Request sent\n");
}

void sent_success_handler(void *connection)
{
    os_printf("Data has been successfully sent\n");
    ready_to_send = true;
}

void data_received_handler(void *conn_info, char *data, unsigned short len)
{
//    os_printf("SERVER RESPONSE:\n %s", data);
    system_deep_sleep_instant(DEEPSLEEP_TIME);
//    espconn_secure_disconnect(connection);
}

void wifi_event_handler(System_Event_t *e)
{
    switch (e->event)
    {
        case EVENT_STAMODE_CONNECTED:
            os_printf("Connected to SSID: %s \n", e->event_info.connected.ssid);
            if (wifi_get_macaddr(0x00, mac))
            {
                os_printf("\ngot MAC\n");
                parseMAC();
                os_delay_us(32000);
            } else
            {
                os_printf("\ncannot get MAC\n");
            }
            break;

        case EVENT_STAMODE_GOT_IP:

            os_printf("\nGot ip\n");

            wifi_get_ip_info(STATION_IF, &ipconfig);
            os_memcpy(connection->proto.tcp->local_ip, &(e->event_info.got_ip.ip), 4);

            init_json_template(system_get_chip_id());

            init_sntp_server();
            os_delay_us(10000);

        case EVENT_STAMODE_DISCONNECTED:
            os_printf("\nWiFi connection lost\n");
            break;
        case EVENT_STAMODE_DHCP_TIMEOUT:
            os_printf("\nDHCP timeout\n");
            break;
        case EVENT_STAMODE_AUTHMODE_CHANGE:
            os_printf("\nAuthmode changed\n");
            break;
        default:
            os_printf("\nOther event happened\n");
            break;
    }
}

void initConnection()
{
    os_printf("\nPreparing SSL connection\n");

    espconn_secure_set_size(SSL_CLIENT, SSL_PACKET_SIZE);

    if (!espconn_secure_ca_enable(SSL_CLIENT, SSL_CERT_LOCATION))
        os_printf("\nERROR ----> Cannot enable certifiactes validation\n\r");
    if (!espconn_secure_cert_req_enable(SSL_CLIENT, SSL_PRIVATE_KEY_LOCATION))
        os_printf("\nERROR ----> Cannot enable private key validation\n\r");

    os_printf("HEAP SIZE: %d", system_get_free_heap_size());

    os_printf("\nInitialized cert requirement\n");

    connection = os_zalloc(sizeof(struct espconn));
    tcp_info = os_zalloc(sizeof(esp_tcp));


    connection->type = ESPCONN_TCP;
    connection->state = ESPCONN_NONE;
    tcp_info->remote_port = SERVER_PORT;
    os_memcpy(tcp_info->remote_ip, SERVER_IP, 4);
    tcp_info->local_port = espconn_port();
    connection->proto.tcp = tcp_info;

    os_sprintf(server_ip_str, "%d.%d.%d.%d", SERVER_IP[0], SERVER_IP[1], SERVER_IP[2], SERVER_IP[3]);
    os_printf("SERVER IP IN STRING:%s", server_ip_str);

    espconn_regist_connectcb(connection, connection_success_handler);
    espconn_regist_reconcb(connection, connection_failure_handler);

    os_printf("\nLeaving initConnection()\n\r");

}

void connection_failure_handler(void *conn, sint8 err)
{
    os_printf("\nConnection failed! Reason: ");

    os_printf("Failed connection IP: %d.%d.%d.%d \nPort: %d",
              connection->proto.tcp->remote_ip[0],
              connection->proto.tcp->remote_ip[1],
              connection->proto.tcp->remote_ip[2],
              connection->proto.tcp->remote_ip[3],
              connection->proto.tcp->remote_port
    );

    switch (err)
    {
        case ESPCONN_TIMEOUT :
            os_printf(" TIMEOUT\n\r");
            break;
        case ESPCONN_ABRT:
            os_printf(" ABORTED\n\r");
            break;
        case ESPCONN_RST:
            os_printf(" RESET\n\r");
            break;
        case ESPCONN_CLOSE:
            os_printf(" CLOSED\n\r");
            break;
        case ESPCONN_CONN:
            os_printf(" TCP connection failed\n\r");
            break;
        case ESPCONN_HANDSHAKE:
            os_printf(" SSL handshake failed\n\r");
            break;
        default:
            os_printf(" OTHER ERROR\n\r");
            break;
    }

//    ready_to_send = false;
    os_timer_disarm(&timer);
    os_timer_setfn(&timer, start_connection, NULL);
    os_timer_arm(&timer, 10000, 0);
}

void init_sntp_server()
{
    os_printf("\nstarting sntp server connection\n");
    sntp_setservername(0, "pool.ntp.org");
    sntp_init();

    os_timer_disarm(&timer);
    os_timer_setfn(&timer, (os_timer_func_t *) sntp_listener, NULL);
    os_timer_arm(&timer, 1000, 0);

    os_printf("\nLeaving init_sntp_server()\n");
}

void sntp_listener(void *arg)
{
    os_printf("\nEntering sntp listener\n");
    uint32 timestamp;

    if ((timestamp = sntp_get_current_timestamp()) == 0)
    {
        os_timer_arm(&timer, 1000, 0);
    } else
    {
        os_timer_disarm(&timer);
        os_printf("\nSNTP server returned timestamp: %d \n", timestamp);

        start_connection();
    }
    os_printf("\nLeaving sntp listener\n");
}

void start_connection()
{
    os_printf("\nSending secure connection request\n");
    sint8 connection_result = espconn_secure_connect(connection);
    os_delay_us(1000);

    switch (connection_result)
    {
        case 0:
            os_printf("\nConnection request sent\n");
            break;
        case ESPCONN_MEM:
            os_printf("\nERROR -----> Not enough memory for ssl connection\n");
            break;
        case ESPCONN_ARG:
            os_printf("\nERROR -----> Provided connection argument is invalid\n");
            break;
        default:
            os_printf("\nERROR -----> Connection already established\n");
            break;
    }
}

