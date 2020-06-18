import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> list = new ArrayList<Integer>();
        List<Integer> listFin = new ArrayList<Integer>();

      list = Arrays
              .stream(scanner.nextLine()
              .split("\\s"))
              .mapToInt(Integer::parseInt)
              .collect(ArrayList::new, List::add , List::addAll);

        List<Integer> finalList = list;

        listFin = IntStream
              .range(0, list.size())
              .filter(num -> ( num % 2 ) != 0)
                .map(num2 -> finalList.get(num2))
              .collect(ArrayList::new, List::add , List::addAll);

for (int i = listFin.size() - 1; i >= 0; i--) {
    System.out.print(listFin.get(i) + " ");
}


    }
}