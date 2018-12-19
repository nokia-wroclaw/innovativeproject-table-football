#include "json_parser.h"
//#include "user_interface.h"
#include "osapi.h"
#include "mem.h"
//#include "c_types.h"

char *MAC_ADDRESS;
char *single_reading_format = "%d.%d";
char *json_format = "{\"id\":\"%s\",\"x\": [%s], \"y\": [%s], \"z\": [%s] }";

char *parse_readings(real32 x, real32 y, real32 z) {
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

void parse_reading(float reading) {
//    os_printf("\nInside parse_reading()\n");
    int int_reading = (int) reading;
    int fraction_reading = (int) (reading * FLOAT_CONVERSION_PRECISION) % FLOAT_CONVERSION_PRECISION;
    if (fraction_reading < 0) {
        fraction_reading = -fraction_reading;
    }

    parsedReading.int_part = int_reading;
    parsedReading.frac_part = fraction_reading;

//    os_printf("\nLeaving parse_reading()\n");
}

void reading_to_char() {
//    os_printf("\n inside reading_to_char()\n");
//    os_printf("\nparsedReading.int_part = %d, parsedReading.frac_part = %d", parsedReading.int_part,
//              parsedReading.frac_part);
    os_sprintf(reading, single_reading_format, parsedReading.int_part, parsedReading.frac_part);
//    os_printf("\nLeaving reading_to_char() with reading %s", reading);
}

char *parse_full_buffer(float *x, float *y, float *z, int bufferSize) {
    char *json_string = (char *) os_zalloc(2048 * sizeof(char));

    os_printf("\n Located memory and made json_format\n");

    char *x_string = convert_buffer_to_string(x, bufferSize);
    char *y_string = convert_buffer_to_string(y, bufferSize);
    char *z_string = convert_buffer_to_string(z, bufferSize);

    os_printf("\n All buffers ready\n");
    os_printf("\n Formatting json\n");

    os_sprintf(json_string, json_format, MAC_ADDRESS, x_string, y_string, z_string);

    os_free(x_string);
    os_free(y_string);
    os_free(z_string);

    os_printf("\n Freed memory\n");

    os_printf("Formatted JSON: %s", json_string);

    return json_string;
}

char *convert_buffer_to_string(float *buffer, int bufferSize) {

    os_printf("\n Inside convert_buffer_to_string\n");

    char *buffer_string = (char *) os_zalloc(sizeof(char) * 512);
    char *buffer_string_start = buffer_string;

    int string_size = 0;
    int i = 0;
    for (i = 0; i < bufferSize - 1; ++i) {
        parse_reading(buffer[i]);
        reading_to_char();
        string_size = os_strlen(reading);
        reading[string_size] = '0';
        os_memcpy(buffer_string, reading, string_size);
        buffer_string += string_size;
        *buffer_string = ',';
        buffer_string++;
    }

    parse_reading(buffer[bufferSize - 1]);
    reading_to_char();
    string_size = os_strlen(reading);
    reading[string_size] = '0';
    memcpy(buffer_string, reading, string_size);
    buffer_string += string_size;
    *buffer_string = '\0';

    os_printf("\nComplete buffer: %s", buffer_string_start);
    return buffer_string_start;
}

void init_json_template(uint32 chip_id) {
    os_printf("\nEntering init_json_template\n");
    os_printf("\nchip_id: %u\n", chip_id);

//    json_format = (char *) os_zalloc(128);
//    json_format = "{\"id\":\"%s\",\"readings\":[%d\056%d,%d\056%d,%d\056%d]}";
}

void parseMAC() {
    MAC_ADDRESS = os_zalloc(sizeof(char) * 32);
    os_sprintf(MAC_ADDRESS, "%X:%X:%X:%X:%X:%X", mac[0], mac[1], mac[2], mac[3], mac[4], mac[5]);
}
