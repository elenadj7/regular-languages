import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Stack;
import java.util.Vector;

/**
 * <p>
 * Ova klasa omogucava implementaciju regularnih izraza. Sadrzi 
 * atribut expression koji predstavlja regularni izraz. Ima mogucnost
 * provjere da li su dva jezika ekvivalentna i transformacije u neki drugi oblik
 * poput Epsilon nedeterministickog i deterministickog konacnog automata.
 * @author Elena Djurdjevic
 * @version 1.0
 */
public class RegularExpression {
    
    public String expression = new String();

    /**
     * Konstruktor
     * @param input regularni izraz
     */
    public RegularExpression(String input){
        expression = input;
    }

    /**
     * Provjerava prioriter operatora. Najveci
     * prioritet ima Kleen-ov operator, zatim operator konkatenacije
     * jezika, a najmanji prioritet ima operator unije dva jezika.
     * @param operator operator ciji se prioritet provjerava
     * @return vrijednost prioriteta
     */
    private Integer OperatorsPriority(Character operator){
        switch(operator){
            case '*':
            return 3;


            case '.':
            return 2;


            case '|':
            return 1;

            
            default:
            return 0;
        }
    }


    /**
     * Konvertuje infiksni oblik regularnog izraza u postfiksni
     * pomocu steka. Na stek se stavljaju svi operatori osim desne zagrade, a simboli se 
     * skladiste u promjenljivu nazvanu postfix. Operatori se skidaju sa steka kada dodje desna zagrada ili
     * sve dok je prioritet operatora koji se treba staviti na stek manji ili jednak prioritetu operatora koji
     * je na vrhu steka.
     * @return regularni izraz u postfiksnom obliku 
     */
    private String ConvertToPostfix(){

        try{

            Lexer lexer = new Lexer(expression);
            lexer.LexicalAnalysis();
            Vector<Character> tokens = lexer.GetTokens();

            String postfix = new String();
            
            Stack<Character> operators = new Stack<>();
    
            for(int i = 0; i < tokens.size(); ++i){
    
                Character tmp = tokens.elementAt(i);
                
                if(tmp != '|' && tmp != '*' && tmp != '.' && tmp != '(' && tmp != ')'){
                    postfix += tmp;
                }
                else if(tmp == '('){
                    operators.push(tmp);
                }
                else if(tmp == ')'){
                    while(operators.lastElement() != '('){
                        postfix += operators.pop();
                    }
    
                    operators.pop();
                }
                else{
    
                    while(!operators.empty() && OperatorsPriority(tmp) <= OperatorsPriority(operators.lastElement())){
    
                        postfix += operators.pop();
                    }
    
                    operators.push(tmp);
                }
            }
    
            while(!operators.empty()){
    
                postfix += operators.pop();
            }

            return postfix;
        }
        catch(Exception e){

        }

        return "";
    }

    /**
     * Transformise regularni izraz u Epsilon nedeterministicki konacni automat pomocu steka.
     * Prolazi kroz regularni izraz u postfiksnom obliku i za svaki simbol kreira automat koji stavlja na 
     * stek, a ukoliko naidje na binarni operator skida prva dva automata sa vrha steka i izvrsi 
     * operaciju nad njima te finalni automat vraca na stek. Ukoliko naidje na unarni operator
     * skida samo jedan automat za vrha steka i vrsi operaciju nad tim automatom te njegovu transformaciju vraca na
     * stek. Algoritam zavrsava kada jedan automat ostane na steku i kada se prodje kroz cijeli postfiksi oblik.
     * @return Epsilon NKA dobijen od regularnog izraza
     */
    public ENfa TransformToENfa(){

        try{
            
            Lexer lexer = new Lexer(expression);
            lexer.LexicalAnalysis();
            String postfix = ConvertToPostfix();
            Stack<ENfa> enfa = new Stack<>();
    
            for(int i = 0; i < postfix.length(); ++i){
    
                Character tmp = postfix.charAt(i);
    
                if(tmp == '|'){
    
                    ENfa first = enfa.pop();
                    ENfa second = enfa.pop();
    
                    enfa.push(second.Union(first));
                }
                else if(tmp == '.'){
    
                    ENfa first = enfa.pop();
                    ENfa second = enfa.pop();
    
                    enfa.push(second.Concatenation(first));
                }
                else if(tmp == '*'){
    
                    ENfa first = enfa.pop();
    
                    enfa.push(first.KleeneStar());
                }
                else if(tmp != '(' && tmp != ')'){
                    
                    ENfa first = new ENfa();
    
                    enfa.push(first.OneSymbolENfa(tmp));
                }
            }
    
            if(!enfa.empty()){
                
                return enfa.pop();
    
            }
        }
        catch(Exception e){

            System.out.println(e.getMessage());

        }

        return new ENfa();
    }

    /**
     * Transformise regularni izraz u deterministicki konacni automat pomocu tako
     * sto prvo izraz transformise u ENKA a zatim ENKA transformise u DKA
     * @return deterministicki konacni automat dobijen od regularnog izraza
     */
    public Dfa TransformToDfa(){
        return TransformToENfa().TransformToDfa();
    }

    /**
     * Provjerava da li su dva regularna izraza ekvivalentna na nacin da ih transformise 
     * u reprezentaciju pomocu DKA a zatim poredi dva deterministicka automata
     * @param other regularni izraz koji se poredi sa pozivaocem
     * @return true ako su izrazi jednaki, false ako nisu
     */
    public Boolean AreEquals(RegularExpression other){

        return this.TransformToDfa().AreEquals(other.TransformToDfa());
    }

    /**
     * Upisuje regularni izraz u fajl
     * @param filename naziv fajla
     */
    public void WriteToFile(String filename){

        try{

            FileWriter writer = new FileWriter(filename);
            writer.write(expression + "\n");
            writer.close();

        }
        catch(Exception e){

            System.out.println("An error occurred.");

        }
    }

    /**
     * Ucitava regularni izraz iz fajla
     * @param filename naziv fajla
     */
    public void ReadFromFile(String filename){

        try{

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            expression = reader.readLine();
            reader.close();

            Lexer lexer = new Lexer(expression);
            lexer.LexicalAnalysis();

        }
        catch(Exception e){

            System.out.println("An error occurred." + e.getMessage());

        }
    }
}