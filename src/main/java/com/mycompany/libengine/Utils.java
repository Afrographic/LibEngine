/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.libengine;

/**
 *
 * @author X M G
 */
public class Utils {

    public static String formatString(String str) {
        str = str.replaceAll("\"", "");

        String words[] = str.split("\\s");
        String capitalizeStr = "";

        if (words.length >= 2) {

            for (String word : words) {
                if (word.length() >= 3) {
                    // Capitalize first letter
                    String firstLetter = word.substring(0, 1);
                    // Get remaining letter
                    String remainingLetters = word.substring(1).toLowerCase();
                    capitalizeStr += firstLetter.toUpperCase() + remainingLetters + " ";
                }
            }
            System.out.println(capitalizeStr);
            return capitalizeStr;
        } else {
            // Capitalize first letter
            String firstLetter = str.substring(0, 1);
            // Get remaining letter
            String remainingLetters = str.substring(1).toLowerCase();
            capitalizeStr += firstLetter.toUpperCase() + remainingLetters + " ";
            return capitalizeStr;
        }

    }

}
