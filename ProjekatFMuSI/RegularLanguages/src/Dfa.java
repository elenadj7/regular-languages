import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;


/**
 * <p>
 * Klasa implementuje deterministicki konacni automat. Ima mogucnost provjere
 * da li rijec pripada jeziku, pronalazak duzine najkrace i najduze rijeci u jeziku, te 
 * ispituje konacnost jezika. Ima mogucnost vrsenja unije, presjeka, konkatenacije, razlike
 * i komplementa jezika sa podrskom za ulancavanje operacija.
 * @author Elena Djurdjevic
 * @version 1.0
 */
public class Dfa {

    private HashMap<Pair, String> deltaFunction = new HashMap<>();
    public String startState;
    public HashSet<String> finalStates = new HashSet<>();

    /**
     * Podrazumijevani konstruktor
     */
    public Dfa(){

    }

    /**
     * Copy konstruktor
     * @param other automat koji se kopira
     */
    public Dfa(Dfa other){
        deltaFunction.putAll(other.deltaFunction);
        startState = other.startState;
        finalStates.addAll(other.finalStates);
    }

    /**
     * Postavlja novo pocetno stanje
     * @param state novo pocetno stanje
     */
    public void setStartState(String state){
        startState = state;
    }

    /**
     * Dodaje novo finalno stanje
     * @param state finalno stanje
     */
    public void addFinalState(String state){
        finalStates.add(state);
    }

    /**
     * Dodaje novi prelaz u automatu
     * @param currentState novo trenutno stanje
     * @param newSymbol simbol za koji se prelazi u sljedece stanje
     * @param nextState novo sljedece stanje
     */
    public void addTransition(String currentState, Character newSymbol, String nextState){
        deltaFunction.put(new Pair(currentState, newSymbol), nextState);
    }

    /**
     * Ispituje da li automat prihvata rijec tako sto prolazi 
     * kroz automat za sve prelaze u stringu i ako se na kraju nadje u 
     * stanju koje je finalno to znaci da prihvata rijec, a ako se nadje u stanju koje 
     * nije finalno to znaci da ne prihvata rijec
     * @param inputString rijec koju automat ispituje
     * @return vrijednost true ako prihvata a false ako ne prihvata rijec 
     */
    public boolean accepts(String inputString){
        String currentState = startState;
        for (int i = 0; i < inputString.length(); ++i) {
            currentState = deltaFunction.get(new Pair(currentState, inputString.charAt((i))));
        }
        return finalStates.contains(currentState);
    }

    /**
     * Pravi uniju dva automata tako sto pravi Dekartov proizvod svih stanja konkatenacijom stanja, 
     * a zatim sve tranzicije smijesta u funkciju prelaza. Finalna stanja novog automata su finalna stanja prvog
     * i drugog automata.
     * @param other automat koji ucestvuje u uniji zajedno sa pozivaocem
     * @return novi automat koji predstavlja uniju dva automata
     */
    public Dfa union(Dfa other){
        Dfa ret = new Dfa();
        ret.startState = startState + other.startState;
        for (var element1 : deltaFunction.keySet()) {
            for(var element2 : other.deltaFunction.keySet()){
                if(element1.getSymbol() == element2.getSymbol()){
                    ret.deltaFunction.put(element1.sumOfPairs(element2), deltaFunction.get(element1) + other.deltaFunction.get(element2));
                }
                if(finalStates.contains(element1.getState()) || other.finalStates.contains(element2.getState())){
                    ret.finalStates.add(element1.getState() + element2.getState());
                }
            }
        }
        return ret;
    }

    /**
     * Pravi presjek dva automa tako sto pravi Dekartov proizvod svih stanja konkatenacijom stanja, 
     * a zatim sve tranzicije smijesta u funkciju prelaza. Finalna stanja novog automata su stanja koja su finalna 
     * i u prvom i u drugom automatu.
     * @param other automat koji ucestvuje u presjeku zajedno za pozivaocem
     * @return novi automat koji predstavlja presjek dva automata
     */
    public Dfa intersection(Dfa other){
        Dfa ret = new Dfa();
        ret.startState = startState + other.startState;
        for (var element1 : deltaFunction.keySet()) {
            for(var element2 : other.deltaFunction.keySet()){
                if(element1.getSymbol() == element2.getSymbol()){
                    ret.deltaFunction.put(element1.sumOfPairs(element2), deltaFunction.get(element1) + other.deltaFunction.get(element2));
                }
                if(finalStates.contains(element1.getState()) && other.finalStates.contains(element2.getState())){
                    ret.finalStates.add(element1.getState() + element2.getState());
                }
            }
        }
        return ret;
    }

