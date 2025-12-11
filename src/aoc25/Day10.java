/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aoc25;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Antoni
 */
public class Day10 {

    record Machine(boolean[] lights, int[] joltage, boolean[] goal, int[][] buttons, int[] joltageGoal, int steps) {
        public Machine(boolean[] goal, int[][] buttons, int[] joltageGoal) {
            this(new boolean[goal.length], new int[joltageGoal.length], goal, buttons, joltageGoal, 0);
        }
        public String toString() {
            String s = "";
            /*s += ("Lights = ");
            for (int i = 0; i < lights.length; i++) {
                s += (lights[i]);
                s += (",");
            }
            s += ("Goal = ");
            for (int i = 0; i < goal.length; i++) {
                s += (goal[i]);
                s += (",");
            }*/
            /*s += ("Buttons = ");
            for (int i = 0; i < buttons.length; i++) {
                for (int j = 0; j < buttons[i].length; j++) {
                    s += (buttons[i][j]);
                    s += (",");
                }
                s += " ";
            }*/
            s += ("Joltage = ");
            for (int i = 0; i < joltage.length; i++) {
                s += (joltage[i]);
                s += (",");
            }
            s += ("JoltageGoal = ");
            for (int i = 0; i < joltageGoal.length; i++) {
                s += (joltageGoal[i]);
                s += (",");
            }
            s += "\n";
            return s;
        }
        public Machine useButtonPart1(int buttonIndex) {
            int[] button = buttons[buttonIndex];
            boolean[] lights2 = new boolean[lights.length];
            // Copy
            for (int i = 0; i < lights.length; i++) {
                lights2[i] = lights[i];
            }
            // Change according to button
            for (int i = 0; i < button.length; i++) {
                int lightIndex = button[i];
                lights2[lightIndex] = !lights2[lightIndex];
            }
            return new Machine(lights2, joltage, goal, buttons, joltageGoal, steps + 1);
        }
        public Machine useButtonPart2(int buttonIndex) {
            int[] button = buttons[buttonIndex];
            int[] joltage2 = new int[joltage.length];
            // Copy
            for (int i = 0; i < joltage.length; i++) {
                joltage2[i] = joltage[i];
            }
            // Change according to button
            for (int i = 0; i < button.length; i++) {
                int lightIndex = button[i];
                joltage2[lightIndex]++;
            }
            return new Machine(lights, joltage2, goal, buttons, joltageGoal, steps + 1);
        }
        public boolean isGoalPart1() {
            for (int i = 0; i < goal.length; i++) {
                if (lights[i] != goal[i]) {
                    return false;
                }
            }
            return true;
        }
        public boolean isGoalPart2() {
            for (int i = 0; i < joltageGoal.length; i++) {
                if (joltage[i] != joltageGoal[i]) {
                    return false;
                }
            }
            return true;
        }
        public boolean tooMuch() {
            // if we get too much joltage in a light or whatever,
            // we cannot go back, so prune this machine
            for (int i = 0; i < joltageGoal.length; i++) {
                if (joltage[i] > joltageGoal[i]) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (other == null) return false;
            if (!(other instanceof Machine)) return false;
            Machine mOther = (Machine) other;
            if (this.joltage.length != mOther.joltage.length) return false;
            for (int i = 0; i < joltage.length; i++) {
                if (this.joltage[i] != mOther.joltage[i]) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(joltage);
        }
    };
    
    static Machine[] machines;
    
    static void getData(String path) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            
            String line = br.readLine();
            Pattern pattern = Pattern.compile("(\\[[^\\(]+\\]) (\\([^\\{]+\\)) (\\{[^\\}]+\\})");
            
            LinkedList<Machine> machines = new LinkedList<>();
            
            while (line != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String group1 = matcher.group(1);  // [ ... ]
                    String group2 = matcher.group(2);  // ( ... )
                    String group3 = matcher.group(3);  // { ... }
                    
                    char[] a1 = group1.substring(1, group1.length() - 1).toCharArray();
                    boolean[] lights = new boolean[a1.length];
                    for (int i = 0; i < a1.length; i++) {
                        lights[i] = a1[i] == '#';
                    }
                    
                    String[] g2split = group2.split(" ");
                    int[][] buttons = new int[g2split.length][];
                    for (int i = 0; i < g2split.length; i++) {
                        String buttonWithoutParentheses = g2split[i].substring(1, g2split[i].length() - 1);
                        String[] buttonSplit = buttonWithoutParentheses.split(",");
                        int[] button = new int[buttonSplit.length];
                        for (int j = 0; j < buttonSplit.length; j++) {
                            button[j] = Integer.parseInt(buttonSplit[j]);
                        }
                        buttons[i] = button;
                    }
                    
                    String joltageWithoutBraces = group3.substring(1, group3.length() - 1);
                    String[] joltageSplit = joltageWithoutBraces.split(",");
                    int[] joltage = new int[joltageSplit.length];
                    for (int i = 0; i < joltageSplit.length; i++) {
                        joltage[i] = Integer.parseInt(joltageSplit[i]);
                    }
                    
                    Machine m = new Machine(lights, buttons, joltage);

                    machines.add(m);
                    
                } else {
                    System.out.println("?");
                }
                line = br.readLine();
            }
            
