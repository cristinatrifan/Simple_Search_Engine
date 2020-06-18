package search;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        List<String[]> people = new ArrayList<>();
        String word = " ";
        int menuNo = 1;
        Main obj = new Main();

        if (Arrays.asList(args).contains("--data")) {
            File file = new File(Arrays.asList(args)
                    .get(Arrays.asList(args)
                            .indexOf("--data") + 1));

            //get all elements from the file
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    people.add(scanner
                            .nextLine()
                            .trim()
                            .split(" "));
                }

                //create inverted index with the people data:
                obj.createInvertedIndex(people);

            } catch (Exception e) {
                System.out.println("Problems with reading the file");
            }

            //get all strings that should be searched in the elements
            try (Scanner scanner = new Scanner(System.in)) {

                //scanner.nextLine();
                while (menuNo > 0) {
                    System.out.println("\n=== Menu ===\n" +
                            "1. Find a person\n" + "2. Print all people\n" + "0. Exit");

                    if (scanner.hasNextLine()) {
                        menuNo = Integer.parseInt(scanner.nextLine());
                    }

                    //search for someone in the list
                    switch (menuNo) {
                        case 1 : {
                            System.out.println("");
                            System.out.println("Enter a name or email to search all suitable people.");

                            if (scanner.hasNextLine()) {
                                word = scanner.nextLine().trim();
                            }

                            //use the inverted index search to find the
                            //information about the required person
                            obj.getMatchByID(word, people);
                            break;
                        }

                        //print the list of people
                        case 2 : {
                            System.out.print("\n=== List of people ===");
                            for (int i = 0; i < people.size(); i++) {
                                System.out.print("\n");
                                System.out.println(obj.toString(people.get(i)));
                            }
                            System.out.print("\n");
                            break;
                        }

                        //exit
                        case 0 : {
                            System.out.println("Bye!");
                            break;
                        }


                        //error message
                        default : {
                            System.out.println("Incorrect option! Try again.");
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Problems with reading the input");
            }

        }
    }

    //inverted index
    private Map<String, List<Integer>> invIndex = new HashMap<>();

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
    }

    private void createInvertedIndex(List<String[]> people) {

        for (int i = 0; i < people.size(); i++) {
            for (int j = 0; j < people.get(i).length; j++) {
                if (invIndex.containsKey(people.get(i)[j].toLowerCase())) {
                    //add the entry identifier, if this entry
                    //contains the name/email in the String
                    invIndex.get(people.get(i)[j].toLowerCase())
                            .add(i);
                } else {
                    //create an index with the name/email in the String
                    //and add the first entry identifier that corresponds
                    invIndex.put(people.get(i)[j].toLowerCase(),
                            new ArrayList<>(List.of(i)));
                }
            }
        }
    }

    private void getMatchByID(String word, List<String[]> people) {

        if (invIndex.containsKey(word.toLowerCase())) {
            System.out.println(invIndex.get(word.toLowerCase()).size() + " persons found:\n");

            for (int i = 0; i < invIndex.get(word.toLowerCase()).size(); i++) {

                System.out.println(this.toString(people
                        .get(invIndex
                        .get(word.toLowerCase())
                        .get(i)
                )));
            }
        }
        else {
            System.out.println("No matching people found.");
        }
    }

    private String toString(String[] people) {
        String toPrint = "";
        for (int i = 0; i < people.length; i++) {
            toPrint += people[i] + " ";
        }
        toPrint = toPrint.trim();
        return toPrint;
    }
}