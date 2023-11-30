package com.editor.xml_editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileHandler {

    public static void writeFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content.getBytes(StandardCharsets.UTF_8));
        fos.close();
    }

    public static String readFile(String path) throws IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        int line;
        StringBuilder sb = new StringBuilder();
        while ((line = fis.read()) != -1) {
            sb.append((char) line);
        }
        return sb.toString();
    }

    public static void appendFile(String path, String appendedString) throws IOException {
        StringBuilder sb = new StringBuilder();
        String s = readFile(path);
        sb.append(s).append(appendedString);
        writeFile(path, sb.toString());
    }
}
