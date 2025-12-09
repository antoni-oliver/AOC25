/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aoc25;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 *
 * @author Antoni
 */
public class Day8 {

    record Position(int x, int y, int z) {
        public static Position fromString(String s) {
            String[] split = s.split(",");
            
            return new Position(Integer.parseInt(split[0]),
                                Integer.parseInt(split[1]),
                                Integer.parseInt(split[2]));
        }
    };
    
    static double euclideanSquared(Position p1, Position p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2) + Math.pow(p2.z - p1.z, 2));
    }
    
    record Pair(Position a, Position b, double d2) {
        public Pair(Position a, Position b) {
            this(a, b, euclideanSquared(a, b));
        }
    };
    
    static PriorityQueue<Pair> pq = new PriorityQueue<>((o1, o2) -> Double.compare(o1.d2, o2.d2));
    
    static ArrayList<Position> data;
    
    static class Circuit {
        LinkedList<Position> data;
        public Circuit() {
            data = new LinkedList<>();
        }
        public void add(Position p) {
            data.add(p);
        }
        public int size() {
            return data.size();
        }
        public Position poll() {
            return data.poll();
        }
    };
    
    static LinkedList<Circuit> circuits = new LinkedList<>();
    static HashMap<Position, Circuit> positionCircuit = new HashMap<>(1000);
    
    static void getData(String path) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            
            data = new ArrayList<>();
            
            String line = br.readLine();
            
            while (line != null) {
                data.add(Position.fromString(line));
                line = br.readLine();
            }
            
            br.close();
            fr.close();
        } catch (Exception e) {
            
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        getData("day/8/input");
        
        // System.out.println(data);
        
        for (int i = 0; i < data.size(); i++) {
            Position pi = data.get(i);
            Circuit c = new Circuit();
            c.add(pi);
            circuits.add(c);
            positionCircuit.put(pi, c);
            for (int j = i + 1; j < data.size(); j++) {
                pq.add(new Pair(pi, data.get(j)));
            }
        }
        
        // System.out.println(pq);
        
        Pair lastPair = null;
        
        int n = 0;
        long p10 = 0;
        long p1000 = 0;
        while (!pq.isEmpty() && circuits.size() > 1) {
            //System.out.println("PQ: " + pq.size() + ", C: " + circuits.size());
            Pair pair = pq.poll();
            lastPair = pair;
            Circuit ca = positionCircuit.get(pair.a);
            Circuit cb = positionCircuit.get(pair.b);
            if (ca == cb) {
                // already same circuit, do nothing
            } else {
                // make new circuit
                if (ca.size() <= cb.size()) {
                    Position pos = cb.poll();
                    while (pos != null) {
                        positionCircuit.put(pos, ca);
                        ca.add(pos);
                        pos = cb.poll();
                    }
                    circuits.remove(cb);
                } else {
                    Position pos = ca.poll();
                    while (pos != null) {
                        positionCircuit.put(pos, cb);
                        cb.add(pos);
                        pos = ca.poll();
                    }
                    circuits.remove(ca);
                }
            }
            
            n++;
            // PART 1
            if (n == 10 || n == 1000) {
                int[] largest = {0, 0, 0};

                for (Circuit c : circuits) {
                    int size = c.size();
                    if (size > largest[0]) {
                        largest[2] = largest[1];
                        largest[1] = largest[0];
                        largest[0] = size;
                    } else if (size > largest[1]) {
                        largest[2] = largest[1];
                        largest[1] = size;
                    } else if (size > largest[2]) {
                        largest[2] = size;
                    }
                }

                long p = 1;
                for (int i = 0; i < largest.length; i++) {
                    p *= largest[i];
                }

                if (n == 10) p10 = p;
                else p1000 = p;
            }
        }
        
        // PART 1
        System.out.println("part 1, n = 10: " + p10);
        System.out.println("part 1, n = 1000: " + p1000);

        
        // PART 2
        
        System.out.println((long)lastPair.a.x * (long)lastPair.b.x);
    }
    
}
