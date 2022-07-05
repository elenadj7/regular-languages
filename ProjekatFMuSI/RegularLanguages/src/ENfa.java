import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.Vector;
import java.util.Map.Entry;



/** 
 * <p>
 * Ova klasa omogucava reprezentaciju jezika u Epsilon nedeterministickom
 * konacnom automatu. Moguce je izvrsiti operacije konkatenacije, Kleen-ove zvijezde i unije dva automata
 * sa podrskom za ulancavanje operacija. Takodje je moguce transformisati ENKA u DKA i 
 * moguce je ispitati pripadnost rijeci nekom jeziku. Takodje je moguce kreirati automat
 * koji prihvata samo jedan simbol.
 * @author Elena Djurdjevic
 * @version 1.0
 */
public class ENfa {
    private HashMap<Pair, HashSet<String>> deltaFunction = new HashMap<Pair, HashSet<String>>();
    private String startState;
    private HashSet<String> finalStates = new HashSet<String>();

    /**
     * Podrazumijevani konstruktor
     */
    public ENfa(){

    }

    /**
     * Copy konstruktor
     * @param other automat koji se kopira
     */
    public ENfa(ENfa other){
        deltaFunction.putAll(other.deltaFunction);
        startState = other.startState;
        finalStates.addAll(other.finalStates);
    }

    /**
     * Dodaje novo pocetno stanje
     * @param state novo pocetno stanje
     */
    public void SetStartState(String state){
        startState = state;
    }

    /**
     * Dodaje novo finalno stanje
     * @param state novo finalno stanje
     */
    public void AddFinalState(String state){
        finalStates.add(state);
    }

    /**
     * Dodaje novu tranziciju u funkciju prelaza
     * @param currentState novo trenutno stanje
     * @param symbol simbol za koji se prelazi u sljedece stanje
     * @param nextState novo sljedece stanje
     */
    public void AddTransition(String currentState, Character symbol, String nextState){
        Pair key = new Pair(currentState, symbol);
        if(deltaFunction.containsKey(key)){
            deltaFunction.get(key).add(nextState);
        }
        else{
            HashSet<String> value = new HashSet<String>();
            value.add(nextState);
            deltaFunction.put(key, value);
        }
    }

    /**
     * Vrsi closure jednog stanja tako sto trazi sve epsilon prelaze koji idu iz tog stanja
     * pomocu steka. Na stek se stavljaju stanja koja treba da se obidju, zatim se skine element sa
     * vrha i traze se njegovi epsilon prelazi, i tako sve dok nesto postoji na steku. Sva stanja se smijestaju
     * u HashSet stringova koji se zove closure.
     * @param state stanje od kojeg se trazi closure
     * @return set svih stanja koja se dobiju closure-om proslijedjenog stanja
     */
    public HashSet<String> Closure(String state){
        HashSet<String> closure = new HashSet<>();
        try{
            closure.addAll(deltaFunction.get(new Pair(state, '$')));
        }catch(Exception e){

        }
        Stack<String> vector = new Stack<>();
        vector.addAll(closure);
        do{
            try{
                HashSet<String> tmp = deltaFunction.get(new Pair(vector.pop(), '$'));

                for(String element : tmp){
                    if(!vector.contains(element) && !closure.contains(element)){
                        vector.add(element);
                    }
                }
                closure.addAll(tmp);
            }catch(Exception e){

            }
        }while(!vector.isEmpty());
        closure.add(state);
        return closure;
    }

    /**
     * Provjerava da li automat prihvata rijec tako sto prolazi kroz sve funkcije prelaza za
     * svaki karakter u proslijedjenoj rijeci i sva stanja koja obidje stavlja u Hashset stanja. Kada dodje
     * do kraja rijeci provjerava da li se u Hashset-u stanja nalazi bar jedno finalno stanje. Ako se nalazi u bar 
     * jednom finalnom stanju vraca true, a ako je sav skup stanja nefinalan onda vraca false.
     * @param inputString rijec koju provjerava da li pripada jeziku
     * @return true ako rijec pripada jeziku, false ako ne pripada
     */
    public boolean Accepts(String inputString){
        HashSet<String> currentState = Closure(startState);
        for(int i = 0; i < inputString.length(); ++i){
            HashSet<String> tmpState = new HashSet<String>();
            for(String state : currentState){
                try{
                    tmpState.addAll(deltaFunction.get(new Pair(state, inputString.charAt(i))));
                }catch(Exception e){

                }
            }
            HashSet<String> tmpClosure = new HashSet<String>();

            for(String state : tmpState){
                tmpClosure.addAll(Closure(state));
            }
            currentState = tmpClosure;
        }
        if(IsFinal(currentState)){
            return true;
        }
        return false;
    }

