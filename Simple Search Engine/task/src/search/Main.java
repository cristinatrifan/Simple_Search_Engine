package search;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<String> people = new ArrayList<>();
        String word = " ";
        int noPeople = 0;
        int menuNo = 1;
        Main obj = new Main();

        if (Arrays.asList(args).contains("--data")) {
            File file = new File(Arrays.asList(args)
                    .get(Arrays.asList(args)
                            .indexOf("--data") + 1));

            //get all elements from the file
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    people.add(scanner.nextLine());
                    noPeople--;
                }
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
                    if (menuNo == 1) {
                        System.out.println("");
                        System.out.println("Enter a name or email to search all suitable people.");

                        if (scanner.hasNextLine()) {
                            word = scanner.nextLine().trim();
                        }
                        obj.getMatch(people, word);
                    }

                    //print the list of people
                    else if (menuNo == 2) {
                        System.out.println("\n=== List of people ===");
                        for (int i = 0; i < people.size(); i++) {
                            System.out.println(people.get(i));
                        }
                    }

                    //exit
                    else if (menuNo == 0) {
                        System.out.println("Bye!");
                    }

                    //error message
                    else {
                        System.out.println("Incorrect option! Try again.");
                    }
                }
            } catch (Exception e) {
                System.out.println("Problems with reading the input");
            }

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
