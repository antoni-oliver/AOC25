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
            
            width = maxX - minX + 1;
            height = maxY - minY + 1;
            
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
        getData("day/9/test");
        
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
        
        record Range(Position start, Position end) {};
        
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
                    LinkedList<Range> toCheck = new LinkedList<>();
                    int top = Math.min(a.y, b.y);
                    int right = Math.max(a.x, b.y);
                    int bottom = Math.max(a.y, b.y);
                    int left = Math.min(a.x, b.x);
                    /*
                    A-B
                    | |
                    D-C
                    */
                    Position A = new Position(left, top);
                    Position B = new Position(right, top);
                    Position C = new Position(right, bottom);
                    Position D = new Position(left, bottom);
                    toCheck.add(new Range(A, B));
                    toCheck.add(new Range(B, C));
                    toCheck.add(new Range(C, D));
                    toCheck.add(new Range(D, A));
                    while (!toCheck.isEmpty()) {
                        Range r = toCheck.remove();
                        System.out.println(r);
                        for (int k = 0; k < array.length - 1; k++) {
                            Range segment = new Range(array[k], array[k + 1]);
                            if (segment.start.x == segment.end.x) {
                                int x = segment.start.x;
                                // vertical
                                if (r.start.x == x && r.start.x == x) {
                                    // also vertical, same col
                                    int rTop, rBottom, sTop, sBottom;
                                    if (r.start.y < r.end.y) {
                                        rTop = r.start.y;
                                        rBottom = r.end.y;
                                    } else {
                                        rTop = r.end.y;
                                        rBottom = r.end.y;
                                    }
                                    if (segment.start.y < segment.end.y) {
                                        sTop = segment.start.y;
                                        sBottom = segment.end.y;
                                    } else {
                                        sTop = segment.end.y;
                                        sBottom = segment.end.y;
                                    }
                                    if (sTop <= rTop && sBottom >= rBottom) {
                                        // map segment includes range to check
                                        // done
                                    } else if (sTop <= rTop) {
                                        // map segment includes the start of the range
                                        // re-add the end of the range
                                        toCheck.add(new Range(new Position(x, sBottom + 1), new Position(x, rBottom)));
                                    } else if (sBottom >= rBottom) {
                                        // map segment includes the end of the range
                                        // re-add the start of the range
                                        toCheck.add(new Range(new Position(x, rTop), new Position(x, sTop - 1)));
                                    } else {
                                        // map segment include sthe middle of the range
                                        // add the start and the end of the range
                                        toCheck.add(new Range(new Position(x, rTop), new Position(x, sTop - 1)));
                                        toCheck.add(new Range(new Position(x, sBottom + 1), new Position(x, rBottom)));
                                    }
                                }
                            } else if (segment.start.y == segment.end.y) {
                                int y = segment.start.y;
                                // horizontal
                                if (r.start.y == y && r.start.y == y) {
                                    // also horizontal, same row
                                    int rLeft, rRight, sLeft, sRight;
                                    if (r.start.x < r.end.x) {
                                        rLeft = r.start.x;
                                        rRight = r.end.x;
                                    } else {
                                        rLeft = r.end.x;
                                        rRight = r.end.x;
                                    }
                                    if (segment.start.x < segment.end.x) {
                                        sLeft = segment.start.x;
                                        sRight = segment.end.x;
                                    } else {
                                        sLeft = segment.end.x;
                                        sRight = segment.end.x;
                                    }
                                    if (sLeft <= rLeft && sRight >= rRight) {
                                        // map segment includes range to check
                                        // done
                                    } else if (sLeft <= rLeft) {
                                        // map segment includes the start of the range
                                        // re-add the end of the range
                                        toCheck.add(new Range(new Position(sRight + 1, y), new Position(rRight, y)));
                                    } else if (sRight >= rRight) {
                                        // map segment includes the end of the range
                                        // re-add the start of the range
                                        toCheck.add(new Range(new Position(rLeft, y), new Position(sLeft - 1, y)));
                                    } else {
                                        // map segment include sthe middle of the range
                                        // add the start and the end of the range
                                        toCheck.add(new Range(new Position(rLeft, y), new Position(sLeft - 1, y)));
                                        toCheck.add(new Range(new Position(sRight + 1, y), new Position(rRight, y)));
                                    }
                                }
                            } else {
                                System.err.println("Should not happen, right?");
                            }
                        }
                    }
                }
            }
        }
        System.out.println("largestP2: " + largestP2);
        
        // part 1
//        HashSet<Position> red = new HashSet<>(positions.size());
//        for (Position p : positions) {
//            red.add(new Position(p.x, p.y));
//        }
//        boolean[][] red = new boolean[height][width];
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                red[y][x] = false;
//            }
//        }
//        
//        for (Position p : positions) {
//            red[p.y - minY][p.x - minX] = true;
//        }
        
        // BFS
//        long largest = 0;
//        record Offset(int top, int right, int bottom, int left) {};
//        LinkedList<Offset> queue = new LinkedList<>();
//        queue.add(new Offset(0, 0, 0, 0));
//        while (largest == 0 && !queue.isEmpty()) {
//            Offset offset = queue.remove();
//            // a1
//            //   \
//            //    a2
//            Position a1 = new Position(offset.left, offset.top);
//            System.out.println(a1);
//            Position a2 = new Position(width - offset.right - 1, height - offset.bottom - 1);
//            
//            //if (red[a1.y][a1.x] && red[a2.y][a2.x]) {
//            if (red.contains(a1) && red.contains(a2)) {
//                int largestWidth = a2.x - a1.x + 1;
//                int largestHeight = a2.y - a1.y + 1;
//                largest = largestWidth * largestHeight;
//            }
//            
//            //    b1
//            //   /
//            // b2
//            Position b1 = new Position(width - offset.right - 1, offset.top);
//            Position b2 = new Position(offset.left, height - offset.bottom - 1);
//            
//            //if (red[b1.y][b1.x] && red[b2.y][b2.x]) {
//            if (red.contains(b1) && red.contains(b2)) {
//                int largestWidth = b1.x - b2.x + 1;
//                int largestHeight = b2.y - b1.y + 1;
//                largest = largestWidth * largestHeight;
//            }
//            
//            queue.add(new Offset(offset.top + 1, offset.right, offset.bottom, offset.left));
//            queue.add(new Offset(offset.top, offset.right + 1, offset.bottom, offset.left));
//            queue.add(new Offset(offset.top, offset.right, offset.bottom + 1, offset.left));
//            queue.add(new Offset(offset.top, offset.right, offset.bottom, offset.left + 1));
//        }
//        
//        System.out.println("largest = " + largest);
    }
    
}
