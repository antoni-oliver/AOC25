/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aoc25;

import java.util.LinkedList;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Antoni
 */
public class Day6 {

    static int[][] data;
    static ArrayList<Integer>[] data2;
    static char[] ops;
    static int[] offsets;
    
    static void getData(String path) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            
            LinkedList<int[]> rows = new LinkedList<>();
            String line;
            
            while ((line = br.readLine()) != null) {
                String[] split = line.trim().split("\\s+");
                if ("+".equals(split[0]) || "*".equals(split[0])) {
                    ops = new char[split.length];
                    for (int i = 0; i < ops.length; i++) {
                        ops[i] = split[i].charAt(0);
                    }
                    // part2
                    offsets = new int[ops.length];
                    char[] lineArray = line.toCharArray();
                    int p = 0;
                    for (int i = 0; i < lineArray.length; i++) {
                        if (lineArray[i] != ' ') {
                            offsets[p++] = i;
                        }
                    }
                } else {
                    rows.add(Arrays.asList(split).stream().mapToInt(Integer::valueOf).toArray());
                }
            }
            
            data = new int[rows.size()][];
            for (int i = 0; i < data.length; i++) {
                data[i] = rows.remove();
            }
            
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static void getData2(String path) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            
            LinkedList<String> rows = new LinkedList<>();
            
            String line;
            
            data2 = new ArrayList[ops.length];
            for (int i = 0; i < ops.length; i++) {
                data2[i] = new ArrayList<>();
            }
            
            while ((line = br.readLine()) != null) {
                char first = line.charAt(0);
                if (first == '+' || first == '*') {
                    // noop
                } else {
                    for (int i = 0; i < offsets.length; i++) {
                        ArrayList<Integer> al = data2[i];
                        int last;
                        if (i == offsets.length - 1) {
                            last = line.length();
                        } else {
                            last = offsets[i + 1] - 1;
                        }
                        char[] number = line.substring(offsets[i], last).toCharArray();
                        for (int j = 0; j < number.length; j++) {
                            if (al.size() > j) {
                                if (number[j] == ' ') {
                                    continue;
                                }
                                int prev = al.get(j);
                                prev *= 10;
                                al.set(j, prev + (number[j] - '0'));
                            } else {
                                if (number[j] == ' ') {
                                    al.add(0);
                                } else {
                                    al.add(number[j] - '0');
                                }
                            }
                        }
                    }
                }
            }
            
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        getData("day/6/input");

        long totalP1 = 0;
        for (int col = 0; col < ops.length; col++) {
            char op = ops[col];
            long acc = 0;
            if (op == '+') {
                acc = 0;
                for (int row = 0; row < data.length; row++) {
                    acc += data[row][col];
                }
            } else if (op == '*') {
                acc = 1;
                for (int row = 0; row < data.length; row++) {
                    acc *= data[row][col];
                }
            }
            totalP1 += acc;
        }
        
        System.out.println("totalP1 = " + totalP1);
        
        getData2("day/6/input");

        long totalP2 = 0;
        
        for (int col = 0; col < ops.length; col++) {
            char op = ops[col];
            ArrayList<Integer> operands = data2[col];
            long acc = 0;
            if (op == '+') {
                acc = 0;
                for (int i : operands) {
                    acc += i;
                }
            } else if (op == '*') {
                acc = 1;
                for (int i : operands) {
                    acc *= i;
                }
            }
            totalP2 += acc;
        }
        System.out.println("totalP2 = " + totalP2);
    }
    
}
