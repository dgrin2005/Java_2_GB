/**
 * Java 2. Lesson 6. Client
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-09-17
 */

import java.io.*;
import java.util.*;
import java.net.*;

public class Lesson6 {

    public static void main(String[] args) {
        new ChatClient();
    }
}


class ChatClient {

    private final int SERVER_PORT = 8189;
    private final String SERVER_HOST = "localhost";
    private final String EXIT_COMMAND = "end";
    private final int WAITING_SERVER_TIME = 1000;
    private final int WAITING_SERVER_COUNT = 20;

    ChatClient() {

        Socket socket = null;
        int i = 0;
        do {
            try {
                socket = new Socket(SERVER_HOST, SERVER_PORT);
                System.out.println("Подключились к серверу");
                Thread clientRead = new ClientRead(socket);
                clientRead.start();
                Thread clientWrite = new ClientWrite(socket);
                clientWrite.start();
                i = WAITING_SERVER_COUNT;
            } catch (IOException ex) {
                System.out.println("Не удалось подключиться к серверу");
                try {
                    Thread.sleep(WAITING_SERVER_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        } while (i < WAITING_SERVER_COUNT);
        if (socket == null) System.out.println("Сервер не отвечает. Завершение работы.");
     }

    class ClientRead extends Thread {
        Socket socket;

        @Override
        public void run() {
            try {
                String str = "";
                do {
                    Scanner in = new Scanner(socket.getInputStream());
                    if (in.hasNext()) {
                        str = in.nextLine ();
                        System.out.println(str);
                    }
                } while(!str.equalsIgnoreCase(EXIT_COMMAND));
                System.out.println("Отключились от сервера");
                socket.close();
            } catch (Exception ex) {
            }
        }
        ClientRead(Socket socket) {
            this.socket = socket;
        }
    }

    class ClientWrite extends Thread {
        Socket socket;

        @Override
        public void run() {
            try {
                String str = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter pw = new PrintWriter(socket.getOutputStream());
                do {
                    str = reader.readLine();
                    pw.println(str);
                    pw.flush();
                } while (!str.equalsIgnoreCase(EXIT_COMMAND));
                pw.close();
                reader.close();
                socket.close();
            } catch (Exception ex) {
            }

        }
        ClientWrite(Socket socket) {
            this.socket = socket;
        }
    }

}

