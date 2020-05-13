package search;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> people = new ArrayList<>();
        String word = " ";
        int noPeople = 0;
        int searchQ = 0;
        Main obj = new Main();

        //get all elements and all strings that should be searched in the elements
       try (Scanner scanner = new Scanner(System.in)) {
          System.out.println("Enter the number of people: ");

           if (scanner.hasNextInt()) {
               noPeople = scanner.nextInt();
           }

           System.out.println("Enter all people: ");
           scanner.nextLine();

           while (scanner.hasNextLine() && noPeople > 0) {
                people.add(scanner.nextLine());
                noPeople--;
           }

            System.out.println("Enter the number of search queries: ");

           if (scanner.hasNextLine()) {
               searchQ = Integer.parseInt(scanner.nextLine());
           }

           while (searchQ > 0) {
               System.out.println("");
               System.out.println("Enter data to search people: ");

               if (scanner.hasNextLine()) {
                   word = scanner.nextLine().trim();
               }
               obj.getMatch(people, word);
               searchQ--;
           }

       }
       catch (Exception e) {
           System.out.println("Problems with reading the input");
       }
    }

    private void getMatch(ArrayList<String> people, String word) {
        ArrayList<String> matches = new ArrayList<>();

        //search for the strings in the elements
        for (int i = 0; i < people.size(); i++) {
               if (Arrays.stream(people.get(i)
                       .toLowerCase().split(" "))
                       .anyMatch(s -> s.contains(word.toLowerCase()))) {
                   matches.add(people.get(i));
               }
           }

        //print the output
        if (matches.size() > 0) {
            System.out.println("");
            System.out.println("Found people: ");
            for (int i = 0; i < matches.size(); i++) {
                System.out.println(matches.get(i));
            }
        } else {
            System.out.println("No matching people found.");
        }
}}
