#include "os_type.h"
#include "uart.h"
#include "user_config.h"
#include "event_handler.h"
#include "sntp.h"
#include "I2C.h"

uint32 ICACHE_FLASH_ATTR
user_rf_cal_sector_set(void)
{
    enum flash_size_map size_map = system_get_flash_size_map();
    uint32 rf_cal_sec = 0;

    switch (size_map)
    {
        case FLASH_SIZE_4M_MAP_256_256:
            rf_cal_sec = 128 - 8;
            break;

        case FLASH_SIZE_8M_MAP_512_512:
            rf_cal_sec = 256 - 5;
            break;

        case FLASH_SIZE_16M_MAP_512_512:
        case FLASH_SIZE_16M_MAP_1024_1024:
            rf_cal_sec = 512 - 5;
            break;

        case FLASH_SIZE_32M_MAP_512_512:
        case FLASH_SIZE_32M_MAP_1024_1024:
            rf_cal_sec = 1024 - 5;
            break;

        default:
            rf_cal_sec = 0;
            break;
    }

    return rf_cal_sec;
}


void ICACHE_FLASH_ATTR user_init()
{
    user_rf_cal_sector_set();
    uart_init(BIT_RATE_9600, BIT_RATE_9600);

    initI2C();
    os_delay_us(10000);
    initConnection();

    const char ssid[32] = SSID;
    const char password[32] = WIFI_PASS;

    struct station_config *stationConfig = (struct station_config *)
            os_malloc(sizeof(struct station_config));

    wifi_set_opmode(STATION_MODE);
    os_memcpy(stationConfig->ssid, ssid, 32);
    os_memcpy(stationConfig->password, password, 32);
    wifi_station_set_config(stationConfig);
    os_free(stationConfig);

    wifi_set_event_handler_cb(wifi_event_handler);

    os_printf("Connecting to wifi %s: \n", SSID);
    wifi_station_connect();

    os_delay_us(10000);

}




