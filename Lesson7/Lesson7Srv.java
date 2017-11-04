/**
 * Java 2. Lesson 7. Server
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-09-21
 */


import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class Lesson7Srv {

    public static void main(String[] args) {
        new ChatServer();
    }
}

class ChatServer implements Lesson7Const{


    int clientNumbers = 0;
    Map<Integer, Socket> usersSockets = new HashMap<>();
    Map<String, Integer> usersNames = new HashMap<>();
    ServerSocket serv;

    ChatServer() {

        Socket socket;
        new InitDB();
        try {
            serv = new ServerSocket(SERVER_PORT);
            System.out.println(SERVER_START);
            System.out.print(CLIENT_PROMPT);
            Thread serverCommand = new ServerCommand();
            serverCommand.start();
            while (true) {
                try {
                    socket = serv.accept();
                    clientNumbers++;
                    System.out.println(CLIENT_NUMBER + clientNumbers + CLIENT_JOINED);
                    System.out.print(CLIENT_PROMPT);
                    Thread serverListener = new ServerListener(socket, clientNumbers);
                    serverListener.start();
                    usersSockets.put(clientNumbers, socket);
                } catch (IOException e) {
                    System.out.println(CLIENT_CONNECTION_ERROR);
                    System.out.print(CLIENT_PROMPT);
                }
            }
        } catch (IOException e) {
            System.out.println(SERVER_INIT_ERROR);
        }
    }

    class ServerListener extends Thread {
        Socket socket;
        int clientID;
        String name;

        @Override
        public void run() {
            try {
                String str = "";
                PrintWriter pw = new PrintWriter(socket.getOutputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                do {
                    str = reader.readLine();
                    System.out.println(CLIENT_NUMBER + clientID + " (" + name + "): " + str);
                    System.out.print(CLIENT_PROMPT);
                    if (!str.equalsIgnoreCase(EXIT_COMMAND)) {
                        if (str.startsWith(AUTH_COMMAND)) {
                            String[] wds = str.split(" ");
                            if (checkAuthentication(wds[1], wds[2])) {
                                System.out.println(CLIENT_NUMBER + clientID + " (" + name + "): " + AUTH_SUCCESS);
                                System.out.print(CLIENT_PROMPT);
                                name = wds[1];
                                pw.println(AUTH_SUCCESS);
                                pw.println("\0");
                                pw.flush();
                                usersNames.put(name, clientID);
                            } else {
                                System.out.println(CLIENT_NUMBER + clientID + " (" + name + "): " + AUTH_FAIL);
                                System.out.print(CLIENT_PROMPT);
                                pw.println(AUTH_FAIL);
                                pw.println("\0");
                                pw.flush();
                            }
                        } else if (str.startsWith(SEND_COMMAND)) {
                            String a1 = str.trim();
                            String[] wds = a1.split(" ");
                            String recipientName = wds[1];
                            String a2 = a1.substring(SEND_COMMAND.length()).trim();
                            String recipientString = a2.substring(recipientName.length()).trim();
                            try {
                                pw.println("\0");
                                pw.flush();
                                Socket recipientSocket = usersSockets.get(usersNames.get(recipientName));
                                PrintWriter pwr = new PrintWriter(recipientSocket.getOutputStream());
                                pwr.println(name + ": "+ recipientString);
                                pwr.println("\0");
                                pwr.flush();
                            } catch (Exception e) {
                                pw.println(SEND_MESSAGE_ERROR + recipientName);
                                pw.println("\0");
                                pw.flush();
                            }
                        }
                        else if (str.startsWith(SENDALL_COMMAND)) {
                            String a1 = str.trim();
                            String recipientString = a1.substring(SENDALL_COMMAND.length()).trim();
                            pw.println("\0");
                            pw.flush();
                            for (Map.Entry<String, Integer> entry : usersNames.entrySet()) {
                                String recipientName = entry.getKey();
                                try {
                                    Socket recipientSocket = usersSockets.get(usersNames.get(recipientName));
                                    PrintWriter pwr = new PrintWriter(recipientSocket.getOutputStream());
                                    pwr.println(name + ": "+ recipientString);
                                    pwr.println("\0");
                                    pwr.flush();
                                } catch (Exception e) {
                                    pw.println(SEND_MESSAGE_ERROR + recipientName);
                                    pw.println("\0");
                                    pw.flush();
                                }
                            }
                        } else if (str.equalsIgnoreCase(LIST_USERS_COMMAND)) {
                            try {
                                Class.forName(DRIVER_NAME);
                                Connection connect = DriverManager.getConnection(SQLITE_DB);
                                Statement stmt = connect.createStatement();
                                ResultSet rs = stmt.executeQuery(SQL_SELECT);
                                pw.println("LOGIN");
                                while (rs.next()) {
                                    pw.println(rs.getString(LOGIN_COL));
                                }
                                pw.println("\0");
                                pw.flush();
                            } catch (Exception e) { }
                        } else if (str.equalsIgnoreCase(LIST_CONNECTED_USERS_COMMAND)) {
                            pw.println("LOGIN");
                            for (Map.Entry<String, Integer> entry : usersNames.entrySet()) {
                                pw.println(entry.getKey());
                            }
                            pw.println("\0");
                            pw.flush();
                        } else {
                            pw.println("ECHO: " + str);
                            pw.println("\0");
                            pw.flush();
                        }
                    }
                } while(!str.equalsIgnoreCase(EXIT_COMMAND));
                pw.close();
                usersSockets.remove(clientID);
                usersNames.remove(name);
                socket.close();
                System.out.println(CLIENT_NUMBER + clientID + " (" + name + ")" + CLIENT_DISCONNECTED);
                System.out.print(CLIENT_PROMPT);
            } catch (Exception ex) {
            }

        }

        ServerListener(Socket socket, int clientID) {
            this.socket = socket;
            this.clientID = clientID;
            name = "";
        }
    }

