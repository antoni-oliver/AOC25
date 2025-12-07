/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aoc25;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Antoni
 */
public class Day7 {

    static char[][] data;
    
    static void getData(String path) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            
            List<String> lines = br.readAllLines();
                        
            data = new char[lines.size()][];
            
            int i = 0;
            for (String line : lines) {
                data[i] = line.toCharArray();
                i++;
            }
            
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static void show() {
        System.out.println();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                System.out.print(data[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
    
    record Position(int row, int col) {};
    
    static LinkedList<Position> activeBeams = new LinkedList<>();
    static int splits = 0;
    static int timelines = 0;
    
    static void findStart() {
        int row = 0; // fixed
        for (int col = 0; col < data[row].length; col++) {
            if (data[row][col] == 'S') {
                Position p = new Position(row + 1, col);
                activeBeams.add(p);
            }
        }
    }
    
    static void step() {
        while (!activeBeams.isEmpty()) {
            Position p = activeBeams.remove();
            System.out.println(p);
            if (data[p.row][p.col] == '.') {
                data[p.row][p.col] = '|';
                Position newP = new Position(p.row + 1, p.col);
                if (newP.row < data.length) {
                    activeBeams.add(newP);
                }
            } else if (data[p.row][p.col] == '^') {
                Position left = new Position(p.row, p.col - 1);
                Position right = new Position(p.row, p.col + 1);
                if (left.col >= 0) {
                    activeBeams.add(left);
                }
                if (right.col < data[p.row].length) {
                    activeBeams.add(right);
                }
                splits++;
            }
        }
    }
    
    static void step2() {
        while (!activeBeams.isEmpty()) {
            Position p = activeBeams.remove();
            System.out.println(p);
            if (data[p.row][p.col] == '.' || data[p.row][p.col] == '|') {
                data[p.row][p.col] = '|';
                Position newP = new Position(p.row + 1, p.col);
                if (newP.row < data.length) {
                    activeBeams.add(newP);
                } else {
                    timelines++;
                }
            } else if (data[p.row][p.col] == '^') {
                Position left = new Position(p.row, p.col - 1);
                Position right = new Position(p.row, p.col + 1);
                if (left.col >= 0) {
                    activeBeams.add(left);
                }
                if (right.col < data[p.row].length) {
                    activeBeams.add(right);
                }
            }
        }
    }
        
    static long dfs(char[][] data, Position currentPosition, long[][] exits, int timelines) {
        long newTimelines = 0;
        System.out.println(currentPosition);
        if (data[currentPosition.row][currentPosition.col] == '.') {
            data[currentPosition.row][currentPosition.col] = '|';
            if (currentPosition.row < data.length - 1) {
                Position newPosition = new Position(currentPosition.row + 1, currentPosition.col);
                newTimelines += dfs(data, newPosition, exits, timelines);
            } else {
                newTimelines++;
            }
        } else if (data[currentPosition.row][currentPosition.col] == '^') {
            // System.out.println("split");
            Position left = new Position(currentPosition.row, currentPosition.col - 1);
            Position right = new Position(currentPosition.row, currentPosition.col + 1);
            if (left.col >= 0) {
                newTimelines += dfs(data, left, exits, timelines);
            }
            if (newTimelines < 0) {
                System.out.println("asd");
            }
            if (right.col < data[currentPosition.row].length) {
                newTimelines += dfs(data, right, exits, timelines);
            }
            if (newTimelines < 0) {
                System.out.println(currentPosition);
                System.out.println("esd");
            }
        } else if (data[currentPosition.row][currentPosition.col] == '|') {
            if (exits[currentPosition.row][currentPosition.col] == 0) {
                System.out.println("???");
            }
            return exits[currentPosition.row][currentPosition.col];
        }
        
        exits[currentPosition.row][currentPosition.col] = newTimelines;
        
        return newTimelines;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        getData("day/7/input");
        
        // show();
        
        findStart();
        
        while (!activeBeams.isEmpty()) {
            step();
        }
        
        System.out.println("splits part 1 = " + splits);
        
        // agh, DFS ... I overengineered for nothing :')
        
        getData("day/7/input");
                
        findStart();

        Position currentPosition = activeBeams.remove();
        
        long[][] exits = new long[data.length][];
        for (int i = 0; i < data.length; i++) {
            exits[i] = new long[data[i].length];
            for (int j = 0; j < data[i].length; j++) {
                exits[i][j] = 0;
            }
        }
        
        long timelines = dfs(data, currentPosition, exits, 0);
        
        System.out.println("Timelines = " + timelines);
    }
    
}