    /**
     * Pravi razliku dva automata tako sto pravi Dekartov proizvod svih stanja konkatenacijom stanja, 
     * a zatim sve tranzicije smijesta u funkciju prelaza. Finalna stanja novog automata su stanja koja su finalna 
     * u prvom, a nisu u drugom automatu.
     * @param other automat koji ucestvuje u razlici 
     * @return novi automat koji predstavlja razliku this i other automata
     */
    public Dfa diference(Dfa other){
        Dfa ret = new Dfa();
        ret.startState = startState + other.startState;
        for (var element1 : deltaFunction.keySet()) {
            for(var element2 : other.deltaFunction.keySet()){
                if(element1.getSymbol() == element2.getSymbol()){
                    ret.deltaFunction.put(element1.sumOfPairs(element2), deltaFunction.get(element1) + other.deltaFunction.get(element2));
                }
                if(finalStates.contains(element1.getState()) && !other.finalStates.contains(element2.getState())){
                    ret.finalStates.add(element1.getState() + element2.getState());
                }
            }
        }
        return ret;
    }

    /**
     * Pravi komplement automata tako sto kopira funkciju prelaza, a finalna stanja 
     * formira od stanja koja nisu finalna u automatu nad kojim se vrsi komplement.
     * @return automat koji predstavlja komplement pozivaoca
     */
    public Dfa complement(){
        Dfa ret = new Dfa();
        ret.startState = startState;
        for(var element : deltaFunction.keySet()){
            if(!finalStates.contains(element.getState())){
                ret.finalStates.add(element.getState());
            }
            ret.deltaFunction.put(element, deltaFunction.get(element));
        }
        return ret;
    }

    /**
     * Spaja dva automata u jedan tako sto kopira funkciju prelaza i sva finalna
     * stanja prvog automata mijenja pocetnim stanjem drugog automata, a finalna stanja novog automata
     * formira tako sto kopira finalna stanja drugog automata.
     * @param other automat koji se spaja zajedno sa pozivaocem
     * @return novi automat koji je konkatenacija dva automata
     */
    public Dfa concatenation(Dfa other){
        Dfa ret = new Dfa();
        ret.startState = startState;
        ret.finalStates = other.finalStates;
        for(var element : deltaFunction.keySet()){
            if(finalStates.contains(deltaFunction.get(element))){
                ret.deltaFunction.put(element, other.startState);
            }
            else{
                ret.deltaFunction.put(element, deltaFunction.get(element));
            }
        }
        for(var element : other.deltaFunction.keySet()){
            ret.deltaFunction.put(element, other.deltaFunction.get(element));
        }
        return ret;
    }

    /**
     * Pretrazuje funkciju prelaza i trazi sve simbole jezika
     * @return alfabet jezika
     */
    private HashSet<Character> getSymbols(){
        HashSet<Character> ret = new HashSet<>();
        for (Map.Entry<Pair, String> stateTransition : this.deltaFunction.entrySet()) {
                ret.add(stateTransition.getKey().getSymbol());
        }
        return ret;
    }

    /**
     * Pretrazuje funkciju prelaza i trazi sva stanja automata
     * @return skup stanja automata
     */
    private HashSet<String> getStates(){
        HashSet<String> ret = new HashSet<>();
        for(Map.Entry<Pair, String> stateTransition : deltaFunction.entrySet()){
            ret.add(stateTransition.getKey().getState());
        }
        return ret;
    }

    /**
     * Ispisuje prelaze automata u formi trenutno stanje_simbol->novo stanje
     */
    public void print() {
        for (Map.Entry<Pair, String> stateTransition : this.deltaFunction.entrySet()) {
            System.out.println(stateTransition.getKey() + " -> " + stateTransition.getValue());
        }
    }

