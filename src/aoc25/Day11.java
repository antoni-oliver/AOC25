/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aoc25;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author Antoni
 */
public class Day11 {
    
    record Node(String id, LinkedList<Node> connections, boolean visited) {
        public Node(String id) {
            this(id, new LinkedList<>(), false);
        }
    };
    
    LinkedList<Node> nodes;
    Node you;
    Node out;
    Node svr;
    Node dac;
    Node fft;

    void getData(String path) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);

            nodes = new LinkedList<>();
            HashMap<String,Node> hash = new HashMap<>();

            String line = br.readLine();

            while (line != null) {
                String[] split = line.split(" ");
                String fromId = split[0].substring(0, 3);
                Node fromNode;
                if (hash.containsKey(fromId)) {
                    fromNode = hash.get(fromId);
                } else {
                    fromNode = new Node(fromId);
                    hash.put(fromId, fromNode);
                    nodes.add(fromNode);
                }
                for (int i = 1; i < split.length; i++) {
                    String toId = split[i];
                    Node toNode;
                    if (hash.containsKey(toId)) {
                        toNode = hash.get(toId);
                    } else {
                        toNode = new Node(toId);
                        hash.put(toId, toNode);
                        nodes.add(toNode);
                    }
                    fromNode.connections.add(toNode);
                }
                line = br.readLine();
            }
            
            you = hash.get("you");
            out = hash.get("out");
            svr = hash.get("svr");
            dac = hash.get("dft");
            fft = hash.get("fft");

            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Day11(String path) {
        getData(path);
    }
    
    void part1() {
        // PART 1
        // DFS
        LinkedList<Node> stack = new LinkedList<>();
        //HashSet<Node> visited = new HashSet<>(); // forgot to use, and it was correct, LOL
        
        int exitsP1 = 0;
        stack.push(you);
        while (!stack.isEmpty()) {
            Node cur = stack.pop();
            if (cur == out) {
                exitsP1++;
            }
            for (Node next : cur.connections) {
                //if (!visited.contains(next)) {
                    stack.push(next);
                //}
            }
        }
        
        System.out.println("ExitsP1 = " + exitsP1);
    }
    
    void part2() {
        // PART 2
        // DFS recursive with a set of the nodes visited so far?
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new Day11("day/11/input").part1();
        new Day11("day/11/test2").part1();
    }
    
}
