package aoc25;

import java.io.*;
import java.util.LinkedList;


/**
 *
 * @author Antoni
 */
public class Day4 {
    final static int[][] DIRS = {
        {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}
    };
    
    static char[][] getData(String path) throws Exception {
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        
        String line = br.readLine();
        LinkedList<char[]> rows = new LinkedList<>();
        
        while (line != null) {
            rows.add(line.toCharArray());
            line = br.readLine();
        }
        
        br.close();
        fr.close();
        
        char[][] data = new char[rows.size()][];
        int i = 0;
        for (char[] r : rows) {
            data[i++] = r;
        }

        return data;
    }
    
    static void show(char[][] map) {
        System.out.println();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
    
    static int remove(char[][] map) {
        int removed = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == '.') {
                    continue;
                }
                int totalRollsAdjacent = 0;
                for (int[] dir : DIRS) {
                    int row = i + dir[0];
                    int col = j + dir[1];
                    if (row >= 0 && row < map.length && col >= 0 && col < map[i].length) {
                        if (map[row][col] == '@' || map[row][col] == 'X') {
                            totalRollsAdjacent++;
                        }
                    }
                }
                if (totalRollsAdjacent < 4) {
                    removed++;
                    map[i][j] = 'X';
                }
            }
        }
        // effectively remove them
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 'X') {
                    map[i][j] = '.';
                }
            }
        }
        return removed;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        char[][] map = getData("day/4/input");
        
        int removed = 0;
        
        show(map);
        
        removed += remove(map);
       
        show(map);
        
        System.out.println();
        System.out.println("removed 1st round = " + removed + " (first part)");
        System.out.println();
        
        int removedThisRound;
        
        removedThisRound = remove(map);
        while (removedThisRound != 0) {
            System.out.println("removedThisRound = " + removedThisRound);
            removed += removedThisRound;
            
            removedThisRound = remove(map);
        }
        
        System.out.println("removed in total = " + removed);
    }
    
}
