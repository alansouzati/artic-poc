package br.ufrgs.artic.sectLabel;


import org.apache.commons.lang.StringUtils;

import java.io.Console;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SectLabelToken {

    private final String originalWord;

    public SectLabelToken(String originalWord) {
        this.originalWord = originalWord.trim();
    }

    public static void main(String[] args) {
        System.out.println(new SectLabelToken(" 5400 ").toString());
    }

    @Override
    public String toString() {
        StringBuilder sectLabelToken = new StringBuilder();

        sectLabelToken.append("TOKEN-").append(originalWord).append(" ");
        sectLabelToken.append(originalWord).append(" ");
        sectLabelToken.append(originalWord.replaceAll("[^a-zA-Z0-9]", "")).append(" ");

        boolean isFirstCharUppercase = Character.isUpperCase(originalWord.charAt(0));
        boolean isAllLettersUppercase = StringUtils.isAllUpperCase(originalWord);

        Pattern p = Pattern.compile("(\\p{Punct}$)");
        Matcher m = p.matcher(originalWord);
        boolean endsWithPunctuation = m.find();

        if (isAllLettersUppercase) {
            sectLabelToken.append("A").append(" ");
        } else if (endsWithPunctuation) {
            sectLabelToken.append(originalWord.charAt(originalWord.length() - 1)).append(" ");
        } else {
            sectLabelToken.append("a").append(" ");
        }

        for (int i = 1; i <= 4; i++) {
            int endIndex = Math.min(i, originalWord.length());

            sectLabelToken.append(originalWord.substring(0, endIndex));

            if (endIndex < i) {
                int numberOfPipes = i - endIndex;
                while (numberOfPipes > 0) {
                    sectLabelToken.append("|");
                    numberOfPipes--;
                }
            }

            sectLabelToken.append(" ");
        }


        String reverseWord = new StringBuilder(originalWord).reverse().toString();

        for (int i = 1; i <= 4; i++) {
            int endIndex = Math.min(i, reverseWord.length());

            sectLabelToken.append(new StringBuilder(reverseWord.substring(0, endIndex)).reverse().toString());

            if (endIndex < i) {
                int numberOfPipes = i - endIndex;
                while (numberOfPipes > 0) {
                    sectLabelToken.append("|");
                    numberOfPipes--;
                }
            }

            sectLabelToken.append(" ");
        }

        if (isAllLettersUppercase) {
            sectLabelToken.append("AllCap");
        } else if (isFirstCharUppercase) {
            sectLabelToken.append("InitCap");
        } else {
            sectLabelToken.append("others");
        }

        return sectLabelToken.toString();
    }
}
