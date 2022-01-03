/**
 * Inhancing performance of the greenhouse, this program afford
 * user to set recommended values about temperature, humidity and lights.
 * 
 * These values are stored inside EEPROM of AtMega329P.
 * According to micro-controller's datasheet the amount of writing on EEPROM is
 * limited (about 2000 times), sure it is very high and AtMega is very cheap, but do not abuse!
 * 
 * WRITING IS LIMITED, READING IS NOT...!!!
 * 
 * 1) READING
 *      Recommended values are read from EEPROM every time AtMega is restarted via loadRcmdValues().
 *      Once read, they're stored inside relative arrays declared below.
 * 
 *      #################################################################    
 *      #Below I am gonna explaining how values are stored inside EEPROM#
 *      #################################################################
 * 
 * 2) WRITING
 *      The Android application I developed has own feature for setting new seed's recommended values.
 *      You can do it pressing NEW SEED button from main layout, writing new values and press SAVE
 * 
 *      Anyway, you can develop your own function for this. As data sent by HC-05 to smartphone, new recommended 
 *      values are sent as raw: 24 bytes:
 *          1-4:    Min temperature value
 *          5-8:    Max temperature value
 *          9-12:   Min humidity value
 *          13-16:  Max humidity value
 *          17-20:  Min light value
 *          21-24:  Max light value
 * 
 *      As said in main.h documentation, AtMega uses 2 bytes for representing integers inside memory, anyway I chose
 *      to do not mind about sending values represented with more than 2 bytes 'cause I don't suppose someone could 
 *      needed to have a recommended temperature value greater than 2^16. Then, humidity and light are represented as
 *      percentage. Anyway, later I could change from integer to short Android app's variable   
 *
 *  
 *  3) STORING INSIDE EEPROM
 *      As already said, AtMega uses 2 bytes for storing integer inside memory.
 *      Now, we're gonna threating 6 integers (Min(1) and Max(2) for each value(Temperature, 
 *      Humidity and Light)).
 * 
 *      EEPROM:
 *          1-2 bytes: min temperature value
 *          3-4 bytes: max temperature value
 * 
 *          5-6 bytes: min humidity value
 *          7-8 bytes: max humidity value
 * 
 *          9-10 bytes: min light value
 *          11-12 bytes: max light value
 */ 

#ifndef SEED_H
#define SEED_H

#include <EEPROM.h>


extern int temperature[2];
extern int humidity[2];
extern int light[2]; 

/**
 * Called on startup, it read recommended values about temperature, humidity and lights from EEPROM
 */  
void loadRcmdValues();

/**
 * Once user has sent new values, SoftwareSerial#available() will return an integer greater than 0;
 * this check is done in the loop function before send current values
 */ 
void setNewSeed(Stream* blue);

#endif
