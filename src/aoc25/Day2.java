package aoc25;

/**
 *
 * @author Antoni
 */
public class Day2 {
    
    public static void main(String[] args) throws Exception {
        TextReader reader = new TextReader("day/2/input");
        
        Text id1, id2;
        long sumHalvesRepeated = 0;
        long sumPartsRepeated = 0;
        
        id1 = reader.readText('-');
        id2 = reader.readText(',');
        
        while (!id1.isEmpty()) {
            long num1, num2;
            num1 = id1.toLong();
            num2 = id2.toLong();
            
            System.out.println(num1 + "-" + num2);
            
            for (long i = num1; i <= num2; i++) {
                Text num = new Text(String.valueOf(i));
                if (num.isTwoHalves()) {
                    System.out.println("-> " + i);
                    sumHalvesRepeated += i;
                }
                if (num.isPartsRepeated()) {
                    System.out.println("parts->" + i);
                    sumPartsRepeated += i;
                }
            }
            
            id1 = reader.readText('-');
            id2 = reader.readText(',');
        }
        
        System.out.println("sumHalvesRepeated = " + sumHalvesRepeated);
        System.out.println("sumPartsRepeated = " + sumPartsRepeated);
                
        reader.tanca();
    }
}
