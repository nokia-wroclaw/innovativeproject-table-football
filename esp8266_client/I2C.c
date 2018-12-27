//
// Created by warchlak on 19.11.18.
//

//#include <I2C.h>
#include <json_parser.h>
#include <mem.h>

#include "include/I2C.h"

bool configure_for_calibration();

int initI2C() {
    i2c_master_gpio_init();
    i2c_master_init();

    fifo_buffer_x = os_zalloc(sizeof(sint16) * FIFO_SIZE);
    fifo_buffer_y = os_zalloc(sizeof(sint16) * FIFO_SIZE);
    fifo_buffer_z = os_zalloc(sizeof(sint16) * FIFO_SIZE);

    setRange(SENSITIVITY_2G);
    output_rate = OUTPUT_RATE_1_25Hz;

    MMA845x_Standby();
    os_delay_us(100);
//    configure_for_calibration();
    configure_accelerometer();
    os_delay_us(100);
    MMA845x_Active();


    return 0;

}

bool configure_for_calibration() {
//    write_to(OFF_X_REG, 0x0);
//    write_to(OFF_Y_REG, 0x0);
//    write_to(OFF_Z_REG, 0x0);
    write_to(FIFO_SETUP_REGISTER, FIFO_FILL);
    write_to(INTERRUPT_REGISTER, FIFO_INTERRUPT_ENABLE);
    write_to(INT_MAPPING_REGISTER, FIFO_INT_MAP_1);
    write_to(SENSITIVITY_CFG_REGISTER, SENSITIVITY_2G);
    write_to(CTRL_REG_1, output_rate << OUTPUT_RATE_OFFSET);
}

bool configure_accelerometer() {
    uint8 data = 0;
//    read_from(FIFO_SETUP_REGISTER, &data);
//    os_printf("FIFO_REGISTER is ");
//    print_in_binary(data);
    write_to(OFF_X_REG, 0x00);
    write_to(OFF_Y_REG, 0x00);
    write_to(OFF_Z_REG, 0x00);

    write_to(SENSITIVITY_CFG_REGISTER, range);

    if (FIFO_INTERRUPT_ENABLE) {
        write_to(FIFO_SETUP_REGISTER, FIFO_FILL);
        write_to(INTERRUPT_REGISTER, FIFO_INTERRUPT_ENABLE);
        write_to(INT_MAPPING_REGISTER, FIFO_INT_MAP_1);
    } else {
        write_to(FIFO_SETUP_REGISTER, FIFO_DISABLED);
    }

    if (HP_FILTER_ENABLED) {
        write_to(SENSITIVITY_CFG_REGISTER, range | HP_FILTER_MASK);
        write_to(HP_FILTER_CUTOFF_REGISTER, HP_FILTER_CUTOFF_2HZ);
    }

    uint8_t config_reg1 = 0x00;
    config_reg1 = output_rate << OUTPUT_RATE_OFFSET;
    if (FAST_READ_ENABLED) {
        config_reg1 = config_reg1 | FAST_READ_MASK;
    }
    if (LOW_POWER_ENABLED) {
        config_reg1 = config_reg1 | LOW_POWER_MASK;
    }

    write_to(CTRL_REG_1, config_reg1);
    read_from(CTRL_REG_1, &data);

    if (MOTION_ENABLED) {
        write_to(MOTION_CONFIG_REGISTER, MOTION_CONFIG_SETUP);
        write_to(MOTION_CONFIG_REGISTER, MOTION_CONFIG_SETUP);
        write_to(INTERRUPT_REGISTER, MOTION_ENABLE);
        write_to(INT_MAPPING_REGISTER, MOTION_INT_PIN_1);
        write_to(MOTION_THRESHOLD_REGISTER, MOTION_THRESHOLD);
        write_to(MOTION_DEBOUNCE_COUNTER_REGISTER, DEBOUNCE_COUNTER);
    }
}

void gather_readings() {
    if (start_transmission()) {
        if (get_readings()) {
            print_readings();
        } else {
            os_printf("\nError during communication with accelerometer\n");
        }
    }

    end_transmission();
}

bool start_transmission() {
    i2c_master_start();
    i2c_master_writeByte(SLAVE_ADDRESS_WRITE);
    return i2c_master_checkAck();
}

bool end_transmission() {
    i2c_master_stop();
}