    /**
     * Preimenuje sva stanja u automatu na osnovu stringa koji prima kao parametar. Preimenuje uz pomoc brojaca
     * koji se svaki put povecava kada se formira novo stanje. U mapi newNames skladisti parove (staro ime, novo ime) tako sto
     * prolazi kroz funkciju prelaza i svakom stanju koje nije u mapi newNames dodaje novo ime i smijesta ga u 
     * tu mapu.
     * @param statename naziv stanja
     */
    public void renameStates(String statename){
        Integer position = 0;
        HashMap<String, String> newNames = new HashMap<>();

        newNames.put(startState, statename + position.toString());
        position++;

        setStartState(newNames.get(startState));

        for (Map.Entry<Pair, String> stateTransition : this.deltaFunction.entrySet()) {
            
            if(!newNames.containsKey(stateTransition.getKey().getState())){

                newNames.put(stateTransition.getKey().getState(), statename + position.toString());
                position++;

            }
            if(!newNames.containsKey(stateTransition.getValue())){

                newNames.put(stateTransition.getValue(), statename + position.toString());
                position++;

            }
        }

        HashSet<String> newFinalStates = new HashSet<>();
        HashSet<String> tmp1 = new HashSet<>();
        for(var element : finalStates){
            newFinalStates.add(newNames.get(element));
        }
        
        tmp1.addAll(finalStates);
        finalStates.removeAll(tmp1);
        finalStates.addAll(newFinalStates);

        HashMap<Pair, String> newDeltaFunction = new HashMap<>();
        for (Map.Entry<Pair, String> stateTransition : this.deltaFunction.entrySet()){

            newDeltaFunction.put(new Pair(newNames.get(stateTransition.getKey().getState()), stateTransition.getKey().getSymbol()), newNames.get(stateTransition.getValue()));

        }

        HashMap<Pair, String> tmp2 = new HashMap<>();
        tmp2.putAll(deltaFunction);
        for (Map.Entry<Pair, String> stateTransition : tmp2.entrySet()){
            deltaFunction.remove(stateTransition.getKey(), stateTransition.getValue());
        }

        deltaFunction.putAll(newDeltaFunction);

    }

    /**
     * Modifikuje funkciju prelaza tako sto uklanja 
     * sva nedohvatna stanja, tj stanja do kojih nije moguce doci ni za jedan prelaz ni
     * iz jednog stanja tako sto prolazi kroz sve tranzicije i smijesta ih u vektor stanja. Potom
     * pravi novu funkciju prelaza i u nju stavlja samo prelaze koji nisu u posjeceni. Konacno 
     * se iz funkcije prelaza brisu nedohvatna stanja tako sto se obrisu svi prelazi iz nove funkcije prelaza.
     */
    public void removeInaccessibleStates(){
        Vector<String> states = new Vector<>();
        HashSet<String> toVisit = new HashSet<>();
        HashSet<Character> alphabet = getSymbols();

        states.add(startState);

        for(int i = 0; i < states.size(); ++i){

            toVisit.add(states.elementAt(i));

            for(var element : alphabet){

                String nextState = deltaFunction.get(new Pair(states.elementAt(i), element));

                if(!toVisit.contains(nextState)){
                    states.add(nextState);
                }
            }
        }

        HashMap<Pair, String> newDeltaFunction = new HashMap<>();
        for (Map.Entry<Pair, String> stateTransition : deltaFunction.entrySet()){
            
            if(!toVisit.contains(stateTransition.getKey().getState())){

                newDeltaFunction.put(stateTransition.getKey(), stateTransition.getValue());
            }
        }

        
        for (Map.Entry<Pair, String> stateTransition : newDeltaFunction.entrySet()){
            deltaFunction.remove(stateTransition.getKey(), stateTransition.getValue());
        }
    }

