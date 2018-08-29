import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntConsumer;

public class main {
    static int[] arr = {1,3,4,5,6,7,8,9,10};
    public static void main(String[] args) {
//        List<Integer> numbers = Arrays.asList(1,2,3,4,5,6);
//        numbers.forEach((Integer value)-> System.out.println(value));
//
//        final int num = 2;
//        Function<Integer, Integer> stringConverter = (from)->from * num;
//        System.out.println(stringConverter.apply(3));
//
        List<User> users = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            users.add(new User(i, "billy" + Integer.toString(i)));
        }
        users.stream().map(User::getName).forEach(System.out::print);
        //bad method ref
//        List<Double> numbers = new ArrayList<>();
//        for (int i = 1; i < 10; i++) {
//            numbers.add(Double.valueOf(i));
//        }
//        numbers.stream().map(Double::toString).forEach(System.out::println);

        Arrays.stream(arr).forEach(value -> System.out.println(value));
    }

    public static class User {
        int id;
        String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public User(int i, String s) {
            this.id = i;
            this.name = s;
        }
    }
}
