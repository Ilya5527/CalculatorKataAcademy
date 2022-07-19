package ru.ilya.myProject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RomanNumbConvertor {

    private static String result;
    private static boolean isRomanNumb = true;

    static Map<String, Integer> roman = new HashMap<>() {{
        put("I", 1);put("XXI", 21);put("XLI", 41);put("LXI", 61);put("LXXXI", 81);
        put("II", 2);put("XXII", 22);put("XLII", 42);put("LXII", 62);put("LXXXII", 82);
        put("III", 3);put("XXIII", 23);put("XLIII", 43);put("LXIII", 63);put("LXXXIII", 83);
        put("IV", 4);put("XXIV", 24);put("XLIV", 44);put("LXIV", 64);put("LXXXIV", 84);
        put("V", 5);put("XXV", 25);put("XLV", 45);put("LXV", 65);put("LXXXV", 85);
        put("VI", 6);put("XXVI", 26);put("XLVI", 46);put("LXVI", 66);put("LXXXVI", 86);
        put("VII", 7);put("XXVII", 27);put("XLVII", 47);put("LXVII", 67);put("LXXXVII", 87);
        put("VIII", 8);put("XXVIII", 28);put("XLVIII", 48);put("LXVIII", 68);put("LXXXVIII", 88);
        put("IX", 9);put("XXIX", 29);put("XLIX", 49);put("LXIX", 69);put("LXXXIX", 89);
        put("X", 10);put("XXX", 30);put("L", 50);put("LXX", 70);put("XC", 90);
        put("XI", 11);put("XXXI", 31);put("LI", 51);put("LXXI", 71);put("XCI", 91);
        put("XII", 12);put("XXXII", 32);put("LII", 52);put("LXXII", 72);put("XCII", 92);
        put("XIII", 13);put("XXXIII", 33);put("LIII", 53);put("LXXIII", 73);put("XCIII", 93);
        put("XIV", 14);put("XXXIV", 34);put("LIV", 54);put("LXXIV", 74);put("XCIV", 94);
        put("XV", 15);put("XXXV", 35);put("LV", 55);put("LXXV", 75);put("XCV", 95);
        put("XVI", 16);put("XXXVI", 3);put("LVI", 56);put("LXXVI", 76);put("XCVI", 96);
        put("XVII", 17);put("XXXVII", 37);put("LVII", 57);put("LXXVII", 77);put("XCVII", 97);
        put("XVIII", 18);put("XXXVIII", 38);put("LVIII", 58);put("LXXVIII", 78);put("XCVIII", 98);
        put("XIX", 19);put("XXXIX", 39);put("LIX", 59);put("LXXIX", 79);put("XCIX", 99);
        put("XX", 20);put("XL", 40);put("LX", 60);put("LXXX", 80);put("C", 100);
    }};

    public boolean isRoman(String firstNumb, String secondNumb){

        if (roman.get(firstNumb) == null && roman.get(secondNumb) == null){
            isRomanNumb = false;
        } else if (roman.get(firstNumb) == null || roman.get(secondNumb) == null){
            try {
                throw new Exception("используются одновременно разные системы счисления");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return isRomanNumb;
    }

    public int convertInRomanToArab(String numbOfConvert){
        return roman.get(numbOfConvert);
    }

    public String convertInArabToRoman(Integer numbOfConvert){

        if (numbOfConvert < 1){
            try {
                throw new Exception("в римской системе нет отрицательных чисел");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            Iterator it = roman.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                if (pair.getValue() == numbOfConvert) {
                    result = (String)pair.getKey();
                }
                it.remove(); // дабы не было ConcurrentModificationException
            }
        }

        if (result == null){
            try {
                throw new Exception("Чилса можно использовать в диапозоне от I до X!");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return result;
    }
}