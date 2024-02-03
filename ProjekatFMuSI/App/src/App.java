import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.println("[0] - DFA\n[1] - ENFA\n[2] - Regular Expression\n[3] - Code Generation");

        int option = scanner.nextInt();

        System.out.println("Filename: ");
        scanner.nextLine();
        String filename = scanner.nextLine();


        switch(option){
            
            case 0:

            Dfa dfa = new Dfa();
            dfa.readFromFile(filename);

            System.out.println("Word: ");
            String word1 = scanner.nextLine();

            System.out.println(dfa.accepts(word1));
            break;

            case 1:

            ENfa enfa = new ENfa();
            enfa.readFromFile(filename);
    
            System.out.println("Word: ");
            String word2 = scanner.nextLine();

            System.out.println(enfa.accepts(word2));
            break;

            case 2:

            RegularExpression regex = new RegularExpression("");
            regex.readFromFile(filename);

            System.out.println("Word: ");
            String word3 = scanner.nextLine();
            System.out.println(regex.transformToENfa().accepts(word3));
            break;

            case 3:

            System.out.println("Dfa filename: ");
            String dfaName = scanner.next();

            Dfa dfa2 = new Dfa();
            dfa2.readFromFile(dfaName);

            //dfa2.generate(filename);
            break;

            default:
            System.out.println("Wrong option.");
        }

        scanner.close();
    }
}
