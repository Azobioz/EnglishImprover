package TrainEnglish;

public interface Vocabulary {
   String findWord(String word);
   String findWordFromIndex(int index);
   void showWords();
   boolean deleteWord(String word);
   boolean writeIntoStorage(String en, String ru);

}
