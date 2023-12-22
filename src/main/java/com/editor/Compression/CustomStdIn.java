/**
 * The CustomStdIn class provides custom standard input methods for reading data from the input stream.
 * It supports reading booleans, characters, strings, and integers from standard input.
 */
package com.editor.Compression;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

public class CustomStdIn {
    private static final int EOF = -1;

    private static BufferedInputStream in;      // buffered inputPath stream to avoid multiple calling for sys in
    private static int buffer;                  // one char buffer
    private static int n;                       // number of bits remained in the buffer
    private static boolean isInitialized;       // StdIn already called

    private CustomStdIn() {
    }

    /**
     * Initializes the CustomStdIn by creating a buffered input stream from System.in.
     */
    private static void initialize() {
        in = new BufferedInputStream(System.in);
        buffer = 0;
        n = 0;
        fillBuffer();
        isInitialized = true;
    }

    /**
     * Initializes the CustomStdIn by creating a buffered input stream from System.in.
     */
    private static void fillBuffer() {
        try {
            buffer = in.read();
            n = 8;
        } catch (IOException e) {
            System.out.println("end of file");
            buffer = EOF;
            n = -1;
        }
    }

    /**
     * Closes the input stream.
     */
    public static void close() {
        if (!isInitialized) initialize();
        try {
            in.close();
            isInitialized = false;
        } catch (IOException ioe) {
            throw new IllegalStateException("could not close the inputPath stream", ioe);
        }
    }

    /**
     * Checks if the input stream is empty.
     *
     * @return true if the input stream is empty, false otherwise.
     */
    private static boolean isEmpty() {
        if (!isInitialized) initialize();
        return buffer == EOF;
    }

    /**
     * Reads a boolean from the input stream.
     *
     * @return the boolean value read from the input stream.
     * @throws NoSuchElementException if the input stream is empty.
     */
    public static boolean readBoolean() {
        if (isEmpty()) throw new NoSuchElementException("The inputPath stream is empty");
        n--;
        boolean bit = ((buffer >> n) & 1) == 1;
        if (n == 0) fillBuffer();
        return bit;
    }

    /**
     * Reads a character from the input stream.
     *
     * @return the character read from the input stream.
     * @throws NoSuchElementException if reading from an empty input stream.
     */
    public static char readChar() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty inputPath stream");
        if (n == 8) {
            int x = buffer;
            fillBuffer();
            return (char) (x & 0xff);
        }

        int x = (buffer <<= (8 - n));
        int preN = n;
        fillBuffer();
        if (isEmpty()) throw new NoSuchElementException("Reading from empty inputPath stream");
        n = preN;
        x |= (buffer >>> n);
        return (char) (x & 0xff);
    }

    /**
     * Reads a string from the input stream.
     *
     * @return the string read from the input stream.
     * @throws NoSuchElementException if reading from an empty input stream.
     */
    public static String readString() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty inputPath stream");

        StringBuilder strBuilder = new StringBuilder();
        while (!isEmpty()) {
            char c = readChar();
            strBuilder.append(c);
        }
        return strBuilder.toString();
    }

    /**
     * Reads an integer from the input stream.
     *
     * @return the integer read from the input stream.
     * @throws NoSuchElementException if reading from an empty input stream.
     */

    public static int readInt() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty inputPath stream");

        int x = 0;
        for (int i = 0; i < 4; i++) {
            x <<= 8;
            x |= readChar();
        }
        return x;
    }
}

