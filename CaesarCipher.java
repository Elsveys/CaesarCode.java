import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.io.IOException;
import java.util.HashSet;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;


public class CaesarCipher {
    private static StringBuilder decryptedTextResult = new StringBuilder();

    public static void main(String[] args) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the name of the file to read: ");
        String fileName = scanner.nextLine();

        String filePath = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Result.txt";

        try {
            reader = new BufferedReader(new FileReader(fileName));
            StringBuilder cipherText = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                cipherText.append(line).append(' ');
            }

            int option = 0;
            while (option < 1 || option > 3) {
                try {
                    System.out.println("Choose a decryption option:");
                    System.out.println("1. Enter key");
                    System.out.println("2. Brute force (all codes displayed)");
                    System.out.println("3. Brute force (automatic)");

                    option = scanner.nextInt();

                    if (option < 1 || option > 3) {
                        System.out.println("Invalid option. Please enter a number between 1 and 3.");
                    }
                } catch (Exception e) {
                    System.out.println("Please enter a number between 1 and 3.");
                    scanner.nextLine();
                }
            }

            scanner.nextLine();

            if (option == 1) {
                System.out.print("Enter the decryption/encryption key (a number): ");
                int decryptionKey = scanner.nextInt();
                scanner.nextLine();
                String decryptedText = decrypt(cipherText.toString(), decryptionKey);
                System.out.println("Decrypted/encrypted text:");
                System.out.println(decryptedText);

                writer = new BufferedWriter(new FileWriter(filePath));
                writer.write(decryptedText);
                System.out.println("Decrypted text has been saved to the file: " + filePath);
            } else if (option == 2) {
                System.out.println("Performing brute-force decryption (all codes displayed)...");
                StringBuilder output = new StringBuilder();
                for (int key = 1; key <= 34; key++) {
                    String decryptedText = decrypt(cipherText.toString(), key);
                    System.out.println("Key " + key + ": " + decryptedText);
                    output.append("Key ").append(key).append(": ").append(decryptedText).append("\n");
                }

                writer = new BufferedWriter(new FileWriter(filePath));
                writer.write(output.toString());
                System.out.println("Decrypted text has been saved to the file: " + filePath);
            } else if (option == 3) {
                System.out.println("Performing brute-force decryption (automatic)...");
                decryptedTextResult.setLength(0);
                autoDecryption(cipherText.toString());
                System.out.println(decryptedTextResult);
                writer = new BufferedWriter(new FileWriter(filePath));
                writer.write(decryptedTextResult.toString());
                System.out.println("Decrypted text has been saved to the file: " + filePath);
            } else {
                System.out.println("Invalid option. Please choose a valid decryption option.");
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found. Please enter a valid file name.");
        } catch (IOException e) {
            System.err.println("Error reading or writing to the file: " + e.getMessage());
        } finally {
            try {
                if (reader != null)
                    reader.close();
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                System.err.println("Error closing the file: " + e.getMessage());
            }
            scanner.close();
        }
    }

    public static String decrypt(String cipherText, int key) {
        StringBuilder decryptedText = new StringBuilder();

        for (char ch : cipherText.toCharArray()) {
            if (Character.isLetter(ch)) {
                char[] characters = Character.isUpperCase(ch) ? charactersAndPunctuationSymbols.capitalLetters : charactersAndPunctuationSymbols.charactersAndPunctuationSymbols;
                int index = indexOf(characters, ch);
                if (index >= 0) {
                    char decryptedChar = characters[(index - key + characters.length) % characters.length];
                    decryptedText.append(decryptedChar);
                }
            } else {
                decryptedText.append(ch);
            }
        }

        return decryptedText.toString();
    }

    private static int indexOf(char[] characters, char ch) {
        for (int i = 0; i < characters.length; i++) {
            if (characters[i] == ch) {
                return i;
            }
        }
        return -1;
    }

    public static void autoDecryption(String ciphertext) {
        String[] words = ciphertext.split(" ");
        int totalWords = words.length;
        int bestKey = 0;
        int maxCommonWordCount = 0;

        HashSet<String> commonWordsSet = new HashSet<>();
        for (String commonWord : commonWords.commonWords) {
            commonWordsSet.add(commonWord.toLowerCase());
        }

        for (int key = 1; key <= 34; key++) {
            String decryptedText = decrypt(ciphertext, key);
            int commonWordCount = 0;

            for (String word : decryptedText.split(" ")) {
                String lowercaseWord = word.toLowerCase();
                if (commonWordsSet.contains(lowercaseWord)) {
                    commonWordCount++;
                }
            }

            double percentage = (double) commonWordCount / totalWords * 100;

            if (percentage >= 7.5 && commonWordCount > maxCommonWordCount) {
                bestKey = key;
                maxCommonWordCount = commonWordCount;
            }
        }

        if (bestKey != 0) {
            String decryptedText = decrypt(ciphertext, bestKey);
            decryptedTextResult.append("Key ").append(bestKey).append(": ").append(decryptedText).append("\n");
        } else {
            decryptedTextResult.append("No valid decryption found with common word percentage >= 7.5%.\n");
        }
    }
}