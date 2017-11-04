/**
 * Java 2. Lesson 7. Client
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-09-21
 */

import java.io.*;
import java.util.*;
import java.net.*;

public class Lesson7 {

    public static void main(String[] args) {
        new ChatClient();
    }
}


class ChatClient implements Lesson7Const{

    ChatClient() {

        Socket socket = null;
        boolean auth = false;
        int authCount = 0;
        int connectCount = 0;
        do {
            try {
                socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                System.out.println(CONNECT_TO_SERVER);
                do {
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());
                    pw.println(getLoginAndPassword());
                    pw.flush();
                    Scanner in = new Scanner(socket.getInputStream());
                    String str = "";
                    if (in.hasNext()) {
                        str = in.nextLine ();
                        System.out.println(str);
                    }
                    if (str.equalsIgnoreCase(SERVER_USER + ": " + SERVER_STOP)) {
                        authCount = AUTH_COUNT;
                        System.out.println(CONNECT_CLOSED);
                    } else if (str.equals(AUTH_SUCCESS)) {
                        System.out.print(CLIENT_PROMPT);
                        Thread clientRead = new ClientRead(socket);
                        clientRead.start();
                        Thread clientWrite = new ClientWrite(socket);
                        clientWrite.start();
                        connectCount = WAITING_SERVER_COUNT;
                        authCount = AUTH_COUNT;
                        auth = true;
                    } else authCount++;
                } while (!auth && authCount < AUTH_COUNT);
                if (!auth) {
                    System.out.println(AUTH_ERROR);
                    System.out.println(SHUTTING_DOWN);
                    connectCount = WAITING_SERVER_COUNT;
                    socket.close();
                }
            } catch (IOException ex) {
                System.out.println(CONNECTION_ERROR);
                try {
                    Thread.sleep(WAITING_SERVER_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                connectCount++;
            }
        } while (connectCount < WAITING_SERVER_COUNT);
        if (socket == null && authCount == 0) {
            System.out.println(SERVER_NOT_RESPONDING);
            System.out.println(SHUTTING_DOWN);
        }
    }

    String getLoginAndPassword() {
        String login = "";
        String password = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print(LOGIN_PROMPT);
            login = reader.readLine();
            System.out.print(PASSWD_PROMPT);
            password = reader.readLine();
        } catch (IOException e) {
        }
        return AUTH_COMMAND + " " + login + " " + password;
    }

    class ClientRead extends Thread {
        Socket socket;

        @Override
        public void run() {
            try {
                String str = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                do {
                    str = reader.readLine();
                    System.out.print(str.equals("\0")? CLIENT_PROMPT : str + "\n");
                } while(!str.equalsIgnoreCase(SERVER_USER + ": " + SERVER_STOP));
                System.out.println(CONNECT_CLOSED);
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
                System.out.println(SHUTTING_DOWN);
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

