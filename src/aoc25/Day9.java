/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aoc25;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Antoni
 */
public class Day9 {

    record Position(int x, int y) {};
    record Segment(Position start, Position end) {
        boolean isHorizontal() {
            return start.y == end.y;
        }
        Segment normalize() {
            if (start.x > end.x || start.y > end.y) {
                return new Segment(end, start);
            } else {
                return this;
            }
        }
    };
    
    static LinkedList<Position> positions;
    static int minX = -1;
    static int minY = -1;
    static int maxX = -1;
    static int maxY = -1;
    static int width = -1;
    static int height = -1;
    
    static void getData(String path) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            
            positions = new LinkedList<>();
            
            String line = br.readLine();
            while (line != null) {   
                String[] split = line.split(",");
                int x = Integer.parseInt(split[0]);
                int y = Integer.parseInt(split[1]);
                if (minX == -1 || x < minX) minX = x;
                if (minY == -1 || y < minY) minY = y;
                if (maxX == -1 || x > maxX) maxX = x;
                if (maxY == -1 || y > maxY) maxY = y;
                Position p = new Position(x, y);
                positions.add(p);
                line = br.readLine();
            }
            
            positions.add(positions.getFirst()); // closed
            
            width = maxX - minX + 1;
            height = maxY - minY + 1;
            
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static boolean intersectsPolygon(Segment[] polygon, int top, int right, int bottom, int left) {
        for (int i = 0; i < polygon.length; i++) {
            Segment s = polygon[i];
            if (s.isHorizontal()) {
                /*
                #-----#
                |     |
               xxxxxxxxx
                |     |
                #-----#
                */
                // s must be between TOP and BOTTOM (not included) and must cross LEFT, RIGHT or be inside.
                int y = s.start.y;
                if (y > top && y < bottom) {
                    if (s.start.x < left && s.end.x > left) {
                        // crosses LEFT
                        return true;
                    }
                    if (s.start.x < right && s.end.x > right) {
                        // crosses RIGHT
                        return true;
                    }
                    if (s.start.x >= left && s.end.x <= right) {
                        return true;
                    }
                }
            } else {
                /*
                   x
                #--x--#
                |  x  |
                |  x  |
                |  x  |
                #--x--#
                   x
                */
                // s must be between LEFT and RIGHT (not included) and must cross TOP, BOTTOM or be inside.
                int x = s.start.x;
                if (x > left && x < right) {
                    if (s.start.y < top && s.end.y > top) {
                        // crosses TOP
                        return true;
                    }
                    if (s.start.y < bottom && s.end.y > bottom) {
                        // crosses BOTTOM
                        return true;
                    }
                    if (s.start.y >= top && s.end.y <= bottom) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    static boolean pointInsidePolygon(Segment[] polygon, double px, double py) {
        int count = 0;
        for (int i = 0; i < polygon.length; i++) {
            Segment s = polygon[i];
            if (!s.isHorizontal()) {
                // Should suffice counting crosses left->right through vertical edges
                /*
                x
                no
                    #
                    |
                x   |   x
               yes  |   no
                    #
                x
                no
                */
                if (py > s.start.y && py < s.end.y) {
                    if (px < s.start.x) {
                        count++;
                    }
                }
            }
        }
        return count % 2 == 1;
    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        getData("day/9/input");
        
        // PART 1
        Position[] array = positions.toArray(Position[]::new);
        long largestP1 = 0;
        for (int i = 0; i < array.length - 1; i++) {
            Position a = array[i];
            for (int j = i + 1; j < array.length; j++) {
                Position b = array[j];
                long width = Math.abs(a.x - b.x) + 1;
                long height = Math.abs(a.y - b.y) + 1;
                long area = width * height;
                if (area > largestP1) {
                    largestP1 = area;
                }
            }
        }
        System.out.println("largestP1: " + largestP1);
        
        // PART 2
        Segment[] polygon = new Segment[array.length - 1];
        for (int i = 0; i < polygon.length; i++) {
            Segment s = new Segment(array[i], array[i + 1]).normalize();
            polygon[i] = s;
        }
        long largestP2 = 0;
        for (int i = 0; i < array.length - 1; i++) {
            Position a = array[i];
            for (int j = i + 1; j < array.length; j++) {
                Position b = array[j];
                long width = Math.abs(a.x - b.x) + 1;
                long height = Math.abs(a.y - b.y) + 1;
                long area = width * height;
                if (area > largestP2) {
                    System.out.println("area: " + area);
                    // it would be larger, check if it's okay though
                    int top = Math.min(a.y, b.y);
                    int right = Math.max(a.x, b.x);
                    int bottom = Math.max(a.y, b.y);
                    int left = Math.min(a.x, b.x);

                    if (intersectsPolygon(polygon, top, right, bottom, left)) {
                        continue;
                    }
                    
                    // Got it
                    largestP2 = area;
                    System.out.println("-");
                }
            }
        }
        System.out.println("largestP2: " + largestP2);
    }
    
}
