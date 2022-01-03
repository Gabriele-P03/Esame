/**
 * 
 * LED PWM
 */

#include "main.h"

int temperature[2];
int humidity[2];
int light[2];

void setup()
{

    //Setting pins mode
    pinMode(DHT_DATA, INPUT); //Reading data from DHT11
    pinMode(PHR, INPUT);      //Reading data from photoresistence
    pinMode(FAN_IN, OUTPUT);
    pinMode(FAN_OUT, OUTPUT);
    pinMode(LED_STATE, OUTPUT);

    pinMode(BT_STATE, INPUT);
    blue = new SoftwareSerial(BT_RX_PIN, BT_TX_PIN);

    Serial.begin(9600);
    blue->begin(9600);
    dht.begin();

    //Setting output pins as LOW
    digitalWrite(FAN_IN, LOW);
    digitalWrite(FAN_OUT, LOW);

    //Reading, from EEPROM, recommended values
    Serial.println("Reading seed...");
    state = READ_SEED;
    loadRcmdValues();
        Serial.println("Min temperature: " + String(temperature[0]));
    Serial.println("Max temperature: " + String(temperature[1]));
    Serial.println("Min humidity: " + String(humidity[0]));
    Serial.println("Max humidity: " + String(humidity[1]));
    Serial.println("Min light: " + String(light[0]));
    Serial.println("Max light: " + String(light[1]));

}

void loop()
{
    
    delay(1000);

    readT_H();
    calcLight();

    //These will be commented. Thay're just debugging purpose
    //Serial.println(t);Serial.println(h);Serial.println(l);

    bluetooth();
    fans();
    blink();
}

void readT_H()
{
    h = dht.readHumidity();
    t = dht.readTemperature();

    //Returning if NULL value has been read from DHT11
    if (isnan(t) || isnan(h))
    {
        return;
    }
}

void calcLight() { l = 100 - (analogRead(PHR) * 0.09); }

void fans()
{

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
    if (count <= 0)
    {
        if (h > humidity[1])
        {
            Serial.println("Too much humidity");
            cool_down(true);
            watering(false);
            warm_up(false);
            ++count;
        }
        else if (h < humidity[0])
        {
            Serial.println("Too few humidity");
            //Humidity low but temperature high
            if (t > temperature[1])
            {
                cool_down(true);
                watering(false);
                warm_up(false);
                ++count;
            }
            //Humidity low and temperature low
            else
            {
                warm_up(true);
                watering(true);
                cool_down(false);
                ++count;
            }
        }
        else
        {

            if (t < temperature[0])
            {
                Serial.println("Warming-up");
                warm_up(true);
                cool_down(false);
                watering(false);
                ++count;
            }
            else if (t > temperature[1])
            {
                Serial.println("Cooling-Down");
                cool_down(true);
                warm_up(false);
                watering(false);
                ++count;
            }
            else
            {
                cool_down(false);
                warm_up(false);
                watering(false);
            }
        }
    }
    else if (++count >= 20)
    {
        //Once about 20 seconds are over, fan can be shutdown
        count = 0;
    }
}

void bluetooth()
{

    //SoftwareSerial#available() returs an integer equals to amount of bytes which can be read
    if (blue->available() > 0)
    {

        if (blue->available() < 2)
        {
            state = TRY_CONN;
            Serial.print("A device is tryna connecting...");
            unsigned char buf;
            buf = blue->read();
            Serial.print(buf);
            if (buf == 'c')
            {
                Serial.println(": Done!");
                blue->write("ok");
            }
            else
            {
                Serial.println(": Error!");
            }
        }
        else
        {
            state = WRITE_SEED;
            Serial.println("Writing new seed...");
            setNewSeed(blue);

            Serial.println("Min temperature: " + String(temperature[0]));
            Serial.println("Max temperature: " + String(temperature[1]));
            Serial.println("Min humidity: " + String(humidity[0]));
            Serial.println("Max humidity: " + String(humidity[1]));
            Serial.println("Min light: " + String(light[0]));
            Serial.println("Max light: " + String(light[1]));
        }
    }

    blue->write((unsigned char)(int)t);
    blue->write((unsigned char)(int)h);
    blue->write((unsigned char)(int)l);
}

void warm_up(bool state) { digitalWrite(FAN_IN, state ? HIGH : LOW); }
void cool_down(bool state) { digitalWrite(FAN_OUT, state ? HIGH : LOW); }
void watering(bool state) { digitalWrite(WATER_PUMP, state ? HIGH : LOW); }

void blink()
{

    if (state <= 0)
    {
        digitalWrite(LED_STATE, LOW);
    }
    else if (state == 1)
    {
        digitalWrite(LED_STATE, HIGH);
    }
    else if (state > 1)
    {   
        digitalWrite(LED_STATE, LOW); //Reset LED
        delay(500); //Without delay, it seems like led doesn't reset
        Serial.println(state);
        for (int i = 0; i < state; i++)
        {
            digitalWrite(LED_STATE, HIGH);
            delay(250);
            digitalWrite(LED_STATE, LOW);
            delay(250);
        }
    }

    state = digitalRead(BT_STATE);
}
