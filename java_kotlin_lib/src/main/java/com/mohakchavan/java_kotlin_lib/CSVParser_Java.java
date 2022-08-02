package com.mohakchavan.java_kotlin_lib;

import java.io.File;
import java.util.Scanner;

public class CSVParser_Java {
    public static void main(String[] args) {
        try {
            System.out.println("Enter File Path:");
            Scanner scanner = new Scanner(System.in);

            String path = scanner.nextLine();
            if (path != null && !path.isEmpty()) {
                scanner = new Scanner(new File(path));
                scanner.useDelimiter("\n");
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                    while (scanner.hasNext()) {
                        System.out.println("https://grubbrr.atlassian.net/browse/" + scanner.next().split(",")[0]);
                    }
                }
                scanner.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