    /**
     * Izvrsava operaciju Kleene-ove zvijezde tako sto dodaje novo stanje koje postaje i finalno i pocetno,
     * kopira sve ostale prelaze i dodaje epsilon prelaze iz finalnih stanja u novo stanje i iz novog stanja u pocetno stanje.
     * Finalna stanja formira tako sto dodaje samo novo stanje u finalno.
     * @return novi automat koji je rezultat primjene Kleene-ovog operatora nad jezikom
     */
    public ENfa KleeneStar(){
        ENfa ret = new ENfa();
        String retStartState = "&";
        ret.SetStartState(retStartState);
        ret.AddFinalState(retStartState);
        ret.AddTransition(retStartState, '$', startState);
        ret.deltaFunction.putAll(deltaFunction);
        for(var element : finalStates){
            ret.AddTransition(element, '$', retStartState);
        }
        return ret;
    }

    /**
     * Vrsi spajanje dva jezika tako sto kreira novi automat kojem postavlja pocetno stanje prvog automata
     * a finalna stanja drugog automata. Zatim dodaje novo stanje i povezuje sva finalna stanja
     * prvog automata epsilon prelazom za novo stanje i novo stanje povezuje epsilon prelazom za pocetno stanje drugog
     * automata.
     * @param other automat koji se konkatenira sa pozivaocem
     * @return novi automat koji je rezultat primjene operatora konkatenacije nad dva automata
     */
    public ENfa Concatenation(ENfa other){
        ENfa ret = new ENfa();

        ret.SetStartState(startState);
        ret.deltaFunction.putAll(deltaFunction);
        ret.deltaFunction.putAll(other.deltaFunction);
        ret.finalStates.addAll(other.finalStates);
        
        for(var element : finalStates){
            ret.AddTransition(element, '$', "#");
        }

        ret.AddTransition("#", '$', other.startState);

        return ret;
    }

    /**
     * Vrsi uniju dva automata tako sto dodaje novo pocetno stanje koje povezuje epsilon prelazima 
     * sa pocetnim stanjima oba automata, a zatim dodaje novo finalno stanje tako sto povezuje sva finalna
     * stanja oba automata epsilon prelazom za novim finalnim stanjem.
     * @param other automat nad kojim se vrsi unija zajedno za pozivaocem
     * @return novi automat koji je rezultat primjene operatora unije nad dva automata
     */
    public ENfa Union (ENfa other){
        ENfa ret = new ENfa();

        ret.deltaFunction.putAll(deltaFunction);
        ret.deltaFunction.putAll(other.deltaFunction);
        ret.finalStates.addAll(finalStates);
        ret.finalStates.add("/");

        ret.SetStartState("%");
        ret.AddTransition("%", '$', startState);
        ret.AddTransition("%", '$', other.startState);
        for(var element : other.finalStates){

            ret.AddTransition(element, '$', "/");

        }

        return ret;
    }

    /**
     * Pronalazi sve simbole u jeziku.
     * @return alfabet jezika
     */
    private HashSet<Character> GetSymbols(){
        HashSet<Character> ret = new HashSet<>();
        for (Entry<Pair, HashSet<String>> stateTransition : this.deltaFunction.entrySet()) {
            if(stateTransition.getKey().GetSymbol() != '$'){
                ret.add(stateTransition.getKey().GetSymbol());
            }
        }
        return ret;
    }

    /**
     * Vrsi konkatenaciju svih stanja u jedno novo stanje.
     * @param states skup stanja koja se konkateniraju
     * @return naziv novog stanja
     */
    private String MakeString(HashSet<String> states){
        String ret = new String();
        for(var element : states){
            ret = ret.concat(element);
        }
        return ret;
    }
    
    /**
     * Provjerava da li je bar jedno stanje finalno u setu stanja
     * @param states skup stanja u kojem se trazi bar jedno finalno stanje
     * @return true ako postoji bar jedno finalno stanje u skupu, false ako ne postoji 
     */
    private boolean IsFinal(HashSet<String> states){
        for(var element : states){
            if(finalStates.contains(element)){
                return true;
            }
        }
        return false;
    }

