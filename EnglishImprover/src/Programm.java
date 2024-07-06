import StartTraining.*;


public class Programm {
    public static void main(String[] args) {
        DBVocabulary db = new DBVocabulary();
        db.setUrl("");
        db.setUsername("");
        db.setPassword("");
        db.startWorkWithLocalDB();

    }
}