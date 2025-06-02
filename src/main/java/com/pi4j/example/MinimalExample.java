package com.pi4j.example;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.util.Console;

import java.util.HashMap;
import java.util.Map;

public class MinimalExample {

    private static final int PIN_LED = 22; // GPIO BCM 22 (PIN 15)
    private static final int UNIT_DURATION_MS = 250;

    private static final Map<Character, String> MORSE_CODE_MAP = new HashMap<>();

    static {
        MORSE_CODE_MAP.put('A', ".-");
        MORSE_CODE_MAP.put('B', "-...");
        MORSE_CODE_MAP.put('C', "-.-.");
        MORSE_CODE_MAP.put('D', "-..");
        MORSE_CODE_MAP.put('E', ".");
        MORSE_CODE_MAP.put('F', "..-.");
        MORSE_CODE_MAP.put('G', "--.");
        MORSE_CODE_MAP.put('H', "....");
        MORSE_CODE_MAP.put('I', "..");
        MORSE_CODE_MAP.put('J', ".---");
        MORSE_CODE_MAP.put('K', "-.-");
        MORSE_CODE_MAP.put('L', ".-..");
        MORSE_CODE_MAP.put('M', "--");
        MORSE_CODE_MAP.put('N', "-.");
        MORSE_CODE_MAP.put('O', "---");
        MORSE_CODE_MAP.put('P', ".--.");
        MORSE_CODE_MAP.put('Q', "--.-");
        MORSE_CODE_MAP.put('R', ".-.");
        MORSE_CODE_MAP.put('S', "...");
        MORSE_CODE_MAP.put('T', "-");
        MORSE_CODE_MAP.put('U', "..-");
        MORSE_CODE_MAP.put('V', "...-");
        MORSE_CODE_MAP.put('W', ".--");
        MORSE_CODE_MAP.put('X', "-..-");
        MORSE_CODE_MAP.put('Y', "-.--");
        MORSE_CODE_MAP.put('Z', "--..");

        MORSE_CODE_MAP.put('1', ".----");
        MORSE_CODE_MAP.put('2', "..---");
        MORSE_CODE_MAP.put('3', "...--");
        MORSE_CODE_MAP.put('4', "....-");
        MORSE_CODE_MAP.put('5', ".....");
        MORSE_CODE_MAP.put('6', "-....");
        MORSE_CODE_MAP.put('7', "--...");
        MORSE_CODE_MAP.put('8', "---..");
        MORSE_CODE_MAP.put('9', "----.");
        MORSE_CODE_MAP.put('0', "-----");

        MORSE_CODE_MAP.put(' ', " "); // Word separator
    }

    public static void main(String[] args) throws Exception {
        final var console = new Console();
        console.title("Pi4J :: Morse Code Example");

        Context pi4j = Pi4J.newAutoContext();
        var led = pi4j.digitalOutput().create(PIN_LED);

        String message = "HELLO RASPBERRY PI";
        console.println("Sending message in Morse code: " + message);

        for (char c : message.toUpperCase().toCharArray()) {
            String morse = MORSE_CODE_MAP.getOrDefault(c, "");
            if (morse.equals(" ")) {
                Thread.sleep(UNIT_DURATION_MS * 7); // Word gap
                continue;
            }

            for (char symbol : morse.toCharArray()) {
                if (symbol == '.') {
                    blinkDot(led);
                } else if (symbol == '-') {
                    blinkDash(led);
                }
                Thread.sleep(UNIT_DURATION_MS); // Gap between dots/dashes
            }
            Thread.sleep(UNIT_DURATION_MS * 2); // Already waited 1 unit above; total 3-unit gap between letters
        }

        pi4j.shutdown();
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