    /**
     * checkAuthentication: check login and password
     */
    private boolean checkAuthentication(String login, String password) {
        Connection connect;
        boolean result = false;
        try {
            Class.forName(DRIVER_NAME);
            connect = DriverManager.getConnection(SQLITE_DB);
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_SELECT_AUTH.replace("?", login));
            while (rs.next())
                result = rs.getString(PASSWORD_COL).equals(password);
            rs.close();
            stmt.close();
            connect.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return result;
    }

    class ServerCommand extends Thread {

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter pw = null;
                String str = "";
                Class.forName(DRIVER_NAME);
                Connection connect = DriverManager.getConnection(SQLITE_DB);
                Statement stmt = connect.createStatement();
                do {
                    str = reader.readLine();
                    if (str.startsWith(ADD_USER_COMMAND)) {
                        String[] wds = str.split(" ");
                        String login = wds[1];
                        String password = wds[2];
                        String strDB = SQL_INSERT_USER.replace("?", login);
                        strDB = strDB.replace("!", password);
                        try {
                            stmt.executeUpdate(strDB);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    } else if (str.startsWith(REMOVE_USER_COMMAND)) {
                        String[] wds = str.split(" ");
                        String login = wds[1];
                        String strDB = SQL_DELETE_USER.replace("?", login);
                        try {
                            stmt.executeUpdate(strDB);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    } else if (str.equalsIgnoreCase(LIST_USERS_COMMAND)) {
                        try {
                            ResultSet rs = stmt.executeQuery(SQL_SELECT);
                            System.out.println("LOGIN\tPASSWORD");
                            while (rs.next()) {
                                System.out.println(rs.getString(LOGIN_COL) + "\t" +
                                        rs.getString(PASSWORD_COL));
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }  else if (str.equalsIgnoreCase(LIST_CONNECTED_USERS_COMMAND)) {
                        System.out.println("LOGIN");
                        for (Map.Entry<String, Integer> entry : usersNames.entrySet()) {
                            System.out.println(entry.getKey());
                        }
                    } if (str.startsWith(SENDALL_COMMAND)) {
                        String a1 = str.trim();
                        String recipientString = a1.substring(SENDALL_COMMAND.length()).trim();
                        for (Map.Entry<String, Integer> entry : usersNames.entrySet()) {
                            String recipientName = entry.getKey();
                            try {
                                Socket recipientSocket = usersSockets.get(usersNames.get(recipientName));
                                PrintWriter pwr = new PrintWriter(recipientSocket.getOutputStream());
                                pwr.println(SERVER_USER + ": "+ recipientString);
                                pwr.println("\0");
                                pwr.flush();
                            } catch (Exception e) {
                                System.out.println(SEND_MESSAGE_ERROR + recipientName);
                            }
                        }
                    } else if (str.startsWith(SEND_COMMAND)) {
                        String a1 = str.trim();
                        String[] wds = a1.split(" ");
                        String recipientName = wds[1];
                        String a2 = a1.substring(SEND_COMMAND.length()).trim();
                        String recipientString = a2.substring(recipientName.length()).trim();
                        try {
                            Socket recipientSocket = usersSockets.get(usersNames.get(recipientName));
                            PrintWriter pwr = new PrintWriter(recipientSocket.getOutputStream());
                            pwr.println(SERVER_USER + ": "+ recipientString);
                            pwr.println("\0");
                            pwr.flush();
                        } catch (Exception e) {
                            System.out.println(SEND_MESSAGE_ERROR + recipientName);
                        }
                    }
                    System.out.print(CLIENT_PROMPT);
                } while (!str.equalsIgnoreCase(EXIT_COMMAND));
                System.out.println(SHUTTING_DOWN);
                reader.close();
                for (Map.Entry<Integer, Socket> entry : usersSockets.entrySet()) {
                    pw = new PrintWriter(entry.getValue().getOutputStream());
                    pw.println(SERVER_USER + ": " + SERVER_STOP);
                    pw.println("\0");
                    pw.flush();
                    entry.getValue().close();
                    pw.close();
                }
                usersSockets.clear();
                usersNames.clear();
                System.exit(0);
            } catch (Exception ex) {
            }

        }

    }

    class InitDB implements Lesson7Const {

        final String SQL_INSERT_MIKE = "INSERT INTO " + TABLE_NAME +
                " (" + LOGIN_COL + ", " + PASSWORD_COL + ") " +
                "VALUES ('mike', 'qwerty');";
        final String SQL_INSERT_JONH = "INSERT INTO " + TABLE_NAME +
                " (" + LOGIN_COL + ", " + PASSWORD_COL + ") " +
                "VALUES ('john', '12345');";

        Connection connect;
        Statement stmt;
        ResultSet rs;

        InitDB() {
            // open db file
            try {
                Class.forName(DRIVER_NAME);
                connect = DriverManager.getConnection(SQLITE_DB);
            } catch (Exception e) { }

            // create table
            try {
                stmt = connect.createStatement();
                stmt.executeUpdate(SQL_CREATE_TABLE);
            } catch (Exception e) { }

            // insert record(s)
            try {
                stmt.executeUpdate(SQL_INSERT_MIKE);
                stmt.executeUpdate(SQL_INSERT_JONH);

            } catch (Exception e) { }

        }
    }
}