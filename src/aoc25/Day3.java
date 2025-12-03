package aoc25;

/**
 *
 * @author Antoni
 */
public class Day3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        TextReader reader = new TextReader("day/3/test");
        
        Text bank = reader.readText('\n');
        long sumJoltage2 = 0;
        long sumJoltage12 = 0;
        
        while (!bank.isEmpty()) {
            long thisJoltage2 = bank.getLargestJoltage2();
            long thisJoltage12 = bank.getLargestJoltageN(12);
            System.out.println(bank + ": x2 = " + thisJoltage2);
            System.out.println(bank + ": x12 = " + thisJoltage12);
            sumJoltage2 += thisJoltage2;
            sumJoltage12 += thisJoltage12;
            bank = reader.readText('\n');
        }
        
        System.out.println("sumJoltage2 = " + sumJoltage2);
        System.out.println("sumJoltage12 = " + sumJoltage12);
        
        reader.tanca();
    }
    
}