    /**
     * Sortira prelaze pocevsi od pocetnog stanja. U vektor posjecenosti stavlja
     * pocetno stanje, a zatim sva stanja do kojih vodi prelaz iz pocetnog stanja i sve tako
     * dok se ne obidju sva stanja. Ovo se radi za svaki automat kako bi se njihove funkcije
     * prelaza mogle porediti.
     * @param symbols alfabet automata
     */
    private void sortTransitions(HashSet<Character> symbols){

        Vector<String> toVisit = new Vector<>();
        toVisit.add(startState);
        HashMap<Pair, String> newDeltaFunction = new HashMap<>();

        for(int i = 0; i < toVisit.size(); ++i){

            for(var element : symbols){

                String state = deltaFunction.get(new Pair(toVisit.elementAt(i), element));
                if(!toVisit.contains(state)){
                    toVisit.add(state);
                }
                newDeltaFunction.put(new Pair(toVisit.elementAt(i), element), state);
            }
        }

        HashMap<Pair, String> tmp = new HashMap<>();
        tmp.putAll(deltaFunction);
        
        for (Map.Entry<Pair, String> stateTransition : tmp.entrySet()){
            deltaFunction.remove(stateTransition.getKey(), stateTransition.getValue());
        }

        deltaFunction.putAll(newDeltaFunction);
    }

    /**
     * Provjerava da li su dva automata jednaka tako sto ih prvo
     * preimenuje i poredi funkcije prelaza, finalna stanja i pocetno stanje
     * @param other automat koji se poredi sa pozivaocem
     * @return true ako su jednaki, false ako nisu
     */
    public Boolean areEquals(Dfa other){
        HashSet<Character> symbols = getSymbols();
        sortTransitions(symbols);
        other.sortTransitions(symbols);
        renameStates("q");
        other.renameStates("q");

        Boolean equals = false;
        if(deltaFunction.equals(other.deltaFunction) && startState.equals(other.startState) && finalStates.equals(other.finalStates)){
            equals = true;
        }

        return equals;
    }
    
    /**
     * Pronalazi najkracu putanju od pocetnog do finalnog stanja tako sto
     * obilazi automat BFS algoritmom.
     * @return duzinu putanje
     */
    public Integer shortestPath(){

        Integer distance = 0;
        Vector<String> toVisit = new Vector<>();
        HashSet<Character> alphabet = getSymbols();
        Vector<String> toVisit2 = new Vector<>();

        toVisit.add(startState);

        int counter = 1;
        while(counter != 0){

            for(int j = 0; j < toVisit.size(); ++j){

                if(finalStates.contains(toVisit.elementAt(j))){
                    return distance;
                }

            }

            for(var element : alphabet){

                for(int j = 0; j < toVisit.size(); ++j){

                    String state = deltaFunction.get(new Pair(toVisit.elementAt(j), element));

                    if(!state.equals(toVisit.elementAt(j))){
                        toVisit2.add(state);
                    }
                }
            }

            distance++;
            toVisit.removeAllElements();
            toVisit.addAll(toVisit2);
            toVisit2.removeAllElements();
            counter = toVisit.size();
        }

        return -1;
    }

    /**
     * Upisuje automat u fajl
     * @param filename naziv fajla
     */
    public void writeToFile(String filename){

        try{
            FileWriter writer = new FileWriter(filename);
            writer.write(startState + "\n");
            
            for(var element : finalStates){

                writer.write(element + ",");

            }
            
            writer.write("\n");

            for (Map.Entry<Pair, String> stateTransition : deltaFunction.entrySet()){
                
                writer.write(stateTransition.getKey().getState() + "," + stateTransition.getKey().getSymbol() + "," + stateTransition.getValue() + "," + "\n");

            }

            writer.close();
        }
        catch(Exception e){

            System.out.println("An error occurred.");

        }
    }

    /**
     * Ucitava automat iz fajla
     * @param filename naziv fajla
     */
    public void readFromFile(String filename){

        try{

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            
            String line = reader.readLine();

            setStartState(line);

            line = reader.readLine();

            int pointer = 0;
            for(int i = 0; i < line.length(); ++i){

                if(line.charAt(i) == ','){
                    addFinalState(line.substring(pointer, i));
                    pointer = i + 1;
                }

            }

            line = reader.readLine();
            while(line != null){
                Vector<String> transitions = new Vector<>();
                pointer = 0;

                for(int i = 0; i < line.length(); ++i){

                    if(line.charAt(i) == ','){

                        transitions.add(line.substring(pointer, i));
                        pointer = i + 1;

                    }
                }
                
                addTransition(transitions.elementAt(0), transitions.elementAt(1).charAt(0), transitions.elementAt(2));
                line = reader.readLine();

            }

            reader.close();

        }
        catch(Exception e){

            System.out.println("An error occurred");
            System.out.println(e.getMessage());

        }

    }
}