bool get_readings() {
    i2c_master_writeByte(OUT_X_MSB);

    if (!i2c_master_checkAck()) {
        return false;
    }

    i2c_master_start();
    i2c_master_writeByte(SLAVE_ADDRESS_READ);

    if (!i2c_master_checkAck()) {
        return false;
    }

    x = i2c_master_readByte();
    i2c_master_send_ack();
    x <<= 8;
    x |= i2c_master_readByte();
    x >>= 2;
    i2c_master_send_ack();

    y = i2c_master_readByte();
    i2c_master_send_ack();
    y <<= 8;
    y |= i2c_master_readByte();
    y >>= 2;
    i2c_master_send_ack();

    z = i2c_master_readByte();
    i2c_master_send_ack();
    z <<= 8;
    z |= i2c_master_readByte();
    z >>= 2;

    uint16_t divider = 0;
    if (range == SENSITIVITY_8G) divider = 1024;
    if (range == SENSITIVITY_4G) divider = 2048;
    if (range == SENSITIVITY_2G) divider = 4096;

    x_g = (float) x / divider * GRAVITY_CONSTANT;
    y_g = (float) y / divider * GRAVITY_CONSTANT;
    z_g = (float) z / divider * GRAVITY_CONSTANT;

    i2c_master_send_nack();

    return true;

}

bool write_to(uint8 reg, uint8 data) {
    if (start_transmission()) {
        i2c_master_writeByte(reg);
        if (!i2c_master_checkAck()) {
            os_printf("\nError during communication with accelerometer\n");
            end_transmission();
            return false;
        }

        i2c_master_writeByte(data);
        if (!i2c_master_checkAck()) {
            os_printf("\nError during communication with accelerometer\n");
            end_transmission();
            return false;
        }

    } else {
        os_printf("\nError during communication with accelerometer\n");
    }

    end_transmission();
}


bool read_from(uint8 reg, uint8 *data) {
    if (start_transmission()) {
        i2c_master_writeByte(reg);

        if (!i2c_master_checkAck()) {
            os_printf("\nError during accelerometer read\n");
            i2c_master_send_nack();
            end_transmission();
            return false;
        }

        i2c_master_start();
        i2c_master_writeByte(SLAVE_ADDRESS_READ);

        if (!i2c_master_checkAck()) {
            os_printf("\nError during accelerometer read\n");
            i2c_master_send_nack();
            end_transmission();
            return false;
        }

        *data = i2c_master_readByte();
        i2c_master_send_nack();

        end_transmission();
    }
}

void print_in_binary(int n) {
    while (n) {
        if (n & 1)
            os_printf("1");
        else
            os_printf("0");

        n >>= 1;
    }
    os_printf("\n");
}

void MMA845x_Standby(void) {
    uint8 current_register_status;
    if (!read_from(CTRL_REG_1, &current_register_status)) {
        os_printf("\nCannot put accelerometer in standby mode -> reading error\n");
    }

    os_printf("\nCTRL1_REG before standby: ");
    print_in_binary(current_register_status);

    if (!write_to(CTRL_REG_1, current_register_status & ~1)) {
        os_printf("\nCannot put accelerometer in standby mode -> register write error\n");
    }
}

void MMA845x_Active(void) {
    uint8 current_register_status;
    if (!read_from(CTRL_REG_1, &current_register_status)) {
        os_printf("\nCannot put accelerometer in active mode -> reading error\n");
    }

    os_printf("\nCTRL1_REG before active: ");
    print_in_binary(current_register_status);

    if (!write_to(CTRL_REG_1, current_register_status | 1)) {
        os_printf("\nCannot put accelerometer in active mode -> register write error\n");
    }

    read_from(CTRL_REG_1, &current_register_status);
    os_printf("CTRL1 register after active is ");
    print_in_binary(current_register_status);
}

void setRange(uint8_t r) {
    range = r;
}

void print_readings() {
    os_printf("\nEntering print readings \n");

    int x_int = (int) x_g;
    int y_int = (int) y_g;
    int z_int = (int) z_g;

    unsigned int fraction_x = (unsigned int) (x_g * FLOAT_CONVERSION_PRECISION) % FLOAT_CONVERSION_PRECISION;
    unsigned int fraction_y = (unsigned int) (y_g * FLOAT_CONVERSION_PRECISION) % FLOAT_CONVERSION_PRECISION;
    unsigned int fraction_z = (unsigned int) (z_g * FLOAT_CONVERSION_PRECISION) % FLOAT_CONVERSION_PRECISION;

    os_printf("%d\056%d\t%d\056%d\t%d\056%d", x_int, fraction_x, y_int, fraction_y, z_int, fraction_z);
}

void clear_overflow_flag() {
    uint8 data;
    read_from(0x00, &data);
}

