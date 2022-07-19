package ru.ilya.myProject;

public class DoCalc {
    private static String result;
    private static int number;
    public String doCalc(boolean isRoman, String operation, int firstNumb, int secondNumb){

        if (operation.equals("+")){
            number = firstNumb + secondNumb;
        } else if (operation.equals("-")) {
            number = firstNumb - secondNumb;
        } else if (operation.equals("*")) {
            number = firstNumb * secondNumb;
        } else if (operation.equals("/")) {
            number = firstNumb / secondNumb;
        } else {
            try {
                throw new Exception("Вводить можно только следующие операторы: +, -, *, /" +
                        "\n" +
                        "Пожалуйста, введите именно их!");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        if (isRoman == true){
            result = new RomanNumbConvertor().convertInArabToRoman(number);
        } else {
            result = String.valueOf(number);
        }

        return result;
    }
}