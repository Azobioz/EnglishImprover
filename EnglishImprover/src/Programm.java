import StartTraining.*;


public class Programm {
    public static void main(String[] args) {
        DBVocabulary db = new DBVocabulary();
        System.out.println(db.findWord("ухо"));
    }
}