import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;


public class testKaAc {
    static Scanner console = new Scanner(System.in);
    static boolean normalMode = false;

    public static void main (String[] args) {
        System.out.println("Введите арабские или римские цифры ");
        while(true) {
            System.out.print("Выражение: ");
            String uInput = console.nextLine();
            if(uInput.contains("stop"))
            {
                System.out.println("Выход из приложения...");
                System.exit(0);
            }
            if(uInput.contains("normal"))
            {
                normalMode = true;
                System.out.println("Ограничения отключены");
            }
            else System.out.println(calc(uInput));
        }
    }


    public static String calc(String input) {
        char n = '+';
        int result;
        char[] uChar = new char[10];
        for (int i = 0; i < input.length(); i++) {
            if (i >= uChar.length) break;
            uChar[i] = input.charAt(i);
            if (uChar[i] == '+') n = '+';
            else if (uChar[i] == '-') n = '-';
            else if (uChar[i] == '*') n = '*';
            else if (uChar[i] == '/') n = '/';
        }
        String uCharString = String.valueOf(uChar).toUpperCase();
        String[] numbs = uCharString.split("[+-/*]");
        if(numbs.length > 2) throw new RuntimeException("Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        if(numbs.length < 2) throw new RuntimeException("Cтрока не является математической операцией");
        String tmpNum1 = numbs[0].trim();
        String tmpNum2 = numbs[1].trim();
        int romanNum1 = romanToArabic(tmpNum1);
        int romanNum2 = romanToArabic(tmpNum2);
        if((romanNum1 > 10 || romanNum2 > 10) && !normalMode) throw new RuntimeException("Доступны операции с числами от 1 до 10");
        if(!(romanNum1 < 0 && romanNum2 < 0))
        {
            if(romanNum1 < 0 || romanNum2 < 0) throw new RuntimeException("Используются одновременно разные системы счисления");
            result = Calculate(romanNum1, romanNum2, n);
            if(result < 1) throw new RuntimeException("Недопустимый результат в выбранной системе счисления");
            return "Результат: " + arabicToRoman(result);
        }
        int arabicNum2 = parseInt(tmpNum2);
        int arabicNum1 = parseInt(tmpNum1);
        if((arabicNum1 > 10 || arabicNum2 > 10) && !normalMode) throw new RuntimeException("Доступны операции с числами от 1 до 10");
        result = Calculate(arabicNum1, arabicNum2, n);
        return "Результат: " + result;
    }

    private static int Calculate(int num1, int num2, char opera)
    {
        int result;
        switch (opera) {
            case '+' -> result = num1 + num2;
            case '-' -> result = num1 - num2;
            case '*' -> result = num1 * num2;
            case '/' -> {
                try {
                    result = num1 / num2;
                } catch (Exception e) {
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

        int i = 0;
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