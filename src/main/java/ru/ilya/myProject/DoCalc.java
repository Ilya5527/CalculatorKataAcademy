package ru.ilya.myProject;

public class DoCalc {
    public String doCalc(boolean isRoman, String operation, int firstNumb, int secondNumb){
        int number;

        if (operation.equals("+")){
            number = firstNumb + secondNumb;
        } else if (operation.equals("-")) {
            number = firstNumb - secondNumb;
        } else if (operation.equals("*")) {
            number = firstNumb * secondNumb;
        } else if (operation.equals("/")) {
            number = firstNumb / secondNumb;
        } else {
            throw new RuntimeException("Вводить можно только следующие операторы: +, -, *, /\nПожалуйста, введите именно их!");
        }

        if (isRoman){
            return new RomanNumbConvertor().convertInArabToRoman(number);
        } else {
            return String.valueOf(number);
        }
    }
}