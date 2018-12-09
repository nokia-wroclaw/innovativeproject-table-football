#include "c_types.h"

#define FLOAT_CONVERSION_PRECISION 10000

struct ParsedReading
{
    int int_part;
    int frac_part;
};

struct ParsedReading parsedReading;
char reading[10];

char *parse_readings(float x, float y, float z);

void parse_reading(float reading);

void reading_to_char();

char *parse_full_buffer(float *x, float *y, float *z, int bufferSize);

char *convert_buffer_to_string(float *buffer, int bufferSize);

void init_json_template(uint32 chip_id);