    /**
     * Transformise ENKA u DKA tako sto obilazi ENKA i za svako
     * stanje trazi closure.
     * @return novi automat koji je tipa DKA
     */
    public Dfa TransformToDfa(){
        if(deltaFunction.isEmpty()){
            return new Dfa();
        }
        
        Dfa ret = new Dfa();
        Vector<HashSet<String>> toVisit = new Vector<>();
        HashSet<String> newStartState = Closure(startState);
        toVisit.add(newStartState);
        HashSet<Character> symbols = GetSymbols();
        ret.SetStartState(MakeString(newStartState));
        for(int i = 0; i < toVisit.size(); ++i){
            HashSet<String> iterate = toVisit.elementAt(i);
            for(var transition : symbols){
                HashSet<String> tmpstates1 = new HashSet<>();
                for(var element : iterate){
                    try{
                        tmpstates1.addAll(deltaFunction.get(new Pair(element, transition)));
                    }catch(Exception e){
                    }
                }
                HashSet<String> tmpstates2 = new HashSet<>();
                for(var tmpclosure : tmpstates1){
                    tmpstates2.addAll(Closure(tmpclosure));
                }
                if(IsFinal(tmpstates2)){
                    ret.AddFinalState(MakeString(tmpstates2));
                }
                ret.AddTransition(MakeString(iterate), transition, MakeString(tmpstates2));
                if(!toVisit.contains(tmpstates2)){
                    toVisit.insertElementAt(tmpstates2, toVisit.size());
                }
            }
        }
        return ret;
    }

    /**
     * Formira ENKA koji prihvata rijeci koje se sastoje samo od jednog simbola
     * koji se prosljedjuje kao argument metode
     * @param symbol simbol od kojeg se formira novi ENKA
     * @return novi ENKA 
     */
    public ENfa OneSymbolENfa(Character symbol){
        ENfa ret = new ENfa();

        ret.SetStartState(symbol.toString() + "0");
        ret.AddFinalState(symbol.toString() + "1");
        ret.AddTransition(symbol.toString() + "0", symbol, symbol.toString() + "1");
        ret.AddTransition(symbol.toString() + "1", symbol, symbol.toString() + "2");

        return ret;
    }

    /**
     * Provjerava da li su dva ENKA ekvivalentna tako sto ih prvo 
     * pretvori u DKA a zatim ih poredi po jednakosti.
     * @param other ENKA koji se poredi sa pozivajucim ENKA
     * @return true ako su jednaki, false ako nisu
     */
    public Boolean AreEquals(ENfa other){

        return this.TransformToDfa().AreEquals(other.TransformToDfa());
    }

    /**
     * Upisuje automat u fajl
     * @param filename naziv fajla
     */
    public void WriteToFile(String filename){

        try{
            FileWriter writer = new FileWriter(filename);
            writer.write(startState + "\n");
            
            for(var element : finalStates){

                writer.write(element + "-");

            }
            
            writer.write("\n");

            for (Entry<Pair, HashSet<String>> stateTransition : deltaFunction.entrySet()){
                
                writer.write(stateTransition.getKey().GetState() + "-" + stateTransition.getKey().GetSymbol() + "-");

                for(var element : stateTransition.getValue()){
                    writer.write(element + ",");
                }

                writer.write("\n");

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
    public void ReadFromFile(String filename){
        
        try{

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            
            String line = reader.readLine();

            SetStartState(line);

            line = reader.readLine();

            int pointer = 0;
            for(int i = 0; i < line.length(); ++i){

                if(line.charAt(i) == '-'){
                    AddFinalState(line.substring(pointer, i));
                    pointer = i + 1;
                }

            }

            line = reader.readLine();
            while(line != null){
                Vector<String> transitions = new Vector<>();
                HashSet<String> states = new HashSet<>();

                pointer = 0;

                for(int i = 0; i < line.length(); ++i){

                    if(line.charAt(i) == '-'){

                        transitions.add(line.substring(pointer, i));
                        pointer = i + 1;

                    }
                    else if(line.charAt(i) == ','){

                        states.add(line.substring(pointer, i));
                        pointer = i + 1;

                    }
                }
                
                for(var element : states){

                    AddTransition(transitions.elementAt(0), transitions.elementAt(1).charAt(0), element);
                }

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