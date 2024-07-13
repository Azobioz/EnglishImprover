package StartTraining;
import TrainEnglish.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class DBVocabulary implements Vocabulary, Trainable {

    private String url;
    private String username;
    private String password;

    public void setUrl(String url) {
        this.url = url;
    }

    private String getUrl() {
        return url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String getPassword() {
        return password;
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


    public void showWords() {
        String query = "select * from Words";
        try {
            Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.println(rs.getString("ID_Words") + ". " + rs.getString("Words") + " - " + rs.getString("Translation_"));
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public boolean writeIntoStorage(String word, String... translation) {
        String allTranslations = String.join(" ", translation);
        String query = "call words_insert('" + word + "', '" + allTranslations + "');";
        try {
            Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
            Statement statement = connection.createStatement();
            boolean rs = statement.execute(query);
            connection.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteWord(String word) {
        String query = "select * from Words";
        try {
            Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (rs.getString("Words").equalsIgnoreCase(word)
                        || rs.getString("Translation_").equalsIgnoreCase(word)) {
                    statement.executeUpdate("call words_delete(" + rs.getString("ID_Words") + ")");
                    connection.close();
                    return true;
                }
            }
            connection.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    public String findWord(String word) {
        String query = "select * from Words";
        try {
            Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (rs.getString("Words").equalsIgnoreCase(word)
                        || rs.getString("Translation_").equalsIgnoreCase(word)) {
                    String foundedWord = rs.getString(("Words")) + " - " + rs.getString("Translation_");
                    connection.close();
                    return foundedWord;
                }
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "no such word";
    }

    public String findWordFromIndex(int index) {
        String query = "select * from Words";
        try {
            Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (rs.getInt("ID_Words") == index) {
                    String foundedWord = rs.getString("ID_Words") + ". " + rs.getString(("Words")) + " - " + rs.getString("Translation_");
                    connection.close();
                    return foundedWord;
                }
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "Not such word from index " + index;
    }

    public void training(boolean order) {
        try {
            String query = "select * from Words";
            Scanner scanner = new Scanner(System.in);
            Random rnd = new Random();
            Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            List<String> wordStorage = new ArrayList<String>();

            while (rs.next()) {
                String line = rs.getString("Words") + " - " + rs.getString("Translation_");
                wordStorage.add(line);
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
                    System.out.print(getOnlyWord(wordStorage.get(i)) + " - ");
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
                            if (getTranslationWords(wordStorage.get(i)).length > 1) {
                                System.out.println("Clue: " + getTranslationWords(wordStorage.get(i))[0].substring(0, getTranslationWords(wordStorage.get(i))[1].length() / 2));
                            }
                            else {
                                System.out.println("Clue: " + wordStorage.get(i).split(" - ")[1].substring(0, wordStorage.get(i).split(" - ")[1].length() / 2));
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
                            System.out.println("Clue: " + wordStorage.get(i).split(" - ")[1].substring(0, wordStorage.get(i).split(" - ")[1].length() / 2));
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

        } catch (SQLException exe) {
            System.out.println("Error: " + exe.getMessage());
        }
    }

    public  void startWorkWithLocalDB() {
        Scanner sc = new Scanner(System.in);

        try {
            String query = "select * from Words";
            Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            System.out.println("\nCommands:\n Write into db \n Start training \n Show words \n Find by index \n Find by word \n Delete word\n");
            String input = "";
            while (true) {
                input = sc.nextLine();
                if (input.equalsIgnoreCase("End")) {
                    sc.close();
                    break;
                }
                if (input.equalsIgnoreCase("Widb")) {
                    String wordInput = "";
                    System.out.println("Enter a word (Word - Translation1 Translation2)");
                    while (true) {
                        try {
                            wordInput = sc.nextLine();
                            if (wordInput.equalsIgnoreCase("Stop")){
                                System.out.println();
                                break;
                            }
                            if (getTranslationWords(wordInput).length > 1) {
                                StringBuilder allTranslation = new StringBuilder();
                                for (int i = 0; i < getTranslationWords(wordInput).length; i++) {
                                    allTranslation.append(getTranslationWords(wordInput)[i] + " ");
                                }
                                writeIntoStorage(getOnlyWord(wordInput), allTranslation.toString());
                            }
                            else {
                                writeIntoStorage(getOnlyWord(wordInput), wordInput.split(" - ")[1]);
                            }
                        } catch (Exception exe) {
                            System.out.println("Enter 2 or more words in line");
                        }
                    }
                }
                else if (input.equalsIgnoreCase("Sw")) {
                    showWords();
                    System.out.println();
                }
                else if (input.equalsIgnoreCase("Dw")) {
                    String garbage = "";
                    System.out.println("Word to delete: ");
                    while (true) {
                        garbage = sc.nextLine();
                        if (garbage.equalsIgnoreCase("Stop")) {
                            System.out.println();
                            break;
                        }
                        else if (!deleteWord(garbage) && !garbage.equalsIgnoreCase("Stop")) {
                            System.out.println("No such word");
                        }
                    }
                }
                else if (input.equalsIgnoreCase("Fbw")) {
                    String search = "";
                    System.out.println("Word to find:");
                    while (true) {
                        search = sc.nextLine();
                        if (search.equalsIgnoreCase("Stop")) {
                            System.out.println();
                            break;
                        }
                        System.out.println(findWord(search));
                    }
                } else if (input.equalsIgnoreCase("Fbi")) {
                    String search = "";
                    System.out.println("Enter an index");
                    while (true) {
                        search = sc.nextLine();
                        if (search.toString().equalsIgnoreCase("Stop")) {
                            System.out.println();
                            break;
                        }
                        try {
                            System.out.println(findWordFromIndex(Integer.parseInt(search)));
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
                            training(false);
                            input = "";
                        }
                        else if (input.equalsIgnoreCase("tw")) {
                            training(true);
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
        } catch (SQLException exe) {
            System.out.println("Error: " + exe.getMessage());
        }

    }
}


