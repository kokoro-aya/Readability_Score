package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    public static String readFile(String fname) {
        File f = new File(fname);
        String ret = "";
        try {
            Scanner scn = new Scanner(f);
            while (scn.hasNextLine()) {
                ret += scn.nextLine();
            }
            scn.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }
        return ret;
    }

    public static int wordCount(String s) {
        return (int) Stream.of(s.split(" ")).filter(e -> !(e.equals("!") || e.equals("?") || e.equals(".") || e.equals(","))).count();
    }

    public static int sentenceCount(String s) {
        return (int) Arrays.stream(s.split("[.?!]")).filter(e -> e.length() > 0).count();
    }

    public static int charCount(String s) {
        return (int) Stream.of(s.split("")).filter(e -> !e.isBlank()).count();
    }

    public static double charScore(int chars, int words, int sentences) {
        return 4.71 * (double)chars / words + 0.5 * (double)words / sentences - 21.43;
    }

    public static void main(String[] args) {

        Map<Integer, String> score_age = new HashMap<>();
        score_age.put(1, "5-6"); score_age.put(2, "6-7"); score_age.put(3, "7-9");
        score_age.put(4, "9-10"); score_age.put(5, "10-11"); score_age.put(6, "11-12");
        score_age.put(7, "12-13"); score_age.put(8, "13-14"); score_age.put(9, "14-15");
        score_age.put(10, "15-16"); score_age.put(11, "16-17"); score_age.put(12, "17-18");
        score_age.put(13, "18-24"); score_age.put(14, "24+");

        String fpara = args[0];
        String file = readFile(fpara);
        System.out.println(file);
        System.out.println();
        int words = wordCount(file), sentences = sentenceCount(file), chars = charCount(file);
        System.out.println("Words: " + words);
        System.out.println("Sentences: " + sentences);
        System.out.println("Characters: " + chars);
        var score = charScore(chars, words, sentences);
        System.out.printf("The score is: %.2f\n", score);
        System.out.println("This text should be understood by " + score_age.get((int)Math.ceil(score)) + " year olds");
    }
}
