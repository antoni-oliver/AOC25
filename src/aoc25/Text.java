package aoc25;

import java.util.Arrays;

/**
 * ~Classe Paraula, però amb espais i tot el que faci falta.
 * És a dir, MyString.
 * @author Antoni
 */
public class Text {
    private char[] lletres;
    private int longitud;
    
    public static final int MAX = 200;
    
    public Text() {
        lletres = new char[MAX];
        longitud = 0;
    }
    
    public Text(char[] array) {
        this();
        for (int i = 0; i < array.length && longitud < lletres.length; i++) {
            lletres[longitud++] = array[i];
        }
    }
    
    public Text(String string) {
        this(string.toCharArray());
    }
    
    public void afegeix(char c) {
        if (longitud < lletres.length) {
            lletres[longitud++] = c;
        }
    }
    
    private static int toInt(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        } else {
            System.err.println("Error: toInt(" + c + ")");
            return 0;
        }
    }
    
    public int toInt() {
        int n = 0;
        for (int i = 0; i < longitud; i++) {
            n *= 10;
            n += toInt(lletres[i]);
        }
        return n;
    }
    
    public long toLong() {
        // srsly
        long n = 0;
        for (int i = 0; i < longitud; i++) {
            n *= 10;
            n += toInt(lletres[i]);
        }
        return n;
    }
    
    public boolean isEmpty() {
        return longitud == 0;
    }
    
    public boolean isTwoHalves() {
        if (longitud % 2 != 0) {
            return false;
        }
        
        // Cercam diferència i, i + longitud / 2
        for (int i = 0, j = longitud / 2; j < longitud; i++, j++) {
            if (lletres[i] != lletres[j]) {
                return false;
            }
        }
        
        return true;
    }
    
    public Text subText(int inici, int mida) {
        Text nou = new Text();
        int i;
        for (i = inici; nou.longitud < mida && i < this.longitud; i++) {
            nou.lletres[nou.longitud++] = this.lletres[i];
        }
        if (nou.longitud < mida) {
            System.err.println("Error: " + this + ".subText(" + inici + "," + mida + ")");
        }
        return nou;
    }
    
    public boolean isPartsRepeated() {
        boolean repeteixTrobat = false;
        for (int candidata = 1; candidata < longitud && !repeteixTrobat; candidata++) {
            if (longitud % candidata == 0) {
                // Podem fer N trossos de mida candidata
                int N = longitud / candidata;
                Text primer = this.subText(0, candidata);
                boolean errorTrobat = false;
                for (int i = 1; i < N && !errorTrobat; i++) {
                    Text seguent = this.subText(i * candidata, candidata);
                    if (!primer.equals(seguent)) {
                        errorTrobat = true;
                    }
                }
                if (!errorTrobat) {
                    repeteixTrobat = true;
                }
            }
        }
        return repeteixTrobat;
    }
    
    public boolean equals(Text altre) {
        if (this.longitud != altre.longitud) {
            return false;
        }
        
        for (int i = 0; i < longitud; i++) {
            if (this.lletres[i] != altre.lletres[i]) {
                return false;
            }
        }
        
        return true;
    }
    
    public String toString() {
        String s = "";
        for (int i = 0; i < longitud; i++) {
            s += lletres[i];
        }
        return s;
    }
    
    public int[] toDigitArray() {
        int[] digits = new int[longitud];
        for (int i = 0; i < longitud; i++) {
            digits[i] = toInt(lletres[i]);
        }
        return digits;
    }
    
    
    public long getLargestJoltage2() {
        int[] digits = toDigitArray();
        long max = 0;
        for (int i = 0; i < longitud - 1; i++) {
            for (int j = i + 1; j < longitud; j++) {
                long current = digits[i] * 10 + digits[j];
                if (current > max) {
                    max = current;
                }
            }
        }
        return max;
    }
    
    public long sumJolts(int[] jolts) {
        long sum = 0;
        for (int i = 0; i < jolts.length; i++) {
            if (jolts[i] == 0) {
                continue;
            } else {
                sum *= 10;
                sum += jolts[i];
            }
        }
        return sum;
    }
    
    // Too slow
    private int[] maxBattery(int[] battery, int N, int currentLength) {
        if (currentLength == N) {
            return battery;
        } else {
            // Calculate all possible batteries with one removed
            long max = 0;
            int[] maxBattery = null;
            for (int i = 0; i < battery.length; i++) {
                int[] newBattery = new int[battery.length];
                if (battery[i] != 0) {
                    // Can remove this digit
                    System.arraycopy(battery, 0, newBattery, 0, battery.length);
                    newBattery[i] = 0;
                    int[] result = maxBattery(newBattery, N, currentLength - 1);
                    long sum = sumJolts(result);
                    if (sum > max) {
                        maxBattery = result;
                    }
                }
            }
            return maxBattery;
        }
    }
    
    public long getLargestJoltageN(int N) {
        int[] digits = toDigitArray();
        /*
        1. Count how many of each digit.
        2. L = length - N, number of excess digits.
        3. From the start, remove AT MOST L digits,
           while the current digit is less than the next one.
        4. Remove digits from that point onwards if they are low.
        */
        
        int[] freqs = new int[10];
        // init
        for (int i = 0; i < freqs.length; i++) {
            freqs[i] = 0;
        }
        // count
        for (int i = 0; i < digits.length; i++) {
            int digit = digits[i];
            freqs[digit - 1]++;
        }
        int L = digits.length - N;
        int index = 0;
        while (index < digits.length - 1 && L > 0 && digits[index] < digits[index + 1]) {
            freqs[digits[index] - 1]--; // remove one freq
            digits[index] = 0; // remove it
            L--; // one less to remove
            index++; // advance
        }
        System.out.println("Removed first " + index);
        // Find L digits to remove
        int[] toRemove = new int[10];
        for (int i = 0; i < toRemove.length; i++) {
            toRemove[i] = 0;
        }
        int pending = 0;
        for (int i = 0; i < freqs.length && pending < L; i++) {
            while (toRemove[i] < freqs[i] && pending < L) {
                toRemove[i]++;
                pending++;
            }
        }
        // Remove L digits, until the end of the array or we're left with only L digits
        while (index < digits.length) {
            int digit = digits[index];
            if (toRemove[digit - 1] > 0) {
                digits[index] = 0;
                toRemove[digit - 1]--;
                L--;
            }
            index++;
        }
        return sumJolts(digits);
    }
    
    public long test() {
        int[] digits = toDigitArray();
        return sumJolts(digits);
    }
    
}
