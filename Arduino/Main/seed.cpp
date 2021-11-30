#include <SoftwareSerial.h>
#include "seed.h"


void loadRcmdValues() {

  /*
      EEPROM#read(int address) returns the byte stored at the given address
      inside EEPROM. Due to contiguos storing, we just need to increment the address var
  */
  int address = 0;

  //Reading temperature
  for (int i = 0; i < 2; i++) {
    temperature[i] = int( (unsigned char)EEPROM.read(address++) << 8 | (unsigned char)EEPROM.read(address++) & 0xFF);
  }

  //Reading humidity
  for (int i = 0; i < 2; i++) {
    humidity[i] = int( (unsigned char)EEPROM.read(address++) << 8 | (unsigned char)EEPROM.read(address++) & 0xFF);
  }

  //Reading light
  for (int i = 0; i < 2; i++) {
    light[i] = int( (unsigned char)EEPROM.read(address++) << 8 | (unsigned char)EEPROM.read(address++) & 0xFF);
  }

}


void setNewSeed(Stream &blue) {

  int address = 0;
  
  for(int i = 0; i < 3; i++){
    for(int j = 0; j < 2; j++){
      unsigned char x = blue.read(), y = blue.read();

      EEPROM.write(address++, x);
      EEPROM.write(address++, y);
    }
  }

  loadRcmdValues();
}
