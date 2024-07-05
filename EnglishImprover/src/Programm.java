import StartTraining.*;


public class Programm {
    public static void main(String[] args) {
        DBVocabulary db = new DBVocabulary();
        db.setUrl("jdbc:postgresql://localhost:15432/VocabularyDB");
        db.setUsername("postgres");
        db.setPassword("12345");
        db.training();
    }
}