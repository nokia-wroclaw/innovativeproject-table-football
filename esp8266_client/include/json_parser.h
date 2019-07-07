#include "c_types.h"

#define FLOAT_CONVERSION_PRECISION 100000

struct ParsedReading
{
    int int_part;
    int frac_part;
};

char mac[6];

struct ParsedReading parsedReading;
char reading[10];

char *parse_readings(float x, float y, float z);

void parse_reading(float reading);

void reading_to_char();

char *parse_request_body(uint16_t  vcc_data, float *x, float *y, float *z, int bufferSize);

char *convert_buffer_to_string(float *buffer, int bufferSize);

uint8 hex_to_int(const char c);

void parseMAC();

void init_json_template(uint32 chip_id);



