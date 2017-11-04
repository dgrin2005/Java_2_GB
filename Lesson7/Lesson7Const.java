/**
 * Java 2. Lesson 7. Constantes
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-09-21
 */

public interface Lesson7Const {

    final String SERVER_START = "Server is started. Waiting for connections.";
    final String SERVER_STOP = "Server stopped.";
    final String CONNECT_TO_SERVER = "Connection to server established.";
    final String CONNECT_CLOSED = "Connection closed.";
    final String SERVER_USER = "Server";
    final String CLIENT_NUMBER = "Client #";
    final String CLIENT_JOINED = " joined.";
    final String CLIENT_DISCONNECTED = " disconnected.";
    final String CLIENT_CONNECTION_ERROR = "Client connection error.";
    final String SERVER_INIT_ERROR = "Server initialization error.";
    final String SEND_MESSAGE_ERROR = "Could not send message to user ";
    final String CONNECTION_ERROR = "Could not connect to the server.";
    final String SERVER_NOT_RESPONDING = "The server is not responding.";
    final String SHUTTING_DOWN = " Shutting down...";

    final String AUTH_FAIL = "Authentication failure.";
    final String AUTH_SUCCESS = "Authentication success.";
    final String AUTH_ERROR = "Authorization attempts exceeded.";
    final int AUTH_COUNT = 5;

    final String CLIENT_PROMPT = "$ ";
    final String LOGIN_PROMPT = "Login: ";
    final String PASSWD_PROMPT = "Password: ";

    final String AUTH_COMMAND = "auth";
    final String ADD_USER_COMMAND = "add";
    final String REMOVE_USER_COMMAND = "remove";
    final String LIST_USERS_COMMAND = "list";
    final String LIST_CONNECTED_USERS_COMMAND = "listc";
    final String EXIT_COMMAND = "exit";
    final String SEND_COMMAND = "/w ";
    final String SENDALL_COMMAND = "/a ";

    final int SERVER_PORT = 8189;
    final String SERVER_ADDRESS = "localhost";
    final int WAITING_SERVER_TIME = 1000;
    final int WAITING_SERVER_COUNT = 20;

    final String DRIVER_NAME = "org.sqlite.JDBC";
    final String SQLITE_DB = "jdbc:sqlite:chat.db";
    final String TABLE_NAME = "users";
    final String LOGIN_COL = "login";
    final String PASSWORD_COL = "password";
    final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            "(" + LOGIN_COL +"  CHAR(6) PRIMARY KEY NOT NULL, " +
            PASSWORD_COL + " CHAR(6) NOT NULL);";
    final String SQL_SELECT = "SELECT * FROM " + TABLE_NAME + ";";
    final String SQL_SELECT_AUTH = "SELECT * FROM " + TABLE_NAME + " WHERE " + LOGIN_COL + " = '?'";
    final String SQL_INSERT_USER = "INSERT INTO " + TABLE_NAME +
            " (" + LOGIN_COL + ", " + PASSWORD_COL + ") " + "VALUES ('?', '!');";
    final String SQL_DELETE_USER = "DELETE FROM " + TABLE_NAME + " WHERE " + LOGIN_COL + " = '?'";

}
