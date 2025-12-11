/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aoc25;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author Antoni
 */
public class Day11 {

    record Node(String id, LinkedList<Node> connections) {

        public Node(String id) {
            this(id, new LinkedList<>());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node node = (Node) o;
            return (id == null ? node.id == null : id.equals(node.id));
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
    ;
    
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
            HashMap<String, Node> hash = new HashMap<>();

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
            dac = hash.get("dac");
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

    record NodePlus(Node node, boolean dacFound, boolean fftFound) {

        public NodePlus(Node node) {
            this(node, false, false);
        }
        
        @Override
        public boolean equals(Object other) {
            NodePlus np = (NodePlus) other;
            return node.equals(np.node) && dacFound == np.dacFound && fftFound == np.fftFound;
        }
    };
    
    HashMap<NodePlus,Long> exits;
    
    long dfs2(NodePlus np) {
        Node node = np.node;
        boolean dacFound = np.dacFound;
        boolean fftFound = np.fftFound;
        if (node == out) {
            if (dacFound && fftFound) {
                return 1;
            } else {
                return 0;
            }
        }
        if (exits.containsKey(np)) {
            return exits.get(np);
        }
        if (node == dac) {
            dacFound = true;
        } else if (node == fft) {
            fftFound = true;
        }
        long exitsFromChildren = 0;
        for (Node next : node.connections) {
            exitsFromChildren += dfs2(new NodePlus(next, dacFound, fftFound));
        }
        exits.put(np, exitsFromChildren);
        return exitsFromChildren;
    }
    
    void part2() {
        // PART 2
        // for each node, know if we have visited both dac and fft.
        // after getting the exits from their children, we can cache it.
        // it is not the same if having visited dac or fft are not the same.
        exits = new HashMap<>();
        long exitsP2 = dfs2(new NodePlus(svr));

        System.out.println("ExitsP2 = " + exitsP2);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new Day11("day/11/input").part1();
        new Day11("day/11/input").part2();
    }

}
