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
            
            width = maxX - minX + 1;
            height = maxY - minY + 1;
            
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static boolean intersection(Position[] polygon, Segment side) {
        boolean sideHorizontal = side.isHorizontal();
        side = side.normalize();
        for (int i = 0; i < polygon.length; i++) {
            Segment segment = new Segment(polygon[i], polygon[i+1 == polygon.length ? 0 : i+1]).normalize();
            boolean segmentHorizontal = segment.isHorizontal();
            if (sideHorizontal && segmentHorizontal && side.start.y == segment.start.y) {
//                if (side.start.x < segment.start.x && side.end.x >= side.start.x) {
//                    return true;
//                }
//                if (side.end.x > segment.end.x && side.start.x <= side.end.x) {
//                    return true;
//                }
            } else if (!sideHorizontal && !segmentHorizontal && side.start.x == segment.start.x) {
//                if (side.start.y < segment.start.y && side.end.y >= side.start.y) {
//                    return true;
//                }
//                if (side.end.y > segment.end.y && side.start.y <= side.end.y) {
//                    return true;
//                }
            } else {
                if (sideHorizontal) {
                    // side is horizontal, segment is vertical
                    if (side.start.x < segment.start.x && side.end.x > segment.start.x && side.start.y > segment.start.y && side.start.y < segment.end.y) {
                        return true;
                    }
                } else {
                    if (side.start.y < segment.start.y && side.end.y > segment.start.y && side.start.x > segment.start.x && side.start.x < segment.end.x) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    static boolean inOrEdge(Position p, int top, int right, int bottom, int left) {
        return p.x >= left && p.x <= right && p.y >= top && p.y <= bottom;
    }
    
    static boolean inStrict(Position p, int top, int right, int bottom, int left) {
        return p.x > left && p.x < right && p.y > top && p.y < bottom;
    }
    
    static boolean out(Position p, int top, int right, int bottom, int left) {
        return !inOrEdge(p, top, right, bottom, left);
    }
    
    static boolean inSegment(Position p, Segment s) {
        if (s.start.x == s.end.x) {
            // vertical
            if (p.x != s.start.x) {
                return false;
            }
            if (s.start.y < s.end.y) {
                // down
                return s.start.y <= p.y && s.end.y >= p.y;
            } else {
                return s.end.y <= p.y && s.start.y >= p.y;
            }
        } else {
            // horizontal
            if (p.y != s.start.y) {
                return false;
            }
            if (s.start.x < s.end.x) {
                // down
                return s.start.x <= p.x && s.end.x >= p.x;
            } else {
                return s.end.x <= p.x && s.start.x >= p.x;
            }
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
                    /*
                    A-B
                    | |
                    D-C
                    */
                    Position A = new Position(left, top);
                    Position B = new Position(right, top);
                    Position C = new Position(right, bottom);
                    Position D = new Position(left, bottom);
                    Segment TOP = new Segment(A, B);
                    Segment RIGHT = new Segment(B, C);
                    Segment BOTTOM = new Segment(C, D);
                    Segment LEFT = new Segment(D, A);
                    if (intersection(array, new Segment(A, B))
                            || intersection(array, new Segment(B, C))
                            || intersection(array, new Segment(C, D))
                            || intersection(array, new Segment(D, A))) {
                        continue;
                    } else {
                        boolean found = false;
                        for (int k = 0; k < array.length && !found; k++) {
                            if (inStrict(array[k], top, right, bottom, left)) {
                                found = true;
                            }
                            int k2 = k == array.length - 2 ? 0 : array.length - 1;
                            Position p1 = array[k];
                            Position p2 = array[k2];
                            if (inOrEdge(p1, top, right, bottom, left) && !inStrict(p1, top, right, bottom, left)
                                    && inOrEdge(p2, top, right, bottom, left) && !inStrict(p2, top, right, bottom, left)) {
                                // if they're both in edge, they must be in the same side (maybe a corner)
                                if (inSegment(p1, TOP) && !inSegment(p2, TOP)
                                        || inSegment(p1, RIGHT) && !inSegment(p2, RIGHT)
                                        || inSegment(p1, BOTTOM) && !inSegment(p2, BOTTOM)
                                        || inSegment(p1, LEFT) && !inSegment(p2, LEFT)) {
                                    found = true;
                                }
                            }
                        }
                        if (found) {
                            continue;
                        }
                    }
                    // Got it
                    largestP2 = area;
                    System.out.println("-");
//                    boolean found = false;
//                    for (int k = 0; k < array.length && !found; k++) {
//                        int k2 = k + 1;
//                        if (k2 == array.length) k2 = 0;
//                        Segment s = new Segment(array[k], array[k2]);
//                        boolean startInStrict = inStrict(array[k], top, right, bottom, left);
//                        boolean endInStrict = inStrict(array[k2], top, right, bottom, left);
//                        boolean startInOrEdge = inOrEdge(array[k], top, right, bottom, left);
//                        boolean endInOrEdge = inOrEdge(array[k2], top, right, bottom, left);
//                        boolean startOut = out(array[k], top, right, bottom, left);
//                        boolean endOut = out(array[k2], top, right, bottom, left);
//                        if (startInStrict || endInStrict
//                                || (s.isHorizontal() && (s.start.x < s.end.x && inSegment(s.start, LEFT) && inSegment(s.end, RIGHT) && !s.start.equals(A) && !s.start.equals(D) && !s.end.equals(B) && !s.end.equals(C))
//                                                                             || inSegment(s.end, LEFT) && inSegment(s.start, RIGHT) && !s.start.equals(B) && !s.start.equals(C) && !s.end.equals(A) && !s.end.equals(D))
//                                || (s.start.y < s.end.y && inSegment(s.start, TOP) && inSegment(s.end, BOTTOM) && !s.start.equals(A) && !s.start.equals(B) && !s.end.equals(C) && !s.end.equals(D))
//                                                        || inSegment(s.end, TOP) && inSegment(s.start, BOTTOM) && !s.start.equals(C) && !s.start.equals(D) && !s.end.equals(A) && !s.end.equals(B)) {
//                            found = true;
//                        }
//                    }
//                    if (!found) {
//                        largestP2 = area;
//                        System.out.println("check");
//                    }
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
