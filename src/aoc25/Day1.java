package aoc25;

import java.io.*;

/**
 *
 * @author Antoni
 */
public class Day1 {
    
    /**
     * Character -> number.
     * @param digit
     * @return 
     */
    static int toInt(char digit) {
        return digit - '0';
    }
    
    /**
     * Read line from input and return rotation with sign.
     * If end of file, returns 0.
     * @param br
     * @return
     * @throws Exception 
     */
    static int getRotation(BufferedReader br) throws Exception {
        String line = br.readLine();
        if (line == null) {
            return 0;
        } else {
            char[] array = line.toCharArray();
            int dir;
            int amount;
            
            if (array[0] == 'L') {
                dir = -1;
            } else {
                dir = +1;
            }
            
            amount = 0;
            for (int i = 1; i < array.length; i++) {
                amount *= 10;
                amount += toInt(array[i]);
            }
            
            return dir * amount;
        }
    }

    /**
     * Count the times dial stops at zero.
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        FileReader fr = new FileReader("day/1/input");
        BufferedReader br = new BufferedReader(fr);
        
        int dial;
        int rotation;
        // Check the end result
        int totalStopsAtZero = 0;
        // The amount of times it has looped?
        int totalTimesThroughZero = 0;
        
        dial = 50;
        
        rotation = getRotation(br);
        
        while (rotation != 0) {
            int after = dial + rotation;
            // negative -> positive/zero OR positive > negative/zero
            // passes through zero and not accounted later
            if ((dial < 0 && (after == 0 || after > 0))
                    || (dial > 0 && (after == 0 || after < 0))) {
                totalTimesThroughZero++;
            }
            dial = after;
            // loops = number of times we have to apply mod
            int loops = Math.abs(dial / 100);
            totalTimesThroughZero += loops;
            dial = Math.floorMod(dial, 100);
            if (dial == 0) {
                totalStopsAtZero++;
            }
            
            rotation = getRotation(br);
        }
        
        System.out.println("TotalStopsAtZero = " + totalStopsAtZero);
        System.out.println("TotalTimesThroughZero = " + totalTimesThroughZero);
        
        br.close();
        fr.close();
    }
    
}
