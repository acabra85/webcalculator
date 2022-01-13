package com.acabra.fsands;

import java.io.*;
import java.util.stream.IntStream;

public class UtilsTest {


    public static String[] readPasswordsFromFile(String fileName) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(getTestResource(fileName)))) {
            int total = Integer.parseInt(br.readLine());
            String[] ans = new String[total];
            IntStream.range(0, 200).forEach(i -> {
                try {
                    ans[i] = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return ans;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    public static InputStream getTestResource(String name) {
        return UtilsTest.class.getClassLoader().getResourceAsStream(name);
    }
}
