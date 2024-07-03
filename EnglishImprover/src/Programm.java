import StartTraining.*;


public class Programm {
    public static void main(String[] args) {
        DBVocabulary db = new DBVocabulary();
        System.out.println(db.writeIntoStorage("Arms", "руки"));
        System.out.println(db.writeIntoStorage("Eyes", "Глаза"));
        System.out.println(db.writeIntoStorage("Bucket", "Ведро"));
        System.out.println(db.writeIntoStorage("Cup", "Стакан"));
    }
}