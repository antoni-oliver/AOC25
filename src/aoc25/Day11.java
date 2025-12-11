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
        int i = 0;
        stack.push(you);
        while (!stack.isEmpty()) {
            System.out.println(++i);
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

    record NodeWithPast(Node node, LinkedList<Node> past, HashSet<Node> visited) {

        public NodeWithPast(Node node) {
            this(node, new LinkedList<>(), new HashSet<>());
        }
    }

    ;
    
    void part2() {
        // PART 2
        // DFS, stacking not only the node but also the past nodes
        LinkedList<NodeWithPast> stack = new LinkedList<>();
        HashMap<Node,Integer> exitsFromHere = new HashMap<>();

        int exitsP2 = 0;
        int i = 0;
        NodeWithPast firstNode = new NodeWithPast(svr);
        stack.push(firstNode);
        while (!stack.isEmpty()) {
            //System.out.println(++i);
            NodeWithPast nwp = stack.pop();
            Node cur = nwp.node;
            //System.out.println(cur);
            LinkedList<Node> past = nwp.past;
            HashSet<Node> visited = nwp.visited;
            if (cur == out && visited.contains(fft) && visited.contains(dac)) {
                // we undo the past and check for fft and dac
                boolean foundFFT = false;
                boolean foundDAC = false;
                Node n = null;
                while (!past.isEmpty() && !(foundFFT && foundDAC)) {
                    n = past.pop();
                    if (n == fft) foundFFT = true;
                    if (n == dac) foundDAC = true;
                }
                if (foundFFT && foundDAC) {
                    // mark from either fft or dac (whichever was found first)
                    // [this is n] upwards, with +1 exit
                    while (n != null) {
                        int exits = 0;
                        if (exitsFromHere.containsKey(n)) {
                            exits = exitsFromHere.get(n);
                        }
                        exits++;
                        exitsFromHere.put(n, exits);
                        if (!past.isEmpty()) {
                            n = past.pop();
                        } else {
                            n = null;
                        }
                    }
                    exitsP2++;
                } // si no, posar amb zero sortides? -- assegurar que avall no se lia
            }

            LinkedList<Node> newPast = new LinkedList<>(past);
            HashSet<Node> newVisited = new HashSet<>(visited);
            newPast.push(cur);
            newVisited.add(cur);
            for (Node next : cur.connections) {
                if (exitsFromHere.containsKey(next)) {
                    System.out.println("case b");
                    // next already has X exits -> add X to the exits of the newPast
                    // (including cur, but NOT next)
                    // this means it is the first in a fft-dac chain or an antecessor
                    int alreadyFoundExits = exitsFromHere.get(next);
                    newPast.forEach((n) -> {
                        int exits = 0;
                        if (exitsFromHere.containsKey(n)) {
                            exits = exitsFromHere.get(n);
                        }
                        exits++;
                        exitsFromHere.put(n, exits);
                    });
                    exitsP2 += alreadyFoundExits;
                } else {
                    stack.push(new NodeWithPast(next, newPast, newVisited));
                }
            }
        }
        
        System.out.println("x" + exitsFromHere.get(svr));

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
