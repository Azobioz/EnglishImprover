# Stack of technology:
* Java, JDBC, Postgresql

# How to use VocabularyImprover
## Using txt file
1. Write code in main method from Program class
2. Create an object of FileVocabulary class
   
   ![image](https://github.com/user-attachments/assets/03173879-ea68-495d-8ad4-d3ceaf29d3fc)
 3. There 2 ways to set file path to vocabulary 
  
    ![image](https://github.com/user-attachments/assets/dc19e3b4-9ca1-45ff-a4fb-33194637c71a)
    
    ![image](https://github.com/user-attachments/assets/6d64df17-f76f-4042-b0aa-6b72012929ed)

 4. Call startWorkWithFile method from static class FileVocabulary and press Shift + F10

    ![image](https://github.com/user-attachments/assets/91e93af8-6a6f-4bbe-8984-8ea3359ef64b)

After you did above you see that

![image](https://github.com/user-attachments/assets/10c99100-31b8-44f5-a75a-b4e316e5a694)

Following is working for both file and local data base

* Stop - stop the process of command you wrote
* End - stop the program
* To use command write their reductions
  
  Example: write into file - wif
  
  start training - st

  # Using local data base
  
 1. Open Postgresql 
  2. Copy code from Create Words.sql file to create table and Procedure Words.sql to create procedures: insert and delete
  3. Write codes in method main of Program class
  4. Create an object of DBVocabulary class
  5. Call 3 methods

     ![image](https://github.com/user-attachments/assets/ef62daed-6d37-4006-88e0-f4d5966d4786)

7. Call startWorkWithLocalDB method

   ![image](https://github.com/user-attachments/assets/ee6a7415-f7fa-4035-9266-f941bedb99e9)

![image](https://github.com/user-attachments/assets/48b0150c-a902-4eac-9c38-deb64a0afe28)
