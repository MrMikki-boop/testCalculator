import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;


public class testKaAc {
    static Scanner console = new Scanner(System.in); // Ввод любых данных
    static boolean normalMode = false; // Мод с ограничениями

    public static void main (String[] args) {
        System.out.println("Введите арабские или римские цифры ");
        while(true) {
            System.out.print("Выражение: ");
            String uInput = console.nextLine(); // Ввод для String-данных
            if(uInput.contains("stop")) // Если консоль написано это, то
            {
                System.out.println("Выход из приложения...");
                System.exit(0); // Функцию выхода
            }
            if(uInput.contains("cheatMod"))
            {
                normalMode = true;
                System.out.println("Ограничения отключены");
            }
            else System.out.println(calc(uInput));
        }
    }


    public static String calc(String input) {
        var n = '+'; // Инициализация переменной со значением '+'
        int result; // Объявление переменной
        char[] uChar = new char[10]; // Создание массива с размером 10
        for (int i = 0; i < input.length(); i++) { // Цикл for. Будет выполняться, пока переменная i меньше длины строки input.
            if (i >= uChar.length) break; // Если значение переменной i больше или равно длине массива uChar, то цикл прерывается
            uChar[i] = input.charAt(i); // Присваивание элементу массива uChar с индексом i значения символа строки input с индексом i.
            if (uChar[i] == '+') n = '+';     // Если элемент массива i = '+', то перменной n присваивем '+'
            else if (uChar[i] == '-') n = '-';
            else if (uChar[i] == '*') n = '*';
            else if (uChar[i] == '/') n = '/';
        }
        String uCharString = String.valueOf(uChar).toUpperCase(); // переводим массив uChar в верхний регистр
        String[] numbs = uCharString.split("[+-/*]"); //Эта строка кода разбивает строку uCharString на подстроки, используя символы "+", "-", "*", "/" в качестве разделителей. Результатом является массив строк numbs, который содержит все подстроки, полученные в результате разбиения.
        if(numbs.length > 2) throw new RuntimeException("Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)"); // Если больше мат.операций
        if(numbs.length < 2) throw new RuntimeException("Cтрока не является математической операцией"); // Если меньше
        String tmpNum1 = numbs[0].trim(); // Массиво строки обрезается с обоих сторон от лишних пробелов
        String tmpNum2 = numbs[1].trim();
        int romanNum1 = romanToArabic(tmpNum1); // Присваивание tmpNum1 объект romanToArabic и называем это всё romanNum1
        int romanNum2 = romanToArabic(tmpNum2);
        if((romanNum1 > 10 || romanNum2 > 10) && !normalMode) throw new RuntimeException("Доступны операции с числами от 1 до 10"); // Если первое или второе число > 10 и normalMode == false, то пишем исключени
        if(!(romanNum1 < 0 && romanNum2 < 0)) //
        {
            if(romanNum1 < 0 || romanNum2 < 0) throw new RuntimeException("Используются одновременно разные системы счисления");
            result = Calculate(romanNum1, romanNum2, n);
            if(result < 1) throw new RuntimeException("Недопустимый результат в выбранной системе счисления");
            return "Результат: " + arabicToRoman(result) + " (Или же: " + romanToArabic(arabicToRoman(result)) + ")";
        }
        int arabicNum2 = parseInt(tmpNum2); // данная строка кода может использоваться для преобразования числовых значений, которые были введены пользователем в виде строк, в целочисленный формат для дальнейшей обработки программой.
        int arabicNum1 = parseInt(tmpNum1);
        if((arabicNum1 > 10 || arabicNum2 > 10) && !normalMode) throw new RuntimeException("Доступны операции с числами от 1 до 10");
        result = Calculate(arabicNum1, arabicNum2, n);
        return "Результат: " + result + " (Или же: " + arabicToRoman(result) + ")";
    }

    private static int Calculate(int num1, int num2, char opera)
    {
        int result;
        switch (opera) {
            case '+' -> result = num1 + num2; // -> — это лямбда-выражния - анонимная функция. Проще говоря, это метод без объявления
            case '-' -> result = num1 - num2;
            case '*' -> result = num1 * num2;
            case '/' -> {
                try {
                    result = num1 / num2;
                } catch (Exception e) { //  это блок кода, который используется для обработки исключений, которые могут возникнуть при выполнении программы. Если в блоке try происходит ошибка, то управление передается в блок catch, где исключение обрабатывается. Параметр e типа Exception содержит информацию об ошибке, которая может быть использована для ее диагностики и исправления.
                    throw new RuntimeException("Нельзя делить на ноль");
                }
            }
            default -> throw new RuntimeException("Неверная операция");
        }
        return result;
    }




    public static int romanToArabic(String inputArabic) {
        String romanNumeral = inputArabic.toUpperCase();
        int result = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;

        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        if (romanNumeral.length() > 0) result = -1;

        return result;
    }

    public static String arabicToRoman(int number) {
        if ((number <= 0) || (number > 4000)) {
            throw new IllegalArgumentException(number + " вне допустимого диапазона (0,4000]");
        }

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        var i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }





    enum RomanNumeral {
        I(1), IV(4), V(5), IX(9), X(10),
        XL(40), L(50), XC(90), C(100),
        CD(400), D(500), CM(900), M(1000);

        final private int value;

        RomanNumeral(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static List<RomanNumeral> getReverseSortedValues() {
            return Arrays.stream(values())
                    .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                    .collect(Collectors.toList());
        }
    }
}