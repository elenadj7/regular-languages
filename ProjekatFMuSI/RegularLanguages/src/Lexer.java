import java.util.Vector;

/**
 * Lexer klasa provjerava leksicku ispravnost regularnog izraza
 * @author Elena Djurdjevic
 * @version 1.0
 */
public class Lexer {
    
    private String expression;
    private Vector<Character> tokens = new Vector<>();
    private Vector<Integer> lines = new Vector<>();
    private Integer currentLine = 1;
    private Integer brackets = 0;

    /**
     * Parametrizovani konstruktor
     * @param input regularni izraz
     */
    public Lexer(String input){

        expression = input;

    }

    private void MakeTokens(){

        for(int i = 0; i < expression.length(); ++i){

            if(expression.charAt(i) == '('){
                brackets++;
            }
            if(expression.charAt(i) == ')'){
                brackets--;
            }

            if(expression.charAt(i) == '\n'){
                currentLine++;
            }

            lines.add(currentLine);
            tokens.add(expression.charAt(i));

        }

    }

    private void RemoveWhitespaces(){

        Vector<Character> newTokes = new Vector<>();

        for(int i = 0; i < tokens.size(); ++i){

            Character element = tokens.elementAt(i);

            if(Character.isWhitespace(element)){
                
                lines.remove(i);

            }
            else{
                
                newTokes.add(element);

            }
        }

        tokens.removeAllElements();

        tokens.addAll(newTokes);

    }

    /**
     * Prolazi kroz tokene i provjerava da li je odredjenim tokenima 
     * dozvoljeno da se nalaze u vektoru jedan kraj drugog
     * @throws Exception ako se pronadje greska u tokenima
     */
    public void LexicalAnalysis()throws Exception{

        MakeTokens();
        RemoveWhitespaces();

        if(brackets != 0){

            for(int i = 0; i < tokens.size(); ++i){

                if(tokens.elementAt(i) == '(' || tokens.elementAt(i) == ')'){

                    throw new ErrorInLine(lines.elementAt(i));
                }
            }

        }

        for(int i = 0; i < tokens.size() - 1; ++i){

            Character element = tokens.elementAt(i);

            if(!Character.isLetterOrDigit(element) && element != '(' && element != ')' && element != '|' && element != '*' && element != '.'){

                throw new ErrorInLine(lines.elementAt(i));
            }

            if(i == 0 && !Character.isLetterOrDigit(element)){

                throw new ErrorInLine(lines.elementAt(i));

            }
            Character next = tokens.elementAt(i + 1);

            switch(element){

                case '(':

                if(next == '.' || next == ')' || next == '*' || next == '|'){

                    throw new ErrorInLine(lines.elementAt(i + 1));
                }
                break;

                case ')':

                if(next == '(' || Character.isLetterOrDigit(next)){

                    throw new ErrorInLine(lines.elementAt(i + 1));

                }
                break;

                case '.':

                if(!Character.isLetterOrDigit(next) && next != '('){

                    throw new ErrorInLine(lines.elementAt(i + 1));

                }
                break;

                case '|':

                if(!Character.isLetterOrDigit(next) && next != '('){

                    throw new ErrorInLine(lines.elementAt(i + 1));

                }
                break;

                case '*':

                if(next != '.'){

                    throw new ErrorInLine(lines.elementAt(i + 1));

                }
                break;
            }
        }
    }

    /**
     * Geter za tokene
     * @return tokene
     */
    public Vector<Character> GetTokens(){
        return tokens;
    }

}
