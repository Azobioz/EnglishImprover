package StartTraining;

import TrainEnglish.*;


import java.io.*;
import java.util.*;

public class FileVocabulary implements Vocabulary, Trainable {

    private static String filePath;
    public boolean setFilePath(String filePath) {
        if (filePath.startsWith("\"") && filePath.endsWith("\"")) {
           FileVocabulary.filePath =  filePath.substring(1, filePath.length() - 1);
        }
        else {
            FileVocabulary.filePath = filePath;
        }
        File file = new File(FileVocabulary.filePath);
        if (!file.exists()) {
            System.out.println("File does not exist");
            return false;
        }
        return true;
    }


    private String getFilePath() {
        return filePath;
    }

    private static String[] getTranslationWords(String translation) {
        return translation.split(" - ")[1].split(" ");
    }

    private static String getOnlyWord(String word) {
        return word.split(" - ")[0];
    }

    private static int getConditionLength(String line) {
        return line.split(" - ")[1].split(" ").length + 1;
    }

    private static String[] getWordAndTranslation(String line) {
        String word = line.split(" - ")[0];
        String[] translation = line.split(" - ")[1].split(" ");
        String[] wordAndTranslation = new String[translation.length + 1];
        wordAndTranslation[0] = word;
        System.arraycopy(translation, 0, wordAndTranslation, 1, translation.length);
        return wordAndTranslation;
    }

