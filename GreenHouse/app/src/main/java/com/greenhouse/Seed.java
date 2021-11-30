/**
 * This is a simple abstraction of min and max value of temperature, humidity and light
 * for a better life of plant
 *
 *
 * @author Gabriele-P03
 */

package com.greenhouse;

import android.content.Context;
import android.widget.Toast;

import java.io.*;

public class Seed {

    private int minTemp, maxTemp, minHum, maxHum, minLight, maxLight;

    public Seed(int minTemp, int maxTemp, int minHum, int maxHum, int minLight, int maxLight) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.minHum = minHum;
        this.maxHum = maxHum;
        this.minLight = minLight;
        this.maxLight = maxLight;
    }

    public Seed() {
    }

    public int getMinTemp() { return minTemp; }
    public void setMinTemp(int minTemp) { this.minTemp = minTemp; }
    public int getMaxTemp() { return maxTemp; }
    public void setMaxTemp(int maxTemp) { this.maxTemp = maxTemp; }
    public int getMinHum() { return minHum; }
    public void setMinHum(int minHum) { this.minHum = minHum; }
    public int getMaxHum() { return maxHum; }
    public void setMaxHum(int maxHum) { this.maxHum = maxHum; }
    public int getMinLight() { return minLight; }
    public void setMinLight(int minLight) { this.minLight = minLight; }
    public int getMaxLight() { return maxLight; }
    public void setMaxLight(int maxLight) { this.maxLight = maxLight; }

    /**
     * Write the new seed's values on memory in a simple text file.
     * Only one line, separated by a comma
     * @param context
     */
    public void saveNewSeed(Context context){

        File file = new File(context.getFilesDir(), "seed.txt");
        try {

            if(!file.exists()){
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file);
            fw.write(this.minTemp + "," + this.maxTemp + "," + this.minHum + "," + this.maxHum + "," + this.minLight + "," + this.maxLight);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read values from seed.txt file and stores them into a new instance of {@link Seed}
     * @param context
     * @throws IOException if file could not be created or read
     * @throws RuntimeException if file is corrupted
     * @return the new instance of seed with the read values
     */
    public static Seed readSeedFromFile(Context context){
        File file = new File(context.getFilesDir(), "seed.txt");
        try {

            if(!file.exists()){
                file.createNewFile();
            }

            BufferedReader br = new BufferedReader(new FileReader(file));

            //In file, all values are separated by a comma
            String lineRead = br.readLine();

            if(lineRead != null) {

                String[] lines = lineRead.split(",");

                //It should read a total of 6 integers. If it could not read them, an exception will be thrown and toast will appear
                if (lines.length == 6) {
                    return new Seed(
                            Integer.parseInt(lines[0]), Integer.parseInt(lines[1]),
                            Integer.parseInt(lines[2]), Integer.parseInt(lines[3]),
                            Integer.parseInt(lines[4]), Integer.parseInt(lines[5])
                    );
                } else {
                    Toast.makeText(context, "Error during reading values...\nYou should clear all data about this application", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException("Error during reading values. Lines read: " + lines.length);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Seed();
    }

}
