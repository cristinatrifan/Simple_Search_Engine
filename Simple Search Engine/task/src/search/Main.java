package search;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String[] searchWords = new String[30];
        String word = " ";
        Main obj = new Main();

       try (Scanner scanner = new Scanner(System.in)) {
           if (scanner.hasNextLine()) {
               searchWords = scanner.nextLine().split(" ");
           }
           if (scanner.hasNextLine()) {
               word = scanner.nextLine();
           }
       }
       catch (Exception e) {
           System.out.println("Problems with reading the input");
       }

       System.out.println(obj.getIndexOfWord(searchWords, word));
    }

    private String getIndexOfWord(String[] inputList, String searchedWord) {
        String text = " ";
        for (int i = 0; i < inputList.length; i++) {
            if (inputList[i].equals(searchedWord)) {
                Integer index = i + 1;
                return text = index.toString();
            }
        }
        return text = "Not found";
    }
}
