/**
 * This is the Arduino program which I chose for the exam of secondary school
 * 
 * Its purpose is to control a little greenhouse about temperature, humidity and light 
 * percentage.
 * 
 * 
 * 1) TEMPERATURE AND HUMIDITY:
 *      Temperature and humidity are controlled via DHT11.
 * 
 * 
 * 2) LIGHT
 *      Light percentage is controlled via a current divider made with a 10kohm 
 *      in serie of a photoresistor, which current will be input in an analogic pin.
 * 
 * 
 * 3) DATA TRANSFER
 *      Temperature, humidity and light values will be transmitted via an HC-05
 * 
 *      Even I developed an application for reading these values, you can develop your, above all
 *      'cause mine is Android-only. Now, in order to develop your, you just need to know how values
 *      are transmitted: 3 integers (First: temperature, Second: Humidity, Third: Light).
 * 
 *      Notice that AtMega328P stores integer in memory with 2 bytes.
 *      Smartphone use 4 bytes to store integer; anyway there's no problem... If you take a look 
 *      at my Android source code, you will see a 12 bytes array declared...
 *      Even if Arduino sends 2 bytes for integer, it will be saved as 4.
 *
 * 
 * 4) WARM UP/COOL DOWN
 * 
 *      This greenhouse is composed of two fans, too: 
 *      
 *          1): Pulling air outside (Cooling-Down).
 * 
 *          2): Pushing air inside (Warming-Up): a power resistence is behind it which warms up the
 *              air flow fan pushed inside. This fan is on with resistence 
 * 
 *      Being 12V fans, we need to control them with relays which are controlled via transistor. 
 *      2 digital pins will be reserved to control two 2N2222As NPN general purpose transistor
 *      in saturation mode:
 *          
 *          2N2222A's collector receives current from relay's coil - I am using HY4100F-DC12V
 *          with a coil's resistence of 960 ohm -.
 *          2N2222A's emitter is connected to GND of Power Supply PCB
 *          2N2222A's base is connected to relative pin with a resistence of 6.8Kohm
 * 
 *          FOR MORE INFO READ info_transistor.txt
 *  
 * 
 * 5) WATERING
 *      I am not sure to develop this feature. Of course the water should be the first thing in
 *      a greenhouse. I will think on...
 *      
 * 
 * The pins which are set below could be modified once I making PCB due to CROSSING-TRACK problem
 * due to one-layer PCB and AtMega328P schema. I am thinking to use metal bridge... 
 * 
 * I am not gonna using a whole Arduino Developing Board. I am using it only for developing.
 * Once done, I will use an another bought AtMega328P, which will have been burnt bootloader on. 
 * 
 * 
 * @author Gabriele-P03
 */

#ifndef MAIN_H
#define MAIN_H

#include "seed.h"

#define LED_STATE 3

//Buffers
float t, h, l;
/**
 * Before read this documentation, please read the Main#fans() one.
 * This count var is used for on fans' time
 */ 
int count;

typedef unsigned int GHstate;
GHstate state;
/*
    The first two (0 and 1) states will be taken from HC-05's state pin conencted to ATMega328P's 10th pin
*/
#define NOT_CON 0   //No device is connected to HC-05
#define CONN 1      //Device connected to HC-05

#define TRY_CONN 2
#define READ_SEED 3     //Blink twice when reading seed from EEPROM
#define WRITE_SEED 4    //Blink three times when writing new seed into EEPROM

//Blink state GreenHouse led how many times @param state says. 0 off - 1 on and so on blinking
void blink();


//DHT11 PART

#include <DHT_U.h>
#include <DHT.h>
#define DHTTYPE DHT11
//Pin data dht11
#define DHT_DATA 2
DHT dht(DHT_DATA, DHTTYPE);

/**
 * DHT11 function
 * 
 * Read temperature and humidity from DHT11 and store them into buffers
 */
void readT_H(); 




//BLUETOOTH PART

/*
    Cross pins: 
        Bluetooth device's receive pin will RECEIVE data from Arduino and transmit to other BLT device
        Bluetooth device's transmit pin will RECEIVE data from other BLT device and transmit to Arduino

*/
#define BT_TX_PIN 12
#define BT_RX_PIN 11
#define BT_STATE 10
#include <SoftwareSerial.h>
SoftwareSerial* blue;
/**
 * Bluetooth function
 * 
 * Once data are measured/calculated, they're sent to smartphone.
 * 
 * Once user press SAVE from NEW SEED popup view, smartphone will send new values
 * which will be received by HC-05 and saved into EEPROM
 */ 
void bluetooth();




//PHOTORESISTOR PART

//Being a current divider, it needs an analog input in order to read how much current is flowing inside
#define PHR A1
/**
 * Photoresistor function
 * 
 * Calculate the percentage of light inside the greenhouse using
 * current which flow through the current divider
 */
void calcLight();





//FANS PART

//Pushing fan. WARMING UP, common with POWER RES
#define FAN_IN 7

//Pulling fan. COOLING-DOWN.
#define FAN_OUT 8

/**
 * Fans function
 * 
 * Once all current values are measured, they're checked in order to 
 * warm-up/cool-down if needed. 
 * If fans are on/off too much frequently, they may burn up. In order to prevent it, once on, they'll be for about 20 seconds
 */ 
void fans();

/**
 * Once called, it set state of the fan which warm-up the greenhouse as state
 * @param state
 * 
 * Remember that power resistor, which warm-up the air pushed inside, will be on
 * with
 */ 
void warm_up(bool state);

/**
 * Once called, it set the state of the fan which cool-down the greenhouse as state
 * @param state
 */ 
void cool_down(bool state);




//WATER PUMP PART

//Transistor or relay for activating water pump...
#define WATER_PUMP 9
/**
 * Once called, it set the state of the pump as state
 * @param state
 */ 
void watering(bool state);


#endif
