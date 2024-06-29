import StartTraining.*;


public class Programm {
    public static void main(String[] args) {
        //FileVocabulary.startWorkWithFile();
        FileVocabulary fv = new FileVocabulary();
        fv.setFilePath("C:\\Users\\PC\\OneDrive\\Рабочий стол\\Voc2.txt");

        fv.training(fv.getFilePath());
    }
}