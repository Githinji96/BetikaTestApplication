package com.randompackage;

public class GenerateRandomData {


    public static String randomName(int size) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(alphabet.charAt((int) Math.floor(Math.random() * alphabet.length())));
        }
        return sb.toString();
    }

    //generate a random unique number
    public static String randomNum() {
        StringBuilder sb = new StringBuilder("07");
        for (int i = 0; i < 8; i++) {
            sb.append((int) Math.floor(Math.random() * 9));
        }

        return sb.toString();
    }
}
