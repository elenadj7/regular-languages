/**
 * Klasa koja nasljedjuje klasu Exceptions i sadrzi liniju koda
 * u kojoj se nalazi greska koja se pronadje tokom leksicke analize
 * @author Elena Djurdjevic
 * @version 1.0
 */
public class ErrorInLine extends Exception{

    private Integer line;

    /**
     * Parametrizovani konstruktor
     * @param line broj linije
     */
    public ErrorInLine(Integer line){
        this.line = line;
    }

    /**
     * Override-ovana metoda getMessage koja 
     * vraca liniju koda u kojoj je greska pronadjena
     */
    @Override
    public String getMessage(){

        return "Error in line " + line.toString();

    }
}
