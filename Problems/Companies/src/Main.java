import java.util.*;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> list = new ArrayList<String>();

        while(scanner.hasNext()) {
           list.add(scanner.next());
        }

        System.out.println(list);
    }
}