//
// Created by warchlak on 19.11.18.
//

#include <i2c_master.h>
#include <c_types.h>
#include <osapi.h>

#define GRAVITY_CONSTANT 9.80665f

#define SLAVE_ADDRESS 0x1c
#define SLAVE_ADDRESS_WRITE 0x38
#define SLAVE_ADDRESS_READ 0x39

#define OUT_X_MSB 0x01
#define OUT_X_LSB 0x02
#define OUT_Y_MSB 0x03
#define OUT_Y_LSB 0x04
#define OUT_Z_MSB 0x05
#define OUT_Z_LSB 0x06

#define OFF_X_REG 0x2f
#define OFF_Y_REG 0x30
#define OFF_Z_REG 0x31


#define SENSITIVITY_CFG_REGISTER 0x0e
#define SENSITIVITY_8G 0x02
#define SENSITIVITY_4G 0x01
#define SENSITIVITY_2G 0x00

#define FIFO_SETUP_REGISTER 0x09
#define FIFO_DISABLED 0x00
#define FIFO_CIRCURAL_MODE 0x40
#define FIFO_FILL 0x80

#define HP_FILTER_ENABLED 0x00
#define HP_FILTER_MASK (0x01<<4)
#define HP_FILTER_CUTOFF_REGISTER 0x0f
#define HP_FILTER_CUTOFF_16HZ 0x00
#define HP_FILTER_CUTOFF_8HZ 0x01
#define HP_FILTER_CUTOFF_4HZ 0x02
#define HP_FILTER_CUTOFF_2HZ 0x03

#define CTRL_REG_1 0x2a
#define FAST_READ_MASK (0x01<<1)
#define LOW_POWER_MASK (0x01<<2)
#define FAST_READ_ENABLED 0
#define LOW_POWER_ENABLED 0
#define OUTPUT_RATE_OFFSET 3

#define OUTPUT_RATE_800Hz 0x00
#define OUTPUT_RATE_400Hz 0x01
#define OUTPUT_RATE_200Hz 0x02
#define OUTPUT_RATE_100Hz 0x03
#define OUTPUT_RATE_50Hz 0x04
#define OUTPUT_RATE_12_5Hz 0x05
#define OUTPUT_RATE_6_25Hz 0x06
#define OUTPUT_RATE_1_25Hz 0x07

#define INTERRUPT_REGISTER 0x2d
#define FIFO_INTERRUPT_ENABLE 0x40

#define INT_MAPPING_REGISTER 0x2e
#define FIFO_INT_MAP_1 0x40

#define FIFO_SIZE 32

#define MOTION_ENABLED 0
#define MOTION_CONFIG_REGISTER 0x15
#define MOTION_INT_SOURCE_REGISTER 0x16
#define MOTION_CONFIG_SETUP 0xD8
#define MOTION_ENABLE 0x04
#define MOTION_INT_PIN_1 0x04
#define MOTION_THRESHOLD_REGISTER 0x17
#define MOTION_THRESHOLD 0x04
#define MOTION_DEBOUNCE_COUNTER_REGISTER 0x18
#define DEBOUNCE_COUNTER 0x02

sint16_t x;
sint16_t y;
sint16_t z;

float x_g;
float y_g;
float z_g;

uint8_t range;
uint8_t output_rate;

sint16 * fifo_buffer_x;
sint16 * fifo_buffer_y;
sint16 * fifo_buffer_z;

float x_buffer[32];
float y_buffer[32];
float z_buffer[32];

os_timer_t i2c_timer;

int initI2C();

void read_full_fifo_with_float_conversion();

void gather_readings();

bool start_transmission();

bool end_transmission();

bool get_readings();

bool write_to(uint8 reg, uint8 value);

bool read_from(uint8 reg, uint8 *data);

bool configure_accelerometer();

void print_in_binary(int n);

void MMA845x_Standby(void);

void MMA845x_Active(void);

void setRange(uint8_t r);

void print_readings();

void perform_calibration();

void clear_overflow_flag();

void read_full_fifo();
