package readability;

import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        var sentences = sc.nextLine().split("[.?!]");
        var count = Arrays.stream(sentences).map(e -> e.split(" ").length).count();
        var sum = Arrays.stream(sentences).map(e -> e.split(" ").length).reduce(0, Integer::sum);
        if (sum / count > 10) {
            System.out.println("HARD");
        } else {
            System.out.println("EASY");
        }
    }
}
