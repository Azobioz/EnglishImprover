package StartTraining;

import TrainEnglish.Vocabulary;
import TrainEnglish.Trainable;

import java.io.*;
import java.util.*;

public class FileVocabulary implements Vocabulary, Trainable {

    private String filePath;

    public boolean setFilePath(String filePath) {
        if (filePath.startsWith("\"") && filePath.endsWith("\"")) {
           this.filePath =  filePath.substring(1, filePath.length() - 1);
        }
        else {
            this.filePath = filePath;
        }
        File file = new File(this.filePath);
        if (!file.exists()) {
            System.out.println("File does not exist");
            return false;
        }
        return true;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean writeIntoStorage(String word, String... translation) {
        try {
            BufferedWriter file = new BufferedWriter(new FileWriter(getFilePath(), true));
            String allTranslations = String.join(" ", translation);
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
                if (line.split(" - ")[0].equals(word) ||
                        line.split(" - ")[1].equalsIgnoreCase(word)) {
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

                if (line.split(" - ")[0].equalsIgnoreCase(word)
                        || line.split(" - ")[1].equalsIgnoreCase(word)) {
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

    public void training(String filePath) {
        try {
            Scanner scanner = new Scanner(System.in);
            Random rnd = new Random();
            BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
            List<String> wordStorage = new ArrayList<String>();
            String line = fileReader.readLine();

            while(line != null) {
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
                System.out.print(wordStorage.get(i).split(" - ")[0] + " - ");
                String wordInput = scanner.nextLine();
                if (wordStorage.get(i).split(" ").length > 3) {
                    boolean correctWord = false;
                    for (int j = 2; j < wordStorage.get(i).length() && !wordInput.equalsIgnoreCase("stop"); j++) {
                        if (wordStorage.get(i).split(" ")[j].equalsIgnoreCase(wordInput)) {
                            System.out.println("Correct!");
                            wordStorage.remove(i);
                            correctWord = true;
                            break;
                        }
                    }
                    if (!correctWord) {
                        System.out.println("Wrong!");
                        System.out.println("Clue: " + wordStorage.get(i).split(" ")[2].substring(0, wordStorage.get(i).split(" ")[2].length()/2));
                    }
                }
                else if(wordInput.equalsIgnoreCase(wordStorage.get(i).split(" - ")[1])) {
                    System.out.println("Correct!");
                    wordStorage.remove(i);
                }
                else if (wordInput.equalsIgnoreCase("Stop")) {
                    break;
                }
                else {
                    System.out.println("Wrong!");
                    System.out.println("Clue: " + wordStorage.get(i).split(" - ")[1].substring(0, (wordStorage.get(i).split(" - ")[1].length() / 2)));
                }
            }

        }
        catch (IOException exe) {
            System.out.println("Error: IOException");
        }
    }

    //static void startTrain

   public static void startWorkWithFile() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a file path");
        String input = sc.nextLine();
        FileVocabulary file = new FileVocabulary();
        while (!file.setFilePath(input)) {
            input = sc.nextLine();
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
                System.out.println("Enter a word (Example Пример)");
                while (true) {
                    try {
                        wordInput = sc.nextLine();
                        if (wordInput.equalsIgnoreCase("Stop")) break;
                        if (wordInput.split(" ").length > 2) {
                            StringBuilder allTranslation = new StringBuilder();
                            for (int i = 1; i < wordInput.split(" ").length; i++) {
                                allTranslation.append(wordInput.split(" ")[i] + " ");
                            }
                            file.writeIntoStorage(wordInput.split(" ")[0], allTranslation.toString());
                        }
                        else {
                            file.writeIntoStorage(wordInput.split(" ")[0], wordInput.split(" ")[1]);
                        }
                    } catch (Exception exe) {
                        System.out.println("Enter 2 or more words in line");
                    }
                }
            } else if (input.equalsIgnoreCase("Sw")) {
                System.out.println();
                file.showWords();
            } else if (input.equalsIgnoreCase("Dw")) {
                String garbage = "";
                System.out.println("Word to delete: ");
                while (!garbage.equalsIgnoreCase("Stop")) {
                    garbage = sc.nextLine();
                    if (!file.deleteWord(garbage) && !garbage.equals("Stop")) {
                        System.out.println("No such word");
                    }

                }
            } else if (input.equalsIgnoreCase("Fbw")) {
                String search = "";
                System.out.println("Word to find:");
                while (true) {
                    search = sc.nextLine();
                    if (search.equalsIgnoreCase("Stop")) break;
                    System.out.println(file.findWord(search));
                }
            } else if (input.equalsIgnoreCase("Fbi")) {
                String search = "";
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
                file.training(file.getFilePath());
            }
            else {
                System.out.println("No such command");
            }
        }
    }
}


