package search;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        List<String[]> people = new ArrayList<>();
        String[] words ;
        String strategy = " ";
        int menuNo = 1;
        Main obj = new Main();
        Strategy strategyObj;
        ContextStrategy context = new ContextStrategy();

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
                            System.out.println("\nSelect a matching strategy: ALL, ANY, NONE");

                            if (scanner.hasNextLine()) {
                                strategy = scanner.nextLine().trim();
                            }
                            switch (strategy) {
                                case "ANY" : {
                                    strategyObj = new ANYStrategy();
                                    break;
                                }

                                case "ALL" : {
                                    strategyObj = new ALLStrategy();
                                    break;
                                }

                                case "NONE" : {
                                    strategyObj = new NONEStrategy();
                                    break;
                                }

                                default : {
                                    strategyObj = new ANYStrategy();
                                    break;
                                }
                            }

                            System.out.println("");
                            System.out.println("Enter a name or email to search all suitable people.");

                            if (scanner.hasNextLine()) {
                                words = scanner.nextLine().toLowerCase().trim().split(" ");
                            }
                            else
                            {
                                words = new String[0];
                            }

                            //use the inverted index search to find the
                            //information about the required person
                            //obj.getMatchByID(word, people);
                            context.setStrategy(strategyObj);
                            context.invoke(words, people, obj.getInvIndex());
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

    public Map<String, List<Integer>> getInvIndex() {
        return invIndex;
    }

    //inverted index
    private Map<String, List<Integer>> invIndex = new HashMap<>();

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
                            new ArrayList<>(Arrays.asList(i)));
                }
            }
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
};

abstract class Strategy {
    abstract void execute(String[] words, List<String[]> people, Map<String, List<Integer>> invIndex);

    String toString(String[] people) {
        String toPrint = "";
        for (int i = 0; i < people.length; i++) {
            toPrint += people[i] + " ";
        }
        toPrint = toPrint.trim();
        return toPrint;
    }
}

class ContextStrategy {
    private Strategy strategy;

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void invoke(String[] words, List<String[]> people, Map<String, List<Integer>> invIndex){
        this.strategy.execute(words, people, invIndex);
    }
}

class ANYStrategy extends Strategy {

    @Override
    public void execute(String[] words, List<String[]> people, Map<String, List<Integer>> invIndex) {
        int nrPersonsFound = 0;
        List<String[]> peopleOut = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            if (invIndex.containsKey(words[i])) {
                for (int j = 0; j < invIndex.get(words[i]).size(); j++) {
                    if (!peopleOut.contains(people
                            .get(invIndex
                                    .get(words[i])
                                    .get(j)
                            )))  {
                        peopleOut.add(people
                                .get(invIndex
                                        .get(words[i])
                                        .get(j)
                                ));
                        nrPersonsFound++;
                    }
                }
            }
        }

        if(nrPersonsFound > 0) {
            System.out.println(nrPersonsFound + " persons found:\n");
            for (String[] person : peopleOut) {
                System.out.println(super.toString(person));
            }
        }
        else {
            System.out.println("No matching people found.");
        }
    }
}

class ALLStrategy extends Strategy {

    @Override
    public void execute(String[] words, List<String[]> people, Map<String, List<Integer>> invIndex) {
        int nrPersonsFound = 0;
        Map<Integer, Integer> candidateIndexes = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            if (invIndex.containsKey(words[i])) {

                for (Integer candidate : invIndex.get(words[i])) {
                    if(candidateIndexes.containsKey(candidate)) {
                        Integer appearences = candidateIndexes.get(candidate);
                        appearences++;
                        candidateIndexes.replace(candidate, appearences);
                    }
                    else
                    {
                        candidateIndexes.put(candidate, 1);
                    }
                }
            }
        }

        if(candidateIndexes.containsValue(words.length)) {
            for (Integer person : candidateIndexes.values()){
                if(person == words.length) {
                    nrPersonsFound++;
                }
            }
            System.out.println(nrPersonsFound + " persons found:\n");
            for (Map.Entry<Integer, Integer> person : candidateIndexes.entrySet()){
                if(person.getValue() == words.length) {
                    System.out.println(super.toString(people
                                    .get(person.getKey()
                            )));
                }
            }
        }
        else {
            System.out.println("No matching people found.");
        }
    }
}

class NONEStrategy extends Strategy {

    @Override
    public void execute(String[] words, List<String[]> people, Map<String, List<Integer>> invIndex) {
        int nrPersonsFound = 0;
        int nrPeople = 0;
        Map<Integer, Integer> candidateIndexes = new HashMap<>();
        for (String[] person : people) {
            candidateIndexes.put(nrPeople, 0);
            nrPeople++;
        }
        for (int i = 0; i < words.length; i++) {
            if (invIndex.containsKey(words[i])) {

                for (Integer candidate : invIndex.get(words[i])) {
                    if(candidateIndexes.containsKey(candidate)) {
                        Integer appearences = candidateIndexes.get(candidate);
                        appearences++;
                        candidateIndexes.replace(candidate, appearences);
                    }
                }
            }
        }

        if(candidateIndexes.containsValue(0)) {
            for (Integer person : candidateIndexes.values()){
                if(person == 0) {
                    nrPersonsFound++;
                }
            }
            System.out.println(nrPersonsFound + " persons found:\n");
            for (Map.Entry<Integer, Integer> person : candidateIndexes.entrySet()) {
                if(person.getValue() == 0) {
                    System.out.println(super.toString(people
                            .get(person.getKey()
                            )));
                }
            }
        }
        else {
            System.out.println("No matching people found.");
        }
    }
}