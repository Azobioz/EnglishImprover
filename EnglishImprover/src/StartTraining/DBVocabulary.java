package StartTraining;
import TrainEnglish.*;

import java.sql.*;
public class DBVocabulary implements Vocabulary, Trainable {

    public void showWords()   {
        String query = "select * from Words";
        String url = "jdbc:postgresql://localhost:15432/VocabularyDB";
        String username = "postgres";
        String password = "12345";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                System.out.println(rs.getString("Words") + " - " + rs.getString("Translation_"));
            }
            connection.close();
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public boolean writeIntoStorage(String word, String... translation) {
        String allTranslations = String.join(" ", translation);
        String query = "call words_insert('" + word + "', '" + allTranslations + "');";
        String url = "jdbc:postgresql://localhost:15432/VocabularyDB";
        String username = "postgres";
        String password = "12345";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            boolean rs = statement.execute(query);
            connection.close();
            return true;
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }
}
