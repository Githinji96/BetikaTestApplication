package com.randompackage;

public class GenerateRandomData {


    public static String randomName(int size) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        String randName = null;
        for (int i = 0; i < size; i++) {
            randName = String.valueOf(sb.append(alphabet.charAt((int) Math.floor(Math.random() * alphabet.length()))));

        }
        System.out.println("The password generated is "+ randName);
        return sb.toString();
    }

    //generate a random unique number
    public static String randomNum() {
        StringBuilder sb = new StringBuilder("07");
        String num = null;
        for (int i = 0; i < 8; i++) {
            num = String.valueOf(sb.append((int) Math.floor(Math.random() * 9)));
        }
        System.out.println("The phone number generated is " +num);

        return sb.toString();
    }
}
