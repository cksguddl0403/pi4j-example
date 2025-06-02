package com.pi4j.example;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.util.Console;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MinimalExample {

    private static final int PIN_LED = 22; // GPIO BCM 22
    private static final int UNIT_DURATION_MS = 250;

    private static final Map<Character, String> MORSE_CODE_MAP = new HashMap<>();

    static {
        // 대문자
        MORSE_CODE_MAP.put('A', ".-");    MORSE_CODE_MAP.put('B', "-...");
        MORSE_CODE_MAP.put('C', "-.-.");  MORSE_CODE_MAP.put('D', "-..");
        MORSE_CODE_MAP.put('E', ".");     MORSE_CODE_MAP.put('F', "..-.");
        MORSE_CODE_MAP.put('G', "--.");   MORSE_CODE_MAP.put('H', "....");
        MORSE_CODE_MAP.put('I', "..");    MORSE_CODE_MAP.put('J', ".---");
        MORSE_CODE_MAP.put('K', "-.-");   MORSE_CODE_MAP.put('L', ".-..");
        MORSE_CODE_MAP.put('M', "--");    MORSE_CODE_MAP.put('N', "-.");
        MORSE_CODE_MAP.put('O', "---");   MORSE_CODE_MAP.put('P', ".--.");
        MORSE_CODE_MAP.put('Q', "--.-");  MORSE_CODE_MAP.put('R', ".-.");
        MORSE_CODE_MAP.put('S', "...");   MORSE_CODE_MAP.put('T', "-");
        MORSE_CODE_MAP.put('U', "..-");   MORSE_CODE_MAP.put('V', "...-");
        MORSE_CODE_MAP.put('W', ".--");   MORSE_CODE_MAP.put('X', "-..-");
        MORSE_CODE_MAP.put('Y', "-.--");  MORSE_CODE_MAP.put('Z', "--..");

        // 소문자
        MORSE_CODE_MAP.put('a', ".-");    MORSE_CODE_MAP.put('b', "-...");
        MORSE_CODE_MAP.put('c', "-.-.");  MORSE_CODE_MAP.put('d', "-..");
        MORSE_CODE_MAP.put('e', ".");     MORSE_CODE_MAP.put('f', "..-.");
        MORSE_CODE_MAP.put('g', "--.");   MORSE_CODE_MAP.put('h', "....");
        MORSE_CODE_MAP.put('i', "..");    MORSE_CODE_MAP.put('j', ".---");
        MORSE_CODE_MAP.put('k', "-.-");   MORSE_CODE_MAP.put('l', ".-..");
        MORSE_CODE_MAP.put('m', "--");    MORSE_CODE_MAP.put('n', "-.");
        MORSE_CODE_MAP.put('o', "---");   MORSE_CODE_MAP.put('p', ".--.");
        MORSE_CODE_MAP.put('q', "--.-");  MORSE_CODE_MAP.put('r', ".-.");
        MORSE_CODE_MAP.put('s', "...");   MORSE_CODE_MAP.put('t', "-");
        MORSE_CODE_MAP.put('u', "..-");   MORSE_CODE_MAP.put('v', "...-");
        MORSE_CODE_MAP.put('w', ".--");   MORSE_CODE_MAP.put('x', "-..-");
        MORSE_CODE_MAP.put('y', "-.--");  MORSE_CODE_MAP.put('z', "--..");

        // 숫자
        MORSE_CODE_MAP.put('1', ".----"); MORSE_CODE_MAP.put('2', "..---");
        MORSE_CODE_MAP.put('3', "...--"); MORSE_CODE_MAP.put('4', "....-");
        MORSE_CODE_MAP.put('5', "....."); MORSE_CODE_MAP.put('6', "-....");
        MORSE_CODE_MAP.put('7', "--..."); MORSE_CODE_MAP.put('8', "---..");
        MORSE_CODE_MAP.put('9', "----."); MORSE_CODE_MAP.put('0', "-----");

        // 공백은 단어 구분
        MORSE_CODE_MAP.put(' ', " ");
    }

    public static void main(String[] args) throws Exception {
        final var console = new Console();
        console.title("Pi4J :: Morse Code Input Example");

        Context pi4j = Pi4J.newAutoContext();
        var led = pi4j.digitalOutput().create(PIN_LED);

        Scanner scanner = new Scanner(System.in);
        console.println("입력한 문장을 모스 부호로 전송합니다. (exit 입력 시 종료)");

        while (true) {
            System.out.print("\n입력 > ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            for (char c : input.toCharArray()) {
                if (!MORSE_CODE_MAP.containsKey(c)) {
                    console.println("지원하지 않는 문자: '" + c + "'");
                    continue;
                }

                String morse = MORSE_CODE_MAP.get(c);
                if (morse.equals(" ")) {
                    Thread.sleep(UNIT_DURATION_MS * 7); // 단어 간 간격
                    continue;
                }

                console.println("'" + c + "' → " + morse);
                for (char symbol : morse.toCharArray()) {
                    if (symbol == '.') {
                        blinkDot(led);
                    } else if (symbol == '-') {
                        blinkDash(led);
                    }
                    Thread.sleep(UNIT_DURATION_MS); // dot/dash 간 간격
                }
                Thread.sleep(UNIT_DURATION_MS * 2); // 문자 간 간격
            }
        }

        pi4j.shutdown();
        scanner.close();
    }

    private static void blinkDot(DigitalOutput led) throws InterruptedException {
        led.high();
        Thread.sleep(UNIT_DURATION_MS);
        led.low();
    }

    private static void blinkDash(DigitalOutput led) throws InterruptedException {
        led.high();
        Thread.sleep(UNIT_DURATION_MS * 3);
        led.low();
    }
}