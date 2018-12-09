
# common
DRIVER_LOCATION = /home/user/projects/esp-open-sdk/sdk/driver_lib
DRIVER_HEADERS = $(DRIVER_LOCATION)/include/driver
DEFS =
CFLAGS :=
LDFLAGS :=
SDKPATH = /home/user/projects/esp-open-sdk/sdk
SDKINCLUDES = -I$(SDKPATH)/include -I$(DRIVER_HEADERS) -I./include
SDKLIBS = -L$(SDKPATH)/ld -L$(SDKPATH)/lib

# esp8266
ESP_DEFS = -DICACHE_FLASH -DUSE_US_TIMER -D__TARGET_ESP__ $(DEFS)
ESP_CC = xtensa-lx106-elf-gcc
ESP_AR = xtensa-lx106-elf-ar
ESP_CFLAGS = -I. $(SDKINCLUDES) \
	-mlongcalls -Os $(ESP_DEFS) $(CFLAGS)
ESP_LIBS = \
	-lmain \
	-lnet80211 \
	-lwpa \
	-llwip \
	-lpp \
	-lc \
	-lgcc \
	-lcrypto \
	-lssl \
	-lphy \
	-ldriver
ESP_LDFLAGS = -L. $(SDKLIBS) -Teagle.app.v6.ld $(LDFLAGS)
ESP_LINKFLAGS = $(ESP_LDFLAGS) \
	-nostdlib -fdata-sections -ffunction-sections \
	-Wl,-static -Wl,--gc-sections \
	-Wl,--start-group $(ESP_LIBS) -Wl,--end-group -lgcc

SOURCES = $(wildcard *.c)
HEADERS = $(wildcard *.h)
OBJECTS_ESP = $(SOURCES:.c=.o)

esp: esp-image-0x00000.bin

esp-image: $(OBJECTS_ESP) $(HEADERS)
	$(ESP_CC) $(ESP_CFLAGS) -o $@ \
		$(OBJECTS_ESP) $(ESP_LINKFLAGS)

esp-image-0x00000.bin: esp-image
	esptool.py elf2image $^

flash: esp-image-0x00000.bin
	esptool.py write_flash \
		0x00000 esp-image-0x00000.bin 0x10000 esp-image-0x10000.bin 

.c.o:
	$(ESP_CC) $(ESP_CFLAGS) $(ESP_INCLUDES) -c $< -o $@



clean:
	rm -f *.o
	rm -f *.a
	rm -f esp-image*.bin
	rm -f esp-image
	rm -f *~
