package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MobileLibrary {
    static Scanner scanner = new Scanner(System.in);
    static String bookList = "Books.txt";

    public static void main(String[] args) {

        System.out.println("Select the Operation You Want to Perform: ");
        System.out.println("1- Add Book.");
        System.out.println("2- List Books. ");
        int val;
        try {

            val = scanner.nextInt();
            switch (val) {
                case 1:
                    System.out.println("Enter the Following Information to Add a Book");

                    try {
                        System.out.println("Book Name: ");
                        String bookName = scanner.next();
                        System.out.println("Author Name: ");
                        String authorName = scanner.next();
                        System.out.println("Year of Publication: ");
                        int yearOfPublication = scanner.nextInt();
                        System.out.println("ISBN: ");
                        int isbn = scanner.nextInt();
                        Library.addBook(bookName, authorName, yearOfPublication, isbn);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    Library.listBooks();
                    break;
                default:
                    System.out.println("Please enter a valid value");
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
            System.out.println("Incorrect input.");
        }
    }

    public static class Library {
        static ArrayList<String> bookArrayList = new ArrayList<>();

        public static void addBook(String bookName, String authorName, int yearOfPublication, int isbn) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(bookList, true))) {
                writer.write("Book" +
                        "bookName=" + bookName +
                        ", authorName=" + authorName +
                        ", yearOfPublication=" + yearOfPublication +
                        ", isbn=" + isbn);
                writer.newLine();

                System.out.println("Writing to the file completed successfully.");

            } catch (IOException e) {
                System.err.println("File writing error: " + e.getMessage());
            }
        }

        public static void listBooks() {
            try (BufferedReader reader = new BufferedReader(new FileReader(bookList))) {
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    System.out.println((i + 1) + ". Book :" + line);
                    bookArrayList.add(i, line);
                    i++;
                }

            } catch (IOException e) {
                System.err.println("File reading error: " + e.getMessage());
            }

            System.out.println("Select the operation you want to perform: ");
            int val = scanner.nextInt();
            switch (val) {
                case 1:
                    System.out.println("Delete Book");
                    deleteBook();
                    break;
                case 2:
                    System.out.println("Exit");
                    break;
            }
        }

        public static void deleteBook() {
            System.out.println("Which book do you want to delete:");
            int lineIndexToDelete = scanner.nextInt();

            try (BufferedReader reader = new BufferedReader(new FileReader(bookList));
                 BufferedWriter writer = new BufferedWriter(new FileWriter("temporaryFile.txt", false))) {

                String line;
                int lineCounter = 0;
                while ((line = reader.readLine()) != null) {
                    if (lineCounter != lineIndexToDelete - 1) {
                        writer.write(line);
                        writer.newLine();
                    }
                    lineCounter++;
                }

            } catch (IOException e) {
                System.err.println("File operations error: " + e.getMessage());
            }

            File temporaryFile = new File("temporaryFile.txt");
            File originalFile = new File(bookList);
            if (temporaryFile.renameTo(originalFile)) {
                System.out.println("Line deleted successfully.");
            } else {
                System.err.println("Error deleting line.");
            }
        }
    }
}
