/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aoc25;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 *
 * @author Antoni
 */
public class Day5 {
    static Range[] ranges;
    static long[] ids;
    
    record Range(long low, long high) {
        
    }
    
    static void getData(String path) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            
            LinkedList<Range> llRanges = new LinkedList<>();
            LinkedList<Long> llIds = new LinkedList<>();

            String line = br.readLine();
            while (line != null && !line.isEmpty()) {
                String[] split = line.split("-");
                llRanges.add(new Range(
                        Long.parseLong(split[0]),
                        Long.parseLong(split[1])
                ));
                line = br.readLine();
            }

            line = br.readLine();
            while (line != null) {
                llIds.add(Long.valueOf(line));
                line = br.readLine();
            }

            br.close();
            fr.close();
            
            // For part 2
            Collections.sort(llRanges, (Range o1, Range o2) -> {
                if (o1.low < o2.low) {
                    return -1;
                } else if (o1.low > o2.low) {
                    return +1;
                } else {
                    if (o1.high < o2.high) {
                        return -1;
                    } else if (o1.high > o2.high) {
                        return +1;
                    } else {
                        return 0;
                    }
                }
            });
            
            ranges = llRanges.toArray(Range[]::new);
            ids = llIds.stream().mapToLong(l->l).toArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        getData("day/5/input");
        
        int countFresh = 0;
        
        for (int i = 0; i < ids.length; i++) {
            long id = ids[i];
            boolean foundRange = false;
            for (int j = 0; j < ranges.length && !foundRange; j++) {
                Range r = ranges[j];
                if (id >= r.low && id <= r.high) {
                    foundRange = true;
                }
            }
            if (foundRange) {
                countFresh++;
            }
        }
        
        System.out.println("countFresh = " + countFresh);
        
        // Part 2:
        /*
        1. Sort the ranges.
        2. For each range, check the ones that overlap and make them start after it.
        3. Count the values in each range.
        */
        long totalFresh = 0;
        for (int i = 0; i < ranges.length; i++) {
            Range myRange = ranges[i];
            if (myRange.low > myRange.high) {
                // This range got shrinked to oblivion, ignore it
                continue;
            }
            // shrink the following ones that overlap
            for (int j = i + 1; j < ranges.length; j++) {
                Range next = ranges[j];
                if (next.high >= myRange.low && next.low <= myRange.high) {
                    // immutable ¬¬
                    // next.low = myRange.high + 1;
                    ranges[j] = new Range(myRange.high + 1, next.high);
                }
            }
            // count the ids in myRange
            totalFresh += myRange.high - myRange.low + 1;
        }
        
        System.out.println("totalFresh = " + totalFresh);
    }
    
}
