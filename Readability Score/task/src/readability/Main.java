package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public static int syllableCount(String s) {
        Pattern p = Pattern.compile("[aeiouy]+[^$e(,.:;!?)]");
        Matcher m = p.matcher(s);
        int syllables = 0;
        while (m.find()) {
            syllables += 1;
        }
        return syllables;
    }

    public static int polysyllableCount(String s) {
        return (int) Arrays.stream(s.split(" ")).filter(e -> syllableCount(e) > 2).count();
    }

    public static double charScore_ARI(int chars, int words, int sentences) {
        return 4.71 * (double)chars / words + 0.5 * (double)words / sentences - 21.43;
    }

    public static double charScore_Flesch_Kincaid(int words, int sentences, int syllables) {
        return 0.39 * (double) words / sentences + 11.8 * (double) syllables / words - 15.59;
    }

    public static double charScore_SMOG(int sentences, int polysyllables) {
        return 1.043 * Math.sqrt((double) polysyllables * 30 / (double) sentences) + 3.1291;
    }

    public static double charScore_Coleman_Liau(int chars, int words, int sentences) {
        return 0.0588 * (double) chars / words * 100 - 0.296 * (double) sentences / words * 100 - 15.8;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Map<Integer, String> score_age = new HashMap<>();
        score_age.put(1, "5-6"); score_age.put(2, "6-7"); score_age.put(3, "7-9");
        score_age.put(4, "9-10"); score_age.put(5, "10-11"); score_age.put(6, "11-12");
        score_age.put(7, "12-13"); score_age.put(8, "13-14"); score_age.put(9, "14-15");
        score_age.put(10, "15-16"); score_age.put(11, "16-17"); score_age.put(12, "17-18");
        score_age.put(13, "18-24"); score_age.put(14, "24+");

        String fpara = args[0];
        String file = readFile(fpara);
        System.out.println("The text is:");
        System.out.println(file);
        System.out.println();
        int words = wordCount(file), sentences = sentenceCount(file), chars = charCount(file), syllables = syllableCount(file), polysyllables = polysyllableCount(file);
        System.out.println("Words: " + words);
        System.out.println("Sentences: " + sentences);
        System.out.println("Characters: " + chars);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + polysyllables);
        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        var ans = sc.nextLine();
        switch (ans) {
            case "ARI": {
                var score = charScore_ARI(chars, words, sentences);
                System.out.printf("The score is: %.2f\n", score);
                System.out.println("This text should be understood by " + score_age.get((int)Math.ceil(score)) + " year olds");
                break;
            }
            case "FK": {
                var score = charScore_Flesch_Kincaid(words, sentences, syllables);
                System.out.printf("The score is: %.2f\n", score);
                System.out.println("This text should be understood by " + score_age.get((int)Math.ceil(score)) + " year olds");
                break;
            }
            case "SMOG": {
                var score = charScore_SMOG(sentences, polysyllables);
                System.out.printf("The score is: %.2f\n", score);
                System.out.println("This text should be understood by " + score_age.get((int)Math.ceil(score)) + " year olds");
                break;
            }
            case "CL": {
                var score = charScore_Coleman_Liau(chars, words, sentences);
                System.out.printf("The score is: %.2f\n", score);
                System.out.println("This text should be understood by " + score_age.get((int)Math.ceil(score)) + " year olds");
                break;
            }
            case "all": {
                var scores = List.of(
                        charScore_ARI(chars, words, sentences), charScore_Flesch_Kincaid(words, sentences, syllables),
                        charScore_SMOG(sentences, polysyllables), charScore_Coleman_Liau(chars, words, sentences)
                );
                System.out.println("Automated Readability Index: "+ scores.get(0) +" (about "+ score_age.get((int)Math.ceil(scores.get(0))) +" year olds).");
                System.out.println("Flesch–Kincaid readability tests: "+ scores.get(1) +" (about "+ score_age.get((int)Math.ceil(scores.get(1))) +" year olds).");
                System.out.println("Simple Measure of Gobbledygook: "+ scores.get(2) +" (about "+ score_age.get((int)Math.ceil(scores.get(2))) +" year olds).");
                System.out.println("Coleman–Liau index: "+ scores.get(3) +" (about "+ score_age.get((int)Math.ceil(scores.get(3))) +" year olds).");
                var cnt = scores.size();
                var sum = scores.stream().reduce(Double::sum).orElse(0.0);
                System.out.println("This text should be understood in average by " + sum / cnt + " year olds.");
                break;
            }
            default: { break; }
        }

    }
}
