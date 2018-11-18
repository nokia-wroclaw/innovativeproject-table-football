#include "event_handler.h"
#include "requests.h"
#include "json_parser.h"
#include "cert.h"
#include "private_key.h"
#include "sntp.h"
#include "ssl_conf.h"

//uint8 SERVER_IP[4] = {78, 8, 153, 74};
//char *server_ip_str = "78.8.153.74";
char server_ip_str[16];

struct espconn *connection;
esp_tcp *tcp_info;
struct ip_info ipconfig;

os_timer_t timer;

void connection_success_handler(void *connection)
{

    os_printf("Successful connection to server\n");
    os_printf("HEAP SIZE: %d", system_get_free_heap_size());

    espconn_regist_recvcb(connection, data_received_handler);
    espconn_regist_sentcb(connection, sent_success_handler);

    send_request(NULL);

}

void send_request(void *arg)
{
    char *data_to_send;

    os_printf("Allocating data buffer\n");

    data_to_send = (char *) os_zalloc(SSL_PACKET_SIZE);

    os_printf("Data buffer allocated\n");

    os_delay_us(1000);

    char *body = parse_readings(1.233223, 2.334434, 3.232243);
    int content_length = os_strlen(body);

    os_sprintf(data_to_send, POST_JSON_FORM,
               SENSOR_URL, server_ip_str,
               BASIC_HASH,
               content_length,
               body);


    os_printf("DATA TO SEND: %s\n", data_to_send);

    os_printf("Sending secure post request");
    espconn_secure_send(connection, data_to_send, os_strlen(data_to_send));
    os_free(body);
    os_free(data_to_send);

    os_printf("Request sent\n");
}

void sent_success_handler(void *connection)
{
    os_printf("Data has been successfully sent\n");

    os_timer_disarm(&timer);
    os_timer_setfn(&timer, (os_timer_func_t *) send_request, NULL);
    os_timer_arm(&timer, 10000, 0);
}

void data_received_handler(void *conn_info, char *data, unsigned short len)
{
    os_printf("SERVER RESPONSE:\n %s", data);
}

void wifi_event_handler(System_Event_t *e)
{
    switch (e->event)
    {
        case EVENT_STAMODE_CONNECTED:
            os_printf("Connected to SSID: %s \n", e->event_info.connected.ssid);
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
    os_printf("SERVER IP IN STRING:%s",server_ip_str);

    espconn_regist_connectcb(connection, connection_success_handler);
    espconn_regist_reconcb(connection, connection_failure_handler);

    os_printf("\nLeaving initConnection()\n\r");

}

void connection_failure_handler(void *conn, sint8 err)
{
    os_printf("\nConnection failed! Reason: ");

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

