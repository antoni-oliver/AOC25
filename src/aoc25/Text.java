package aoc25;

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
}