            Day10.machines = machines.toArray(Machine[]::new);
            
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
        getData("day/10/input");
        
        // PART 1 : BFS
        long totalSteps = 0;
        for (int i = 0; i < machines.length; i++) {
            LinkedList<Machine> q = new LinkedList<>();
            q.add(machines[i]);
            int steps = -1;
            while (steps == -1 && !q.isEmpty()) {
                Machine m = q.remove();
                if (m.isGoalPart1()) {
                    steps = m.steps;
                } else {
                    for (int j = 0; j < m.buttons.length; j++) {
                        q.add(m.useButtonPart1(j));
                    }
                }
            }
            System.out.println(i + ": " + steps);
            totalSteps += steps;
        }
        
        System.out.println("Part1 = " + totalSteps);
        
        getData("day/10/input");
        
        // PART 2 : PROLOG TIME
        /*
        swipl -q -g solve -t halt tmp.pl
        */
        // tmp.pl:
        /*
        :- use_module(library(simplex)).

        solve :-
          gen_state(S0),
          do_constraints(S0, S),
          objective(S,O),
          write(O).

        do_constraints -->
          constraint([v4,v5] = 3),
          constraint([v1,v5] = 5),
          constraint([v2,v3,v4] = 4),
          constraint([v0,v1,v3] = 7),
          constraint(integral(v0)),
          constraint(integral(v1)),
          constraint(integral(v2)),
          constraint(integral(v3)),
          constraint(integral(v4)),
          constraint(integral(v5)),
          minimize([v0,v1,v2,v3,v4,v5]).
        */
        long totalSteps2 = 0;
        for (int i = 0; i < machines.length; i++) {
            Machine m = machines[i];
            // 1. For each light, make a list of the indexes of the buttons that turn it
            /*
           buttons  (3) (1,3) (2) (2,3) (0,2) (0,1) |
                     0    1    2    3     4     5   |
           lights                                   | Lists     Equations ( = goal)
            0: 3                          x     x   | [4,5]     N4+N5=3
            1: 5          x                     x   | [1,5]     N1+N5=5
            2: 4               x    x     x         | [2,3,4]   N2+N3+N4=4
            3: 7     x    x         x               | [0,1,3]   N0+N1+N3=7
            */
            StringBuilder prolog = new StringBuilder();
            String[] varNames = new String[m.buttons.length];
            String[] symbolNames = new String[m.buttons.length];
            LinkedList<String>[] buttonsThatTurnLight = new LinkedList[m.joltage.length];
            for (int lightId = 0; lightId < buttonsThatTurnLight.length; lightId++) {
                buttonsThatTurnLight[lightId] = new LinkedList<>();
            }

            for (int buttonId = 0; buttonId < m.buttons.length; buttonId++) {
                String varName = "V" + buttonId;
                String symbolName = "v" + buttonId;
                varNames[buttonId] = varName;
                symbolNames[buttonId] = symbolName;
                int[] button = m.buttons[buttonId];
                for (int lightIndex = 0; lightIndex < button.length; lightIndex++) {
                    int lightId = button[lightIndex];
                    buttonsThatTurnLight[lightId].add(symbolName);
                }
            }
            
            prolog.append(":- use_module(library(simplex)).\n\n");
            prolog.append("solve :-\n");
            prolog.append("  gen_state(S0),\n");
            prolog.append("  do_constraints(S0, S),\n");
            prolog.append("  objective(S,Sum),\n");
            prolog.append("  write(Sum).\n\n");
            
            prolog.append("do_constraints -->\n");
            
            for (int lightId = 0; lightId < buttonsThatTurnLight.length; lightId++) {
                // constraint([v4,v5] = 3),
                prolog.append("  constraint([").append(String.join(",", buttonsThatTurnLight[lightId]))
                        .append("] = ").append(m.joltageGoal[lightId]).append("),\n");
            }
            for (int j = 0; j < symbolNames.length; j++) {
                prolog.append("  constraint(integral(")
                        .append(symbolNames[j])
                        .append(")),\n");
            }
            prolog.append("  minimize([").append(String.join(",", symbolNames)).append("]).");
            
            // System.out.println(prolog);
            
            try {
                FileWriter fw = new FileWriter("tmp.pl");
                fw.write(prolog.toString());
                fw.close();
                ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "swipl.exe -q -g solve -t halt tmp.pl");
                Process process = builder.start();
                process.waitFor();
                BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = output.readLine();
                File f = new File("tmp.pl");
                f.delete();
                int steps = Integer.parseInt(line);
                System.out.println(i + ": " + steps);
                totalSteps2 += steps;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("Part2 = " + totalSteps2);

    }
    
}
