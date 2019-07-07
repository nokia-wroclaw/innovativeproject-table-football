#include "os_type.h"
#include "uart.h"
#include "user_config.h"
#include "event_handler.h"
#include "sntp.h"
#include "I2C.h"
#include <ets_sys.h>
#include "main.h"

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wimplicit-function-declaration"
#define INT_PIN 12
#define STATUS_CHECK_DELAY 30000
#define INTERRUPT_THRHESHOLD 5
#define DEBOUNCE_DELAY 500000
#define HEARTBEAT_INTERVAL 3600000
#define HEARTBEAT_INTERVAL_MICRO 3500000000

uint16 num_of_readings;
uint32 last_interrupt_timestamp;
uint32 last_send_timestamp;
os_timer_t status_check_timer;
os_timer_t heartbeat_timer;

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

void update_status()
{
    if (num_of_readings >= INTERRUPT_THRHESHOLD && ready_to_send)
    {
        num_of_readings = 0;
        last_send_timestamp = system_get_time();
        start_transmission();
        get_readings();
        end_transmission();
        start_connection();
    }
}

void motionEventInterrupt()
{
    uint32 gpio_status = 0;
    gpio_status = GPIO_REG_READ(GPIO_STATUS_ADDRESS);
    GPIO_REG_WRITE(GPIO_STATUS_W1TC_ADDRESS, gpio_status);

    uint8 data;
    read_from(MOTION_INT_SOURCE_REGISTER, &data);

    uint32 current_time = system_get_time();
    if (current_time - last_interrupt_timestamp >= DEBOUNCE_DELAY)
    {
        os_printf("\nMOTION EVEN DETECTED!\n");
        num_of_readings++;
    }
    last_interrupt_timestamp = system_get_time();
}

void handle_buffer_overflow()
{
    uint32 gpio_status = 0;
    gpio_status = GPIO_REG_READ(GPIO_STATUS_ADDRESS);
    GPIO_REG_WRITE(GPIO_STATUS_W1TC_ADDRESS, gpio_status);

    os_printf("\nI am inside interrupt\n");
    clear_overflow_flag();
    os_printf("\nCleared overflow flag\n");
    read_full_fifo_with_float_conversion();
    os_printf("\nReaded buffer\n");
    if (connection->state == ESPCONN_CLOSE)
    {
        is_connected = false;
        os_printf("\nRenewing connection\n");
        start_connection();
    } else if (is_connected && ready_to_send)
    {
        os_printf("\nCONNECTION STATE: %d\n", connection->state);
        send_request(NULL);
    }
}

void switchToMotionInterrupt()
{
    PIN_FUNC_SELECT(PERIPHS_IO_MUX_MTDI_U, FUNC_GPIO12);
    GPIO_DIS_OUTPUT(GPIO_ID_PIN(INT_PIN));
    ETS_GPIO_INTR_DISABLE();
    ETS_GPIO_INTR_ATTACH(motionEventInterrupt, NULL);
    gpio_pin_intr_state_set(GPIO_ID_PIN(12), GPIO_PIN_INTR_NEGEDGE);
    ETS_GPIO_INTR_ENABLE();
    MMA845x_Active();

    os_timer_disarm(&status_check_timer);
    os_timer_setfn(&status_check_timer, (os_timer_func_t *) update_status, NULL);
    os_timer_arm(&status_check_timer, STATUS_CHECK_DELAY, 1);
}

void switchToBufferOverflowInterrupt()
{
    PIN_FUNC_SELECT(PERIPHS_IO_MUX_MTDI_U, FUNC_GPIO12);
    GPIO_DIS_OUTPUT(GPIO_ID_PIN(INT_PIN));
    ETS_GPIO_INTR_DISABLE();
    ETS_GPIO_INTR_ATTACH(handle_buffer_overflow, NULL);
    gpio_pin_intr_state_set(GPIO_ID_PIN(12), GPIO_PIN_INTR_NEGEDGE);
    ETS_GPIO_INTR_ENABLE();
    MMA845x_Active();

//    os_timer_disarm(&status_check_timer);
//    os_timer_setfn(&status_check_timer, (os_timer_func_t *) update_status, NULL);
//    os_timer_arm(&status_check_timer, STATUS_CHECK_DELAY, 1);
}

void calibrationInterrupt()
{
    uint32 gpio_status = 0;
    gpio_status = GPIO_REG_READ(GPIO_STATUS_ADDRESS);
    GPIO_REG_WRITE(GPIO_STATUS_W1TC_ADDRESS, gpio_status);

    os_printf("\nI am inside interrupt\n");
    clear_overflow_flag();
    read_full_fifo();
    perform_calibration();

    configure_accelerometer();
    os_delay_us(100);

//    switchToMotionInterrupt();
    switchToBufferOverflowInterrupt();

}

void enableInterrupt()
{
    PIN_FUNC_SELECT(PERIPHS_IO_MUX_MTDI_U, FUNC_GPIO12);
    GPIO_DIS_OUTPUT(GPIO_ID_PIN(INT_PIN));
    ETS_GPIO_INTR_DISABLE();
//    ETS_GPIO_INTR_ATTACH(calibrationInterrupt, NULL);
    ETS_GPIO_INTR_ATTACH(handle_buffer_overflow, NULL);
    gpio_pin_intr_state_set(GPIO_ID_PIN(12), GPIO_PIN_INTR_NEGEDGE);
    ETS_GPIO_INTR_ENABLE();
//    MMA845x_Active();
}

void send_heartbeat()
{
    uint32 current_time = system_get_time();
    if (current_time - last_send_timestamp >= HEARTBEAT_INTERVAL_MICRO && ready_to_send)
    {
        last_send_timestamp = system_get_time();
        start_connection();
    }
}

void ICACHE_FLASH_ATTR

user_init()
{
//    os_timer_disarm(&heartbeat_timer);
//    os_timer_setfn(&heartbeat_timer, (os_timer_func_t *) send_heartbeat, NULL);
//    os_timer_arm(&heartbeat_timer, HEARTBEAT_INTERVAL, 1);


    uart_init(BIT_RATE_9600, BIT_RATE_9600);
    os_printf("\nLeaving deep sleep or starting\n");

    vcc_measure = system_get_vdd33();
    os_printf("VCC VOLTAGE : %d\n", vcc_measure);

    initConnection();

    int wakeupReading = 0;
    system_rtc_mem_read(64, &wakeupReading, sizeof(wakeupReading));

    allocateBufferMemory();
    user_rf_cal_sector_set();
    if (wakeupReading != 1)
    {
        enableInterrupt();
        os_printf("\nFirst Run accelerometer will be configured");
        initI2C();
    } else
    {
        os_printf("\nWaking up: accelerometer not configured");
        i2c_master_gpio_init();
//        i2c_master_init();
    }

//    switchToBufferOverflowInterrupt();
    setWakeupFlag(wakeupReading);

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

//    os_printf("Connecting to wifi %s: \n", SSID);
    wifi_station_connect();

    wakeupReading = 1;
    system_rtc_mem_write(64, &wakeupReading, sizeof(wakeupReading));

}


#pragma clang diagnostic pop