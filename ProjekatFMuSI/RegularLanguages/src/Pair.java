/**
* <p> 
* Sadrzi uredjen par (state, symbol) i prvenstveno sluzi za
* funkciju prelaza kod deterministickih i nedeterministickih 
* konacnih automata
* @author  Elena Djurdjevic
* @version 1.0

*/

public class Pair {
    private String state;
    private Character symbol;

    /**
     * Konstruktor klase
     * @param newState skladisti stanje
     * @param newSymbol skladisti karakter
     */
    public Pair(String newState, Character newSymbol){
        state = newState;
        symbol = newSymbol;
    }

    /**
     * Geter za stanje
     * @return stanje
     */
    public String GetState(){
        return state;
    }

    /**
     * Geter za simbol
     * @return karakter
     */
    public Character GetSymbol(){
        return symbol;
    }

    /**
     * Seter za stanje
     * @param newState novo stanje
     */
    public void SetState(String newState){
        state = newState;
    }

    /**
     * Seter za simbol
     * @param newSymbol novi karakter
     */
    public void SetSymbol(Character newSymbol){
        symbol = newSymbol;
    }

    /**
     * Konkatenacija dva stanja koja imaju isti prelaz
     * @param other uredjen par cije se stanje konkatenira sa stanjem pozivaoca
     * @return uredjen par cija su stanja konkatenirana
     */
    public Pair sumOfPairs(Pair other){
        return new Pair(state + other.state, symbol);
    }

    /**
     * Ispis uredjenog para (state, symbol)
     */
    public String toString(){
        return "state : " + GetState() + "   symbol : " + GetSymbol();
    }

    /**
     * Override-ovana metoda equals za provjeru
     * jednakosti dva para
     */
    @Override
    public boolean equals(Object obj) {
        if (this.getClass() == obj.getClass()) {
            final Pair other = (Pair) obj;

            return (symbol == other.symbol && state.equals(other.state));
        }
        return false;
    }

    /**
     * Override-ovana metoda hashCode
     */
    @Override
    public int hashCode() {
        int hash = 13;
        hash = 5 * hash + (state != null ? state.hashCode() : 0);
        hash = 5 * hash + symbol;
        return hash;
    }
}