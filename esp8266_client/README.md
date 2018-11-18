Client application written in C using Espressif NO-OS-SDK.
Configuration needed before running application:
1. Change SSID and WIFI_PASS in user_config.h
2. Configure server and SSL connection parameters in ssl_conf.h
3. Add private_key.h and cert.h generated from your SSL cert and key using Espressif tools (files not included in repository)
4. Specify flash_sectors where you flashed bin files generated form ssl tools provided in the sdk (certificate and private key)
5. Provide http basic hash coresponding to your credentials in ssl_conf.h
6. Customize request format in requests.h
7. Change sdk location and (if needed) library location in Makefile (If you are using driver libraries remember to compile them)

Additional configuration in Makefile may be needed in case of different ESP8266 versions (Program runs on version 12F)

Makefile doesn't produce bootloader bin files (these are provided in the sdk), only user program.


