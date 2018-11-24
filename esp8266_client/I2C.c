//
// Created by warchlak on 19.11.18.
//

#include <I2C.h>
#include <json_parser.h>
#include <mem.h>

#include "include/I2C.h"

int initI2C()
{
    i2c_master_gpio_init();
    i2c_master_init();

    setRange(SENSITIVITY_8G);
    output_rate = OUTPUT_RATE_800Hz;

    MMA845x_Standby();
    os_delay_us(100);
    configure_accelerometer();
    os_delay_us(100);
    MMA845x_Active();

    os_timer_disarm(&i2c_timer);
    os_timer_setfn(&i2c_timer, (os_timer_func_t *) gather_readings, NULL);
    os_timer_arm(&i2c_timer, 10000, 1);

    return 0;

}

bool configure_accelerometer()
{
    uint8 data = 0;
    read_from(FIFO_SETUP_REGISTER, &data);
    os_printf("FIFO_REGISTER is ");
    print_in_binary(data);

    write_to(SENSITIVITY_CFG_REGISTER, range);
    write_to(FIFO_SETUP_REGISTER, FIFO_DISABLED);

    if (HP_FILTER_ENABLED)
    {
        write_to(SENSITIVITY_CFG_REGISTER, range | HP_FILTER_MASK);
    } else
    {
        write_to(SENSITIVITY_CFG_REGISTER, range & ~HP_FILTER_MASK);
    }

    uint8_t config_reg1 = 0x00;
    config_reg1 = output_rate << OUTPUT_RATE_OFFSET;
    if (FAST_READ_ENABLED)
    {
        config_reg1 = config_reg1 | FAST_READ_MASK;
    }
    if (LOW_POWER_ENABLED)
    {
        config_reg1 = config_reg1 | LOW_POWER_MASK;
    }

    write_to(CTRL_REG_1, config_reg1);
    read_from(CTRL_REG_1, &data);
    os_printf("CTRL1 register before active is ");
    print_in_binary(data);
}

void gather_readings()
{
    if (start_transmission())
    {
        if (get_readings())
        {
            print_readings();
        } else
        {
            os_printf("\nError during communication with accelerometer\n");
        }
    }

    end_transmission();
}

bool start_transmission()
{
    i2c_master_start();
    i2c_master_writeByte(SLAVE_ADDRESS_WRITE);
    return i2c_master_checkAck();
}

bool end_transmission()
{
    i2c_master_stop();
}

bool get_readings()
{
    i2c_master_writeByte(OUT_X_MSB);

    if (!i2c_master_checkAck())
    {
        return false;
    }

    i2c_master_start();
    i2c_master_writeByte(SLAVE_ADDRESS_READ);

    if (!i2c_master_checkAck())
    {
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

    os_printf("\nX: %d\n", x);
    os_printf("\nX: %d\n", y);
    os_printf("\nX: %d\n", z);

    x_g = (float) x / divider * GRAVITY_CONSTANT;
    y_g = (float) y / divider * GRAVITY_CONSTANT;
    z_g = (float) z / divider * GRAVITY_CONSTANT;

    i2c_master_send_nack();

    return true;

}

bool write_to(uint8 reg, uint8 data)
{
    if (start_transmission())
    {
        i2c_master_writeByte(reg);
        if (!i2c_master_checkAck())
        {
            os_printf("\nError during communication with accelerometer\n");
            end_transmission();
            return false;
        }

        i2c_master_writeByte(data);
        if (!i2c_master_checkAck())
        {
            os_printf("\nError during communication with accelerometer\n");
            end_transmission();
            return false;
        }

    } else
    {
        os_printf("\nError during communication with accelerometer\n");
    }

    end_transmission();
}

bool read_from(uint8 reg, uint8 *data)
{
    if (start_transmission())
    {
        i2c_master_writeByte(reg);

        if (!i2c_master_checkAck())
        {
            os_printf("\nError during accelerometer read\n");
            i2c_master_send_nack();
            end_transmission();
            return false;
        }

        i2c_master_start();
        i2c_master_writeByte(SLAVE_ADDRESS_READ);

        if (!i2c_master_checkAck())
        {
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

void print_in_binary(int n)
{
    while (n)
    {
        if (n & 1)
            os_printf("1");
        else
            os_printf("0");

        n >>= 1;
    }
    os_printf("\n");
}

void MMA845x_Standby(void)
{
    uint8 current_register_status;
    if (!read_from(CTRL_REG_1, &current_register_status))
    {
        os_printf("\nCannot put accelerometer in standby mode -> reading error\n");
    }

    os_printf("\nCTRL1_REG before standby: ");
    print_in_binary(current_register_status);

    if (!write_to(CTRL_REG_1, current_register_status & ~1))
    {
        os_printf("\nCannot put accelerometer in standby mode -> register write error\n");
    }
}

void MMA845x_Active(void)
{
    uint8 current_register_status;
    if (!read_from(CTRL_REG_1, &current_register_status))
    {
        os_printf("\nCannot put accelerometer in active mode -> reading error\n");
    }

    os_printf("\nCTRL1_REG before active: ");
    print_in_binary(current_register_status);

    if (!write_to(CTRL_REG_1, current_register_status | 1))
    {
        os_printf("\nCannot put accelerometer in active mode -> register write error\n");
    }

    read_from(CTRL_REG_1, &current_register_status);
    os_printf("CTRL1 register after active is ");
    print_in_binary(current_register_status);
}

void setRange(uint8_t r)
{
    range = r;
}

void print_readings()
{
    os_printf("\nEntering print readings \n");

    int x_int = (int) x_g;
    int y_int = (int) y_g;
    int z_int = (int) z_g;

    unsigned int fraction_x = (unsigned int) (x_g * FLOAT_CONVERSION_PRECISION) % FLOAT_CONVERSION_PRECISION;
    unsigned int fraction_y = (unsigned int) (y_g * FLOAT_CONVERSION_PRECISION) % FLOAT_CONVERSION_PRECISION;
    unsigned int fraction_z = (unsigned int) (z_g * FLOAT_CONVERSION_PRECISION) % FLOAT_CONVERSION_PRECISION;

    os_printf("%d\056%d\t%d\056%d\t%d\056%d", x_int, fraction_x, y_int, fraction_y, z_int, fraction_z);
}












