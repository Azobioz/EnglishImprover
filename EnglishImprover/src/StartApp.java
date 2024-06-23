import StartTraining.*;

import java.util.Scanner;

public class StartApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a file path");
        String input = sc.nextLine();
        FileVocabulary file = new FileVocabulary();
        while (!file.setFilePath(input)) {
            input = sc.nextLine();
        }
        System.out.println("\nCommands:\n Write into file \n Show words \n Find by index \n Find by word \n Delete word");
        while (true) {
            System.out.println();
            input = sc.nextLine();
            if (input.equals("End")) {
                break;
            }
            if (input.equals("Wif")) {
                String[] word = new String[2];
                String wordInput = "";
                System.out.println("Enter a word (Example Пример)");
                while (true) {
                    try {
                        wordInput = sc.nextLine();
                        if (wordInput.equals("Stop")) break;
                        file.writeIntoStorage(wordInput.split(" ")[0], wordInput.split(" ")[1]);
                    }
                    catch (Exception exe) {
                        System.out.println("Enter 2 words in line");
                    }
                }
            }
            else if (input.equals("Sw")) {
                System.out.println();
                file.showWords();
            }
            else if (input.equals("Dw")) {
                String garbage = "";
                System.out.println("Word to delete: ");
                while (!garbage.equals("Stop")) {
                    garbage = sc.nextLine();
                    if (!file.deleteWord(garbage) && !garbage.equals("Stop")) {
                        System.out.println("No such word");
                    }

                }
            }
            else if (input.equals("Fbw")) {
                String search = "";
                System.out.println("Word to find:");
                while (true) {
                    search = sc.nextLine();
                    if (search.equals("Stop")) break;
                    System.out.println(file.findWord(search));
                }
            }
            else if (input.equals("Fbi")) {
                Integer search = -1;
                while (true) {
                    search = sc.nextInt();
                    if (search == 0) break;
                    System.out.println(file.findWordFromIndex(search));
                }
            }
            else {
                System.out.println("No such command");
            }



        }



    }
}