    public boolean writeIntoStorage(String word, String... translation) {
        try {
            BufferedWriter file = new BufferedWriter(new FileWriter(getFilePath(), true));
            String allTranslations = String.join(", ", translation);
            file.write(word + " - "  + allTranslations);
            file.newLine();
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
                for (int j = 0; j < getConditionLength(line); j++) {
                    if (getWordAndTranslation(line)[j].equalsIgnoreCase(word)) {
                        fileReader.close();
                        return line;
                    }
                }
                line = fileReader.readLine();
            }
        }
        catch (IOException exe) {
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
            BufferedReader br = new BufferedReader(new FileReader(getFilePath()));

            String line = null;
                while ((line = br.readLine()) != null) {
                     success = false;
                    for (int j = 0;  j < getConditionLength(line); j++) {
                        if (getWordAndTranslation(line)[j].equalsIgnoreCase(word)) {
                            success = true;
                            break;
                        }
                    }
                    if (!success) {
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
            }
            else {
                tempFile.renameTo(filePath);
            }
        }
        catch (IOException exe) {
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
                }
                else {
                    line = fileReader.readLine();
                    count++;
                }
            }
        } catch (IOException exe) {
            System.out.println("Error: " + exe.getMessage());
        }
        return "Not such word from index " + index;
    }

    public void training(boolean order) {
        try {
            Scanner scanner = new Scanner(System.in);
            Random rnd = new Random();
            BufferedReader fileReader = new BufferedReader(new FileReader(getFilePath()));
            List<String> wordStorage = new ArrayList<String>();
            String line = fileReader.readLine();

            while (line != null) {
                wordStorage.add(line);
                line = fileReader.readLine();
            }

            while (!wordStorage.isEmpty()) {
                int i = -1;
                try {
                     i = rnd.nextInt(0, wordStorage.size());
                }
                catch (Exception exe) {
                    i = 0;
                }
                String wordInput = "";
                if (!order) {
                    System.out.print(wordStorage.get(i).split(" - ")[0] + " - ");
                    wordInput = scanner.nextLine();
                    if (getTranslationWords(wordStorage.get(i)).length > 1 && !wordInput.equalsIgnoreCase("Stop")) {
                        boolean correctWord = false;
                        for (int j = 0; j < getTranslationWords(wordStorage.get(i)).length && !wordInput.equalsIgnoreCase("Stop"); j++) {
                            if (getTranslationWords(wordStorage.get(i))[j].equalsIgnoreCase(wordInput)) {
                                System.out.println("Correct!");
                                wordStorage.remove(i);
                                correctWord = true;
                                break;
                            }
                        }
                        if (!correctWord) {
                            System.out.println("Wrong!");
                            if (wordStorage.get(i).split(" - ")[1].split(" ").length > 2) {
                                System.out.println("Clue: " + getTranslationWords(wordStorage.get(i))[0].substring(0, getTranslationWords(wordStorage.get(i))[1].length() / 2));
                            }
                            else {
                                System.out.println("Clue: " + getTranslationWords(wordStorage.get(i))[1].substring(0, getTranslationWords(wordStorage.get(i))[1].length() / 2));
                            }
                        }
                    }
                    else if (wordInput.equalsIgnoreCase(wordStorage.get(i).split(" - ")[1])) {
                        System.out.println("Correct!");
                        wordStorage.remove(i);
                    }
                    else if (wordInput.equalsIgnoreCase("Stop")) {
                        break;
                    }
                    else {
                        System.out.println("Wrong!");
                        if (getTranslationWords(wordStorage.get(i)).length > 1) {
                            System.out.println("Clue: " + getTranslationWords(wordStorage.get(i))[1].substring(0, getTranslationWords(wordStorage.get(i))[1].length() / 2));
                        }
                        else {
                            System.out.println("Clue: " + getOnlyWord(wordStorage.get(i)).substring(0, getOnlyWord(wordStorage.get(i)).length() / 2));
                        }
                    }
                }
                else if (order) {
                    System.out.print(wordStorage.get(i).split(" - ")[1] + " - ");
                    wordInput = scanner.nextLine();
                    if (getTranslationWords(wordStorage.get(i)).length > 1 && !wordInput.equalsIgnoreCase("Stop")) {
                        boolean correctWord = false;
                        for (int j = 0; j < getTranslationWords(wordStorage.get(i)).length && !wordInput.equalsIgnoreCase("Stop"); j++) {
                            if (getOnlyWord(wordStorage.get(i)).equalsIgnoreCase(wordInput)) {
                                System.out.println("Correct!");
                                wordStorage.remove(i);
                                correctWord = true;
                                break;
                            }
                        }
                        if (!correctWord) {
                            System.out.println("Wrong!");
                            System.out.println("Clue: " + getOnlyWord(wordStorage.get(i)).substring(0, getOnlyWord(wordStorage.get(i)).length() / 2));
                        }
                    }
                    else if (wordInput.equalsIgnoreCase(getOnlyWord(wordStorage.get(i)))) {
                        System.out.println("Correct!");
                        wordStorage.remove(i);
                    }
                    else if (wordInput.equalsIgnoreCase("Stop")) {
                        break;
                    }
                    else {
                        System.out.println("Wrong!");
                        System.out.println("Clue: " + getTranslationWords(wordStorage.get(i))[0].substring(0, getTranslationWords(wordStorage.get(i))[0].length() / 2));
                    }
                }
            }
        }
        catch (IOException exe) {
            System.out.println("Error: IOException");
        }
    }

   public static void startWorkWithFile() {
        Scanner sc = new Scanner(System.in);
        FileVocabulary file = new FileVocabulary();
        String input = "";
        if (file.getFilePath() == null) {

            System.out.println("Enter a file path");
            input = sc.nextLine();

            while (!file.setFilePath(input)) {
                input = sc.nextLine();
            }
        }
       System.out.println("\nCommands:\n Write into file \n Start training \n Show words \n Find by index \n Find by word \n Delete word");
       while (true) {
            System.out.println();
            input = sc.nextLine();
            if (input.equalsIgnoreCase("End")) {
                sc.close();
                break;
            }
            if (input.equalsIgnoreCase("Wif")) {
                String wordInput = "";
                System.out.println("Enter a word (Word - Translation1 Translation2)");
                while (true) {
                    try {
                        wordInput = sc.nextLine();
                        if (wordInput.equalsIgnoreCase("Stop")){
                            break;
                        }
                        if (getTranslationWords(wordInput).length > 1) {
                            StringBuilder allTranslation = new StringBuilder();
                            for (int i = 0; i < getTranslationWords(wordInput).length; i++) {
                                allTranslation.append(getTranslationWords(wordInput)[i] + " ");
                            }
                            file.writeIntoStorage(getOnlyWord(wordInput), allTranslation.toString());
                        }
                        else {
                            file.writeIntoStorage(getOnlyWord(wordInput), wordInput.split(" - ")[1]);
                        }
                    } catch (Exception exe) {
                        System.out.println("Enter 2 or more words in line");
                    }
                }
            }
            else if (input.equalsIgnoreCase("Sw")) {
                System.out.println();
                file.showWords();
            }
            else if (input.equalsIgnoreCase("Dw")) {
                String garbage = "";
                System.out.println("Word to delete: ");
                while (true) {
                    garbage = sc.nextLine();
                    if (garbage.equalsIgnoreCase("Stop")) {
                        break;
                    }
                    else if (file.deleteWord(garbage) && !garbage.equalsIgnoreCase("Stop")) {
                        System.out.println("No such word");
                    }
                }
            }
            else if (input.equalsIgnoreCase("Fbw")) {
                String search = "";
                System.out.println("Word to find:");
                while (true) {
                    search = sc.nextLine();
                    if (search.equalsIgnoreCase("Stop")) break;
                    System.out.println(file.findWord(search));
                }
            } else if (input.equalsIgnoreCase("Fbi")) {
                String search = "";
                System.out.println("Enter an index");
                while (true) {
                    search = sc.nextLine();
                    if (search.toString().equalsIgnoreCase("Stop")) break;
                    try {
                        System.out.println(file.findWordFromIndex(Integer.parseInt(search)));
                    } catch (NumberFormatException ex) {
                        System.out.println("Not a number");
                    }
                }
            }
            else if (input.equalsIgnoreCase("St")) {
                System.out.println("Word - Translation / Translation - Word (wt/tw)");
                do {
                    input = sc.nextLine();
                    if (input.equalsIgnoreCase("wt")) {
                        file.training(false);
                        input = "";
                    }
                    else if (input.equalsIgnoreCase("tw")) {
                        file.training(true);
                        input = "";
                    }
                    else if (input.equalsIgnoreCase("Stop")) {
                        break;
                    }
                    else {
                        System.out.println("Enter wt or tw");
                    }
                } while (input.equalsIgnoreCase("wt") && !input.equalsIgnoreCase("tw")
                || !input.equalsIgnoreCase("wt") && input.equalsIgnoreCase("tw"));

            }
            else {
                System.out.println("No such command");
            }
       }
    }
}


