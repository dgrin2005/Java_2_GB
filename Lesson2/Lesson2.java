import java.io.*;

/**
 * Java 2. Lesson 2.
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-09-04
 */

public class Lesson2 {

    final static int ARRAY_SIZE = 4;
    final static String ARRAY_PATH = "array.txt";
    final static String SEPARATOR = " ";

    public static void main(String[] args) {

        //String[][] arr = {{"1","2","3","4"}, {"1","2","3","4"}, {"1","2","3","4"}, {"1","2","3","4"}};
        String[][] arr = new String[ARRAY_SIZE][ARRAY_SIZE];
        try {
            readArray(arr);
        } catch (MyArraySizeException ex) {
            ex.printStackTrace();
        }
        int sum = 0;
        try {
            sum = sumArray(arr);
        } catch (MyArrayDataException ex) {
            ex.printStackTrace();
        } catch (MyArraySizeException ex) {
            ex.printStackTrace();
        }
        System.out.printf("Сумма чисел в массиве [" + ARRAY_SIZE + "][" + ARRAY_SIZE +"] = " + sum);
    }

    static void readArray(String[][] arr) throws MyArraySizeException {
        int i = 0;
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(ARRAY_PATH);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        try {
            while ((strLine = br.readLine()) != null){
                //проверяем если число строк массива больше ARRAY_SIZE
                if (i >= ARRAY_SIZE) try {
                    throw new MyArraySizeException(i, -1, ARRAY_SIZE);
                } catch (MyArraySizeException ex) {
                    ex.printStackTrace();
                } else {
                    arr[i] = strLine.split(SEPARATOR);
                    /* проверка на соответствие количества элементов в строке массива перенесена в
                       процедуру подсчета суммы элементов
                    if (arr[i].length != ARRAY_SIZE) try {
                        throw new MyArraySizeException(i, arr[i].length, ARRAY_SIZE);
                    } catch (MyArraySizeException ex) {
                        ex.printStackTrace();
                    }*/
                }
                System.out.println(strLine);
                i++;
            }
        } catch (IOException eх) {
            eх.printStackTrace();
        }
        //проверяем если число строк массива меньше ARRAY_SIZE
        if (i < ARRAY_SIZE) try {
            throw new MyArraySizeException(i - 1, -1, ARRAY_SIZE);
        } catch (MyArraySizeException ex) {
            ex.printStackTrace();
        }
    }

    static int sumArray(String[][] arr) throws MyArrayDataException, MyArraySizeException {

        int sum = 0;
        int a;
        for (int i = 0; i < ARRAY_SIZE; i++ ) {
            if (arr[i].length != ARRAY_SIZE) try {
                throw new MyArraySizeException(i, arr[i].length, ARRAY_SIZE);
            } catch (MyArraySizeException ex) {
                ex.printStackTrace();
            }
            for (int j = 0; j < (ARRAY_SIZE <= arr[i].length ? ARRAY_SIZE : arr[i].length); j++)
                try {
                    a = Integer.parseInt(arr[i][j]);
                    sum += a;
                } catch (NumberFormatException ex) {
                    try {
                        throw new MyArrayDataException(i, j);
                    } catch  (MyArrayDataException e) {
                        e.printStackTrace();
                    }
                }
        }
        return  sum;
    }

}


class MyArraySizeException extends Exception {

    public MyArraySizeException(int i, int j, int arraySize) {
        System.out.println("Ошибка размерности массива [" + arraySize + "][" + arraySize + "]");
        if (j == -1) {
            if (i + 1 > arraySize) {
                System.out.println("Обнаружена строка " + (i + 1));
            } else if (i + 1  < arraySize) {
                System.out.println("Всего " + (i + 1) + " строк");
            }
        } else {
            System.out.println("В строке " + (i + 1) + " имеется " + j + " элементов");
        }
    }

}

class MyArrayDataException extends Exception {

    public MyArrayDataException(int i, int j) {
        System.out.println("Ошибка преобразования в число элемента массива [" + (i + 1) + "][" + (j + 1) + "]");
    }

}