/**
 * Java 2. Lesson 8. TicTacToe. Constants.
 *
 *  ��������-������ �� ����
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-09-24
 */


public interface TicTacToeConst {

    // ��������� ������� �� ���������
    final int SERVER_PORT = 8189;
    final String SERVER_ADDRESS = "localhost";

    // ��������� �������
    final String SERVER_START = "Server is started. Waiting for connections.";
    final String CLIENT_JOINED = "joined.";
    final String CLIENT_DISCONNECTED = "disconnected.";
    final String SERVER_STOP = "Server stopped.";
    final String SHUTTING_DOWN = " Shutting down...";
    final String USER1 = "User1";
    final String USER2 = "User2";
    final String MOVE = "move";
    final String WIN = "win";
    final String DRAW = "Draw";
    final String GAME_SESSION_STARTED = "Game session started.";
    final String GAME_SESSION_STOPPED = "Game session stopped.";
    final String CONNECTION_ERROR = "Connection error";
    final String PRINT_SIZE = "Map size";
    final String PRINT_DOTS_TO_WIN = "Dots to win";
    final String PRINT_SERVER_PORT = "Server port";

    // ��������� �������
    final String GAME_TITLE = "��������-������";
    final String MENU_TITLE = "����";
    final String NEW_GAME_TITLE = "����� ���� � ���������";
    final String NEW_GAME_AI_TITLE = "����� ���� � AI";
    final String OPTIONS_TITLE = "���������";
    final String SERVER_PORT_TITLE = "���� �������";
    final String SERVER_ADDRESS_TITLE = "����� �������";
    final String EXIT_TITLE = "�����";

    // ��������� �������
    final String NEW_GAME = "��� ������� ���� �������� � ���� ����� '����� ����'";
    final String CONNECTION_WATING = "������� ����������� ���������...";
    final String CONNECTION_ERROR_CLIENT = "������ ����������";
    final String GAME_OVER = "����� ����";
    final String STATUS_WIN = "�� ��������!";
    final String STATUS_LOST = "�� ���������!";
    final String STATUS_DRAW = "�����!";
    final String STATUS_WAITING_YOU = "��� ��� ...";
    final String STATUS_WAITING_OPPONENT = "������� ��� ���������...";

    //������� �������
    final String EXIT_COMMAND = "exit"; // ������� ������
    final String SET_COMMAND = "set "; // set <map size> <dots ti win> - ���������� ��������� ����
    final String GET_COMMAND = "get"; //�������� ��������� ����

    // ��������� ���� �� ���������
    final int SIZE = 3; // map size
    final int DOTS_TO_WIN = 3; // dots to win
}
