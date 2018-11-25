#include "osapi.h"
#include "user_interface.h"
#include "espconn.h"
#include "mem.h"

extern bool ready_to_send;

void wifi_event_handler(System_Event_t * e);
void data_received_handler(void *conn_info,char *data, unsigned short len);
void connection_success_handler(void *connection);
void sent_success_handler(void *connection);
void initConnection();
void connection_failure_handler (void *conn, sint8 err);
void init_sntp_server();
void sntp_listener(void * arg);
void start_connection();
void send_request(void * arg);


