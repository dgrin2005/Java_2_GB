/**
 * Java 2. Lesson 6. Server
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-09-17
 */


import java.io.*;
import java.net.*;
import java.util.*;

public class Lesson6Srv {

    public static void main(String[] args) {
        new ChatServer();
    }
}

class ChatServer{

    private final int SERVER_PORT = 8189;
    private final String EXIT_COMMAND = "end";
    int client = 0;
    List<Socket> sockets = new ArrayList<>();

    ChatServer() {
        ServerSocket serv;
        Socket socket;
        try {
            serv = new ServerSocket(SERVER_PORT);
            System.out.println("������ �������, ������� �����������...");
            Thread serverWriteToAll = new ServerWriteToAll();
            serverWriteToAll.start();
            while (true) {
                try {
                    socket = serv.accept();
                    client++;
                    System.out.println("������ #" + client + " �����������");
                    Thread serverRead = new ServerRead(socket, client);
                    serverRead.start();
                    sockets.add(socket);
                } catch (IOException e) {
                    System.out.println("������ ����������� �������");
                }
            }
        } catch (IOException e) {
            System.out.println("������ ������������� �������");
        }
    }

    class ServerRead extends Thread {
        Socket socket;
        int client;

        @Override
        public void run() {
            try {
                String str = "";
                PrintWriter pw = new PrintWriter(socket.getOutputStream());
                do {
                    Scanner in = new Scanner(socket.getInputStream());
                    if (in.hasNext()) {
                        str = in.nextLine();
                        System.out.println("������ #" + client + ": " + str);
                        pw.println("���: " + str);
                        pw.flush();
                    }
                } while(!str.equalsIgnoreCase(EXIT_COMMAND));
                pw.close();
                sockets.remove(socket);
                socket.close();
                System.out.println("������ #" + client + " ����������");
            } catch (Exception ex) {
            }

        }

        ServerRead(Socket socket, int client) {
            this.socket = socket;
            this.client = client;
        }
    }

    class ServerWriteToAll extends Thread {

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter pw = null;
                String str = "";
                do {
                    str = reader.readLine();
                    for (Socket socket: sockets) {
                        pw = new PrintWriter(socket.getOutputStream());
                        pw.println("������: " + str);
                        pw.flush();
                    }
                } while (!str.equalsIgnoreCase(EXIT_COMMAND));
                reader.close();
                if (pw != null) pw.close();
                for (Socket socket: sockets) {
                    socket.close();
                }
                System.exit(0);
            } catch (Exception ex) {
            }

        }

    }


}