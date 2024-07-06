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

    public void training() {
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
                } catch (Exception exe) {
                    i = 0;
                }
                System.out.print(wordStorage.get(i).split(" - ")[0] + " - ");
                String wordInput = scanner.nextLine();
                if (wordStorage.get(i).split(" ").length > 3) {
                    boolean correctWord = false;
                    for (int j = 2; j < wordStorage.get(i).split(" ").length && !wordInput.equalsIgnoreCase("stop"); j++) {
                        if (wordStorage.get(i).split(" ")[j].equalsIgnoreCase(wordInput)) {
                            System.out.println("Correct!");
                            wordStorage.remove(i);
                            correctWord = true;
                            break;
                        }
                    }
                    if (!correctWord) {
                        System.out.println("Wrong!");
                        System.out.println("Clue: " + wordStorage.get(i).split(" ")[2].substring(0, wordStorage.get(i).split(" ")[2].length() / 2));
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
                    System.out.println("Clue: " + wordStorage.get(i).split(" - ")[1].substring(0, (wordStorage.get(i).split(" - ")[1].length() / 2)));
                }
                if (wordStorage.isEmpty()) {
                    System.out.println();
                }
            }

        } catch (SQLException exe) {
            System.out.println("Error: " + exe.getMessage());
        }
    }

    public void startWorkWithLocalDB() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "select * from Words";
            Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            System.out.println("\nCommands:\n Write into db \n Start training \n Show words \n Find by index \n Find by word \n Delete word\n");
            while (true) {
                String input = sc.nextLine();
                System.out.println();
                if (input.equalsIgnoreCase("End")) {
                    sc.close();
                    break;
                }
                if (input.equalsIgnoreCase("Widb")) {
                    String wordInput = "";
                    System.out.println("Enter a word (Example Пример)");
                    while (true) {
                        try {
                            wordInput = sc.nextLine();
                            if (wordInput.equalsIgnoreCase("Stop")) {
                                System.out.println();
                                break;
                            }
                            if (wordInput.split(" ").length > 2) {
                                StringBuilder allTranslation = new StringBuilder();
                                for (int i = 1; i < wordInput.split(" ").length; i++) {
                                    allTranslation.append(wordInput.split(" ")[i] + " ");
                                }
                                writeIntoStorage(wordInput.split(" ")[0], allTranslation.toString());
                            }
                            else {
                                writeIntoStorage(wordInput.split(" ")[0], wordInput.split(" ")[1]);
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
                    while (!garbage.equalsIgnoreCase("Stop")) {
                        garbage = sc.nextLine();
                        if (!deleteWord(garbage) && !garbage.equals("Stop")) {
                            System.out.println("No such word");
                        }

                    }
                    System.out.println();
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
                }
                else if (input.equalsIgnoreCase("Fbi")) {
                    System.out.println("Enter an index");
                    String search = "";
                    while (true) {
                        search = sc.nextLine();
                        if (search.toString().equalsIgnoreCase("Stop")) {
                            System.out.println();
                            break;
                        }
                        try {
                            System.out.println(findWordFromIndex(Integer.parseInt(search)));
                        }
                        catch (NumberFormatException ex) {
                            System.out.println("Not a number");
                        }
                    }
                }
                else if (input.equalsIgnoreCase("St")) {
                    training();
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


