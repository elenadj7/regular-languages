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
            dfa.ReadFromFile(filename);

            System.out.println("Word: ");
            String word1 = scanner.nextLine();

            System.out.println(dfa.Accepts(word1));
            break;

            case 1:

            ENfa enfa = new ENfa();
            enfa.ReadFromFile(filename);
    
            System.out.println("Word: ");
            String word2 = scanner.nextLine();

            System.out.println(enfa.Accepts(word2));
            break;

            case 2:

            RegularExpression regex = new RegularExpression("");
            regex.ReadFromFile(filename);

            System.out.println("Word: ");
            String word3 = scanner.nextLine();
            System.out.println(regex.TransformToENfa().Accepts(word3));
            break;

            case 3:

            System.out.println("Dfa filename: ");
            String dfaName = scanner.next();

            Dfa dfa2 = new Dfa();
            dfa2.ReadFromFile(dfaName);

            dfa2.Generate(filename);
            break;

            default:
            System.out.println("Wrong option.");
        }

        scanner.close();
    }
}
