package StartTraining;

import TrainEnglish.Vocabulary;
import TrainEnglish.Trainable;

import java.io.*;
import java.util.Scanner;

public class FileVocabulary implements Vocabulary, Trainable {

    private String filePath;

    public boolean setFilePath(String filePath) {
        this.filePath = filePath;
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist");
            return false;
        }
        return true;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean writeIntoStorage(String en, String ru) {
        try {
            BufferedWriter file = new BufferedWriter(new FileWriter(getFilePath(), true));
            file.write(en + " - ");
            file.write(ru + "\n");
            file.close();
            return true;
        } catch (IOException exe) {
            System.out.println("Error: " + exe.getMessage());
        }
        return false;
    }

    public void showWords() {
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(getFilePath()));
            String line = fileReader.readLine();
            while (line != null) {
                System.out.println(line);
                line = fileReader.readLine();
            }
            fileReader.close();
        } catch (IOException exe) {
            System.out.println("Error: " + exe.getMessage());
        }
    }


    public String findWord(String word) {
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(getFilePath()));
            String line = fileReader.readLine();
            while (line != null) {
                if (line.split(" - ")[0].equals(word) ||
                        line.split(" - ")[1].equals(word)) {
                    fileReader.close();
                    return line;
                } else {
                    line = fileReader.readLine();
                }
            }
        } catch (IOException exe) {
            System.out.println("Error: " + exe.getMessage());
        }
        return "No such word";
    }

    public boolean deleteWord(String word) {
        File filePath = new File(getFilePath());
        File tempFile = new File(filePath.getAbsolutePath() + ".tmp");
        boolean success = false;
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            String line = null;
            while ((line = br.readLine()) != null) {

                if (line.split(" - ")[0].equals(word)
                        || line.split(" - ")[1].equals(word)) {
                    success = true;
                    continue;
                } else {
                    pw.println(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();
            if (!filePath.delete()) {
                System.out.println("Could not delete file");
            }
            if (!tempFile.renameTo(filePath)) {
                System.out.println("Could not rename file");
            } else {
                tempFile.renameTo(filePath);
            }
        } catch (IOException exe) {
            System.out.println("Error: " + exe.getMessage());
        }
        return success;
    }


    public String findWordFromIndex(int index) {
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(getFilePath()));
            String line = fileReader.readLine();
            int count = 1;
            while (line != null) {
                if (count == index) {
                    fileReader.close();
                    return line;
                } else {
                    line = fileReader.readLine();
                    count++;
                }
            }
        } catch (IOException exe) {
            System.out.println("Error: " + exe.getMessage());
        }
        return "Not such word from index " + index;
    }

   public static void startWorkWithFile() {
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
                sc.close();
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
                    } catch (Exception exe) {
                        System.out.println("Enter 2 words in line");
                    }
                }
            } else if (input.equals("Sw")) {
                System.out.println();
                file.showWords();
            } else if (input.equals("Dw")) {
                String garbage = "";
                System.out.println("Word to delete: ");
                while (!garbage.equals("Stop")) {
                    garbage = sc.nextLine();
                    if (!file.deleteWord(garbage) && !garbage.equals("Stop")) {
                        System.out.println("No such word");
                    }

                }
            } else if (input.equals("Fbw")) {
                String search = "";
                System.out.println("Word to find:");
                while (true) {
                    search = sc.nextLine();
                    if (search.equals("Stop")) break;
                    System.out.println(file.findWord(search));
                }
            } else if (input.equals("Fbi")) {
                String search = "";
                while (true) {
                    search = sc.nextLine();
                    if (search.toString().equals("Stop")) break;
                    try {
                        System.out.println(file.findWordFromIndex(Integer.parseInt(search)));
                    } catch (NumberFormatException ex) {
                        System.out.println("Not a number");
                    }
                }
            }
            else {
                System.out.println("No such command");
            }
        }
    }
}


