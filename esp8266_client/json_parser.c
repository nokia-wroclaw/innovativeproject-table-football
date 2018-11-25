#include "json_parser.h"
#include "user_interface.h"
#include "osapi.h"
#include "mem.h"

char *MAC_ADDRESS = "00:21:33:22:55:78";

char *parse_readings(real32 x, real32 y, real32 z)
{
    os_printf("\nEntering parseReadings()\n");

    int x_int = (int) x;
    int y_int = (int) y;
    int z_int = (int) z;

    unsigned int fraction_x = (unsigned int) (x * FLOAT_CONVERSION_PRECISION) % FLOAT_CONVERSION_PRECISION;
    unsigned int fraction_y = (unsigned int) (y * FLOAT_CONVERSION_PRECISION) % FLOAT_CONVERSION_PRECISION;
    unsigned int fraction_z = (unsigned int) (z * FLOAT_CONVERSION_PRECISION) % FLOAT_CONVERSION_PRECISION;

    char *body_buffer = os_zalloc(512);
    os_sprintf(body_buffer, json_format, MAC_ADDRESS, x_int, fraction_x,
               y_int, fraction_y, z_int, fraction_z);

    os_printf("\nReturning data from parseReadings(): %s\n", json_format);

    return body_buffer;
}

void init_json_template(uint32 chip_id)
{
    os_printf("\nEntering init_json_template\n");
    os_printf("\nchip_id: %u\n", chip_id);

    json_format = (char *) os_zalloc(128);
    json_format = "{\"id\":\"%s\",\"readings\":[%d\056%d,%d\056%d,%d\056%d]}";
}
