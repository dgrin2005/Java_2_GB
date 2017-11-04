/**
 * Java 2. Lesson 8. TicTacToe. Constants.
 *
 *  Крестики-нолики по сети
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-09-24
 */


public interface TicTacToeConst {

    // параметры сервера по умолчанию
    final int SERVER_PORT = 8189;
    final String SERVER_ADDRESS = "localhost";

    // сообщения сервера
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

    // интерфейс клиента
    final String GAME_TITLE = "Крестики-нолики";
    final String MENU_TITLE = "Меню";
    final String NEW_GAME_TITLE = "Новая игра с человеком";
    final String NEW_GAME_AI_TITLE = "Новая игра с AI";
    final String OPTIONS_TITLE = "Настройки";
    final String SERVER_PORT_TITLE = "Порт сервера";
    final String SERVER_ADDRESS_TITLE = "Адрес сервера";
    final String EXIT_TITLE = "Выйти";

    // сообщения клиента
    final String NEW_GAME = "Для запуска игры выберите в меню пункт 'Новая игра'";
    final String CONNECTION_WATING = "Ожидаем подключение соперника...";
    final String CONNECTION_ERROR_CLIENT = "Ошибка соединения";
    final String GAME_OVER = "Конец игры";
    final String STATUS_WIN = "Вы победили!";
    final String STATUS_LOST = "Вы проиграли!";
    final String STATUS_DRAW = "Ничья!";
    final String STATUS_WAITING_YOU = "Ваш ход ...";
    final String STATUS_WAITING_OPPONENT = "Ожидаем ход соперника...";

    //команды сервера
    final String EXIT_COMMAND = "exit"; // закрыть сервер
    final String SET_COMMAND = "set "; // set <map size> <dots ti win> - установить параметры игры
    final String GET_COMMAND = "get"; //получить параметры игры

    // параметры игры по умолчанию
    final int SIZE = 3; // map size
    final int DOTS_TO_WIN = 3; // dots to win
}
