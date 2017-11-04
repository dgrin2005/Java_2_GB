/**
 * Java 2. Lesson 3.
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-09-08
 */

import java.io.*;
import java.util.*;

public class Lesson3 {

    public static void main(String[] args) {

        //������� 1.
        new UniqWords();

        //������� 1*.
        System.out.println();
        try {
            new UniqWordsFromFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //������� 2.
        System.out.println();
        PhoneBase phoneBase = new PhoneBase();
        phoneBase.add("123456", "Ivanov");
        phoneBase.add("234567", "Petrov");
        phoneBase.add("345678", "Sidorov");
        phoneBase.add("456789", "Petrov");
        phoneBase.add("654321", "Ivanov");

        System.out.println("���������� ����:");
        System.out.println(phoneBase);

        System.out.println(phoneBase.get("", "Ivanov"));
        System.out.println(phoneBase.get("", "Petrov"));
        System.out.println(phoneBase.get("", "Fedorov"));
        System.out.println(phoneBase.get("123123", ""));
        System.out.println(phoneBase.get("456789", ""));

    }

}

class UniqWords {

    UniqWords(){
        List <String> words = new ArrayList<>();
        Map <String, Integer>  uniqWords = new HashMap<>();

        words.add("QWER");
        words.add("ASDF");
        words.add("QWER");
        words.add("ASDF");
        words.add("ASDF");
        words.add("ZXCV");
        words.add("ZXCV");
        words.add("QWER");
        words.add("QWER");
        words.add("ZXCV");
        words.add("ASDF");
        words.add("QWER");

        System.out.println("����� ����:");
        System.out.println(words);

        for (int i = 0; i < words.size(); i++) {
            if (!uniqWords.containsKey(words.get(i))) uniqWords.put(words.get(i), 1);
            else {
                int j = uniqWords.get(words.get(i));
                uniqWords.put(words.get(i), j + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : uniqWords.entrySet()) {
            System.out.println("����� " + entry.getKey() + " ����������� " + entry.getValue() + " ���");
        }
    }

}

class UniqWordsFromFile {

    final static String FILE_PATH = "textfile.txt";

    UniqWordsFromFile() throws IOException {
        List <String> words = new ArrayList<>();
        Map <String, Integer>  uniqWords = new HashMap<>();

        FileInputStream fstream = null;
        fstream = new FileInputStream(FILE_PATH);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;

        System.out.println("���������� ����� " + FILE_PATH + ":");
        while ((strLine = br.readLine()) != null){
            System.out.println(strLine);
            String[] wordsInLine = strLine.split(" ");
            for (String word : wordsInLine) {
                if (word.length() > 0) words.add(word);
            }
        }

        for (int i = 0; i < words.size(); i++) {
            if (!uniqWords.containsKey(words.get(i))) uniqWords.put(words.get(i), 1);
            else {
                int j = uniqWords.get(words.get(i));
                uniqWords.put(words.get(i), j + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : uniqWords.entrySet()) {
            System.out.println("����� " + entry.getKey() + " ����������� " + entry.getValue() + " ���");
        }
    }

}

class PhoneBase{

    Map<String, String> phoneBase = new HashMap<>();

    void add(String phone, String familia) {
        phoneBase.put(phone, familia);
    }

    String get(String phone, String familia) {
        if (!phone.trim().equals("")) {
            //����� �� ������ ��������
            familia = phoneBase.get(phone);
            return "����� ������� �� ������ '" + phone + "'\n ���������: " + ((familia == null) ? "�� �������" : familia);
        } else {
            //����� �� �������
            phone = "";
            for (Map.Entry<String, String> entry : phoneBase.entrySet()) {
                String p = entry.getKey();
                String f = entry.getValue();
                if (f == familia) {
                    if (!phone.trim().equals("")) phone += "\t";
                    phone += p;
                }
            }
            return "����� �������� �� ������� '" + familia + "'\n ���������: " + ((phone.equals("")) ? "�� �������" : phone);
        }
    }

    @Override
    public  String toString() {
        String base = "";
        for (Map.Entry<String, String> entry : phoneBase.entrySet()) {
            String p = entry.getKey();
            String f = entry.getValue();
            base += f + "\t" + p + "\n";
        }
        return base;
    }
}
