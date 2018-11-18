#define GET_REQUEST_FORM "GET %s HTTP/1.1\r\nHost: %s\r\nAccept: */*\r\n\r\n"
#define POST_JSON_FORM "POST %s HTTP/1.1\r\nHost: %s\r\nAccept: */*\r\nContent-Type: application/json\r\nAuthorization: Basic %s\r\nContent-Length: %d\r\n\r\n%s"
#define HTTPS_SCHEMA "https://"

