package ru.ilya.myProject;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Calc {
    private static RomanNumbConvertor romanNumbConvertor = new RomanNumbConvertor();
    private static int intFirstNumb;
    private static int intSecondNumb;
    private static String result;
    private static String firstNumb;
    private static String secondNumb;
    private static String operation;

    public static void main(String[] args) {
        System.out.println("Пожалуйста, введите свое выражение:" +
                "\n" +
                "(Выражение вводится в формате: 1 + 2/I + IV)");
        Scanner scanner = new Scanner(System.in);
        calc(scanner.nextLine());
    }

    public static String calc(String input){
        String[] strings = input.split(" ");

        if (strings.length > 3){
            try {
                throw new Exception("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

        } else if (strings.length < 3){
            try {
                throw new Exception("строка не является математической операцией");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        firstNumb = strings[0];
        operation = strings[1];
        secondNumb = strings[2];

        boolean isRoman = romanNumbConvertor.isRoman(firstNumb, secondNumb);

        if (isRoman){
            intFirstNumb = romanNumbConvertor.convertInRomanToArab(firstNumb);
            intSecondNumb = romanNumbConvertor.convertInRomanToArab(secondNumb);

        } else if (!isRoman){
            if ((Integer.parseInt(firstNumb) <= 10 && (Integer.parseInt(firstNumb) >= 1)
                    && (Integer.parseInt(secondNumb) <= 10 && Integer.parseInt(secondNumb) >= 1))){
                intFirstNumb = Integer.parseInt(firstNumb);
                intSecondNumb = Integer.parseInt(secondNumb);
            } else {
                try {
                    throw new Exception("Чилса можно использовать в диапозоне от 1 до 10!");
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }

        }



        result = new DoCalc().doCalc(isRoman, operation, intFirstNumb, intSecondNumb);
        System.out.println(result);
        return result;
    }
}