void read_full_fifo() {
    MMA845x_Standby();

    start_transmission();

    fifo_buffer_x = os_zalloc(sizeof(sint16) * FIFO_SIZE);
    fifo_buffer_y = os_zalloc(sizeof(sint16) * FIFO_SIZE);
    fifo_buffer_z = os_zalloc(sizeof(sint16) * FIFO_SIZE);

    i2c_master_writeByte(OUT_X_MSB);
    i2c_master_checkAck();
    i2c_master_start();
    i2c_master_writeByte(SLAVE_ADDRESS_READ);
    i2c_master_checkAck();

    int i;
    for (i = 0; i < FIFO_SIZE; i++) {
        x = i2c_master_readByte();
        i2c_master_send_ack();
        x <<= 8;
        x |= i2c_master_readByte();
        x >>= 2;
        i2c_master_send_ack();

        y = i2c_master_readByte();
        i2c_master_send_ack();
        y <<= 8;
        y |= i2c_master_readByte();
        y >>= 2;
        i2c_master_send_ack();

        z = i2c_master_readByte();
        i2c_master_send_ack();
        z <<= 8;
        z |= i2c_master_readByte();
        z >>= 2;
        i2c_master_send_ack();

        fifo_buffer_x[i] = x;
        fifo_buffer_y[i] = y;
        fifo_buffer_z[i] = z;


        os_printf("\nX: %d\n", x);
        os_printf("\nY: %d\n", y);
        os_printf("\nZ: %d\n", z);
    }

    end_transmission();

    os_printf("\nCalibration data acquired\n");
}

void read_full_fifo_with_float_conversion() {
    MMA845x_Standby();

    start_transmission();

    i2c_master_writeByte(OUT_X_MSB);
    i2c_master_checkAck();
    i2c_master_start();
    i2c_master_writeByte(SLAVE_ADDRESS_READ);
    i2c_master_checkAck();

    uint16_t divider = 0;
    if (range == SENSITIVITY_8G) divider = 1024 / GRAVITY_CONSTANT;
    if (range == SENSITIVITY_4G) divider = 2048 / GRAVITY_CONSTANT;
    if (range == SENSITIVITY_2G) divider = 4096 / GRAVITY_CONSTANT;


    int i;
    for (i = 0; i < FIFO_SIZE; i++) {
        x = i2c_master_readByte();
        i2c_master_send_ack();
        x <<= 8;
        x |= i2c_master_readByte();
        x >>= 2;
        i2c_master_send_ack();

        y = i2c_master_readByte();
        i2c_master_send_ack();
        y <<= 8;
        y |= i2c_master_readByte();
        y >>= 2;
        i2c_master_send_ack();

        z = i2c_master_readByte();
        i2c_master_send_ack();
        z <<= 8;
        z |= i2c_master_readByte();
        z >>= 2;
        i2c_master_send_ack();

        fifo_buffer_x[i] = x;
        fifo_buffer_y[i] = y;
        fifo_buffer_z[i] = z;

        x_buffer[i] = ((float) x) / divider;
        y_buffer[i] = ((float) y) / divider;
        z_buffer[i] = ((float) z) / divider;
    }

    end_transmission();

    MMA845x_Active();
}

void perform_calibration() {
    sint32 x_offset = 0;
    sint32 y_offset = 0;
    sint32 z_offset = 0;

    uint8 x_value;
    uint8 y_value;
//    uint8 z_value;


    sint16_t divider = 8;

    int i;
    for (i = 0; i < FIFO_SIZE; i++) {
        x_offset += (fifo_buffer_x[i]);
        y_offset += (fifo_buffer_y[i]);
//        z_offset += (fifo_buffer_z[i]);
    }

    x_offset = (x_offset / FIFO_SIZE);
    y_offset = (y_offset / FIFO_SIZE);
    z_offset = (z_offset / FIFO_SIZE);

    x_offset = -x_offset / divider;
    y_offset = -y_offset / divider;
//    z_offset = -z_offset / divider;

//    os_printf("\nXoffset: %d\n", x_offset);
//    os_printf("\nYoffset: %d\n", y_offset);
//    os_printf("\nZoffset: %d\n", z_offset);

    x_value = (uint8) x_offset;
    y_value = (uint8) y_offset;
//    z_value = (uint8) z_offset;

//    os_printf("\nXvalue: %d\n", x_value);
//    os_printf("\nYvalue: %d\n", y_value);
//    os_printf("\nZvalue: %d\n", z_value);

    write_to(OFF_X_REG, x_value);
    write_to(OFF_Y_REG, y_value);
//    write_to(OFF_Y_REG, z_value);

    os_free(fifo_buffer_x);
    os_free(fifo_buffer_y);
    os_free(fifo_buffer_z);
}












