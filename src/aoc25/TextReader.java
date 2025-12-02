package aoc25;

import java.io.*;

/**
 *
 * @author Antoni
 */
public class TextReader {
    private FileReader fr;
    private BufferedReader br;
    private int codi;
    
    public TextReader(String nomFitxer) throws Exception {
        fr = new FileReader(nomFitxer);
        br = new BufferedReader(fr);
        codi = br.read(); // començam
    }
    
    private void skip(char separator) throws Exception {
        while (codi == separator) {
            codi = br.read();
        }
    }
    
    public Text readText(char separator) throws Exception {
        Text text = new Text();
        while(codi != separator && codi != -1 && codi != '\n') {
            text.afegeix((char) codi);
            codi = br.read();
        }
        skip(separator);
        return text;
    }
    
    public void tanca() throws Exception {
        br.close();
        fr.close();
    }
}
