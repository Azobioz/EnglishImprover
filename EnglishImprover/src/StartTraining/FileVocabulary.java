package StartTraining;

import TrainEnglish.Vocabulary;
import TrainEnglish.Trainable;

import java.io.*;

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
}


