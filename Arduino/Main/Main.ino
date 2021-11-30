/**
 * FINIRE I DELAY ALLE VENTOLE.
 * STACCANO UNA VOLTA RAGGIUNTO IL MINIMO/MASSIMO, QUESTO CAUSA UN CONTINUO ACCESO/SPENTO CHE POTREBBE
 * FAR BRUCIARE LE VENTOLE...
 * 
 * LED PWM
 */


#include "main.h"

int temperature[2];
int humidity[2];
int light[2]; 

void setup(){
    Serial.begin(9600);
    blue.begin(9600);
    dht.begin();

    //Setting pins mode
    pinMode(DHT_DATA, INPUT);   //Reading data from DHT11
    pinMode(PHR, INPUT);        //Reading data from photoresistence
    pinMode(FAN_IN, OUTPUT);    
    pinMode(FAN_OUT, OUTPUT);

    //Setting output pins as LOW
    digitalWrite(FAN_IN, LOW);
    digitalWrite(FAN_OUT, LOW);

    //Reading, from EEPROM, recommended values 
            Serial.println("Reading seed...");
    loadRcmdValues();
        Serial.println("Min temperature: " + String(temperature[0]));
        Serial.println("Max temperature: " + String(temperature[1]));
        Serial.println("Min humidity: " + String(humidity[0]));
        Serial.println("Max humidity: " + String(humidity[1]));
        Serial.println("Min light: " + String(light[0]));
        Serial.println("Max light: " + String(light[1]));
}

void loop(){

    delay(1000);

    readT_H();
        calcLight();
    Serial.println(t);
    Serial.println(h);
    Serial.println(l);
    Serial.println("");

    //Returning if NULL value has been read from DHT11
    if(isnan(t) || isnan(h)){
        return;
    }

    bluetooth();

    fans();
}


void readT_H(){
   h = dht.readHumidity();
   t = dht.readTemperature();
}


void calcLight(){
    l = 100 - (analogRead(PHR) * 0.09);
}


void fans(){

    /*
        If the humidity is too high, it will cool-down even if temperature is low.
        Why? In order to pull out humidity, we have to set on pulling fan which 
        cool-down the greenhouse; now, if the temperature is too low, we should set on
        the pushing fan together resistence. 
        
        This means a waste of power! We should pull out air for reduce humidity and meanwhile
        push hot air?!?! Then before let's remove humidity, then it will check if greenhouse
        needs to be warmed-up

        The same line of reasoning is used when humidity is too low but temperature is too high
    */
    if(h > humidity[1]){
        Serial.println("Too much humidity");
        cool_down(true);
        watering(false);
        warm_up(false);
    }else if(h < humidity[0]){
        Serial.println("Too few humidity");
        //Humidity low but temperature high
        if(t > temperature[1]){
            cool_down(true);
            watering(false);
            warm_up(false);
        }
        //Humidity low and temperature low
        else{
            warm_up(true);
            watering(true);
            cool_down(false);
        }

    }else{

        if(t < temperature[0]){
          Serial.println("Warming-up");
            warm_up(true);
            cool_down(false);
            watering(false);
        }else if(t > temperature[1]){
          Serial.println("Cooling-Down");
            cool_down(true);
            warm_up(false);
            watering(false);
        }else{
            cool_down(false);
            warm_up(false);
            watering(false);
        }
    }
}


void bluetooth(){

    //SoftwareSerial#available() returs an integer equals to amount of bytes which can be read
    if(blue.available() > 1){
        Serial.println("Writing new seed...");
        setNewSeed(blue);

        Serial.println("Min temperature: " + String(temperature[0]));
        Serial.println("Max temperature: " + String(temperature[1]));
        Serial.println("Min humidity: " + String(humidity[0]));
        Serial.println("Max humidity: " + String(humidity[1]));
        Serial.println("Min light: " + String(light[0]));
        Serial.println("Max light: " + String(light[1]));

    }
    
    blue.write(t);
    blue.write(h);
    blue.write(l);
}


void warm_up(bool state){digitalWrite(FAN_IN, state ? HIGH : LOW);}
void cool_down(bool state){digitalWrite(FAN_OUT, state ? HIGH : LOW);}
void watering(bool state){digitalWrite(WATER_PUMP, state ? HIGH : LOW);}
