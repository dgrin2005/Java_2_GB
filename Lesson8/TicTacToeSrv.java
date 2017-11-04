/**
 * Java 2. Lesson 8. TicTacToe. Server part.
 *
 *  Крестики-нолики по сети
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-09-24
 */

import java.io.*;
import java.net.*;
import java.util.Random;


public class TicTacToeSrv {

    public static void main(String[] args) {
        new TTTServer(args);
    }
}

class TTTServer implements TicTacToeConst{

    private ServerSocket serv;
    private Socket user1;
    private Socket user2;
    private Map map;
    private PrintWriter pw1;
    private PrintWriter pw2;
    private BufferedReader reader1;
    private BufferedReader reader2;
    private int currentMapSize;
    private int currentDotsToWin;
    private int currentServerPort;
    private boolean gameStarted;
    private Thread gameHandler;
    private boolean OpponentAI;

    public int getCurrentMapSize() {
        return currentMapSize;
    }

    public void setCurrentMapSize(int currentMapSize) {
        this.currentMapSize = currentMapSize;
    }

    public int getCurrentDotsToWin() {
        return currentDotsToWin;
    }

    public void setCurrentDotsToWin(int currentDotsToWin) {
        this.currentDotsToWin = currentDotsToWin;
    }

    public int getCurrentServerPort() {
        return currentServerPort;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    TTTServer(String[] args) {

        currentMapSize = SIZE;
        currentDotsToWin = DOTS_TO_WIN;
        if (args.length > 0) {
            try {
                currentServerPort = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                currentServerPort = SERVER_PORT;
            }
        } else currentServerPort = SERVER_PORT;
        Thread commandHandler = new CommandHandler();
        commandHandler.start();
        gameHandler = new GameHandler();
        gameHandler.start();

    }

    class Map {

        private int size;
        private int dots_to_win;
        private char[][] map;
        private final char DOT_EMPTY = '.';
        private final char DOT_X = 'x';
        private final char DOT_O = 'o';

        Map(int size, int dots_to_win) {
            this.size = size;
            this.dots_to_win = dots_to_win;
            map = new char[size][size];
            clearMap();
        }

        void clearMap() {
            for (int i = 0; i < size; i++) {
                map[i][i] = DOT_EMPTY;
                for (int j = 0; j < i; j++) {
                    map[i][j] = DOT_EMPTY;
                    map[j][i] = DOT_EMPTY;
                }
            }
        }

        boolean isCellValid(int x, int y) {
            return (map[y][x] == DOT_EMPTY);
        }

        boolean isMapFull() {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (map[i][j] == DOT_EMPTY) return false;
                }
            }
            return true;
        }

        boolean checkWin(char dot) {
            int i, j, k;
            boolean result;
        /* проверяем все возможные вертикальные и горизонатльные "полоски" длиной dots_to_win
         */
            for (i = 0; i <= size - dots_to_win; i++) {
                for (j = 0; j < size; j++) {
                    result = true;
                    k = 0;
                    while (k < dots_to_win && result) {
                        result = map[i + k][j] == dot;
                        k++;
                    }
                    if (result) return true;
                    result = true;
                    k = 0;
                    while (k < dots_to_win && result) {
                        result = map[j][i + k] == dot;
                        k++;
                    }
                    if (result) return true;
                }
            }
        /* проверяем все возможные диагональные "полоски" длиной dots_to_win
         */
            for (i = 0; i <= size - dots_to_win; i++) {
                for (j = 0; j <= size - dots_to_win; j++) {
                    result = true;
                    k = 0;
                    while (k < dots_to_win && result) {
                        result = map[i + k][j + k] == dot;
                        k++;
                    }
                    if (result) return true;
                    result = true;
                    k = 0;
                    while (k < dots_to_win && result) {
                        result = map[i + dots_to_win - k - 1][j + k] == dot;
                        k++;
                    }
                    if (result) return true;
                }
            }
            return false;
        }

        void setDot(int x, int y, char dotType) {
            map[y][x] = dotType;
        }

        int getSize() {
            return size;
        }

        char getDOT_X() {
            return DOT_X;
        }

        char getDOT_O() {
            return DOT_O;
        }

        private void printMap() {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    System.out.print(map[i][j]+" ");
                }
                System.out.println();
            }
        }

        private void AITurn(int[] aiTurn) {

            char dotType = getDOT_O();
            char dotTypeAnother = getDOT_X();
        /* проверяем может ли выиграть игрок сдедующим ходом;
           если находим такую ячейку, то очередным ходом ставим туда фишку
        */
            if (!checkWinNextStep(dotType, aiTurn)) {
            /* проверяем может ли выиграть сдедующим ходом противник;
               если находим такую ячейку, то очередным ходом ставим туда фишку
            */
                if (!checkWinNextStep(dotTypeAnother, aiTurn)) {
                /* ставим фишку в произвольную пустую ячейку
                */
                    findNextStep(aiTurn);
                }
            }
        }


        private boolean checkWinNextStep(char dot, int[] field) {
            int i, j;
            boolean findCell = false;
            i = 0;
            while (i < size && !findCell) {
                j = 0;
                while (j < size && !findCell) {
                    if (map[i][j] == DOT_EMPTY) {
                        map[i][j] = dot;
                        findCell = checkWin(dot);
                        if (findCell) {
                            field[0] = j; //x
                            field[1] = i; //y
                        }
                        map[i][j] = DOT_EMPTY;
                    }
                    j++;
                }
                i++;
            }
            return findCell;
        }

        private void findNextStep(int[] field) {
            Random rand = new Random();
        /* ставим О в произвольную пустую ячейку
        */
            do {
                field[0] = rand.nextInt(size);
                field[1] = rand.nextInt(size);
            } while (!isCellValid(field[0], field[1]));
        }


    }

    class CommandHandler extends Thread {
        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String str = "";
                do {
                    str = reader.readLine();
                    if (str.startsWith(SET_COMMAND)) {
                        if (!isGameStarted()) {
                            String a1 = str.trim();
                            String[] wds = a1.split(" ");
                            String ms = wds[1];
                            String a2 = a1.substring(SET_COMMAND.length()).trim();
                            String dtw = a2.substring(ms.length()).trim();
                            int newMapSize;
                            int newDotsToWin;
                            try {
                                newMapSize = Integer.parseInt(ms);
                                newDotsToWin = Integer.parseInt(dtw);
                            } catch (NumberFormatException e) {
                                newMapSize = getCurrentMapSize();
                                newDotsToWin = getCurrentDotsToWin();
                            }
                            setCurrentMapSize(newMapSize);
                            setCurrentDotsToWin(newDotsToWin);
                            System.out.println(PRINT_SIZE + ": " + getCurrentMapSize() + "x" + getCurrentMapSize());
                            System.out.println(PRINT_DOTS_TO_WIN + ": " + getCurrentDotsToWin());
                        }
                    } else if (str.equalsIgnoreCase(GET_COMMAND)) {
                        System.out.println(PRINT_SIZE + ": " + getCurrentMapSize() + "x" + getCurrentMapSize());
                        System.out.println(PRINT_DOTS_TO_WIN + ": " + getCurrentDotsToWin());
                        System.out.println(PRINT_SERVER_PORT + ": " + getCurrentServerPort());
                    }
                } while (!str.equalsIgnoreCase(EXIT_COMMAND));
                System.out.println(SHUTTING_DOWN);
                if (user1 != null) user1.close();
                if (user2 != null) user2.close();
                serv.close();
                System.exit(0);
            } catch (Exception ex) {
            }

        }
    }

    class GameHandler extends  Thread {
        @Override
        public void run() {
            try {
                serv = new ServerSocket(getCurrentServerPort());
                System.out.println(SERVER_START);
                while (true) {
                    gameStarted = false;
                    user1 = serv.accept();
                    System.out.println(USER1 + " " + CLIENT_JOINED);
                    pw1 = new PrintWriter(user1.getOutputStream());
                    reader1 = new BufferedReader(new InputStreamReader(user1.getInputStream()));
                    String str1 = reader1.readLine();
                    String str2;
                    OpponentAI = str1.equals("1");
                    if (!OpponentAI) {
                        do {
                            user2 = serv.accept();
                            System.out.println(USER2 + " " + CLIENT_JOINED);
                            pw2 = new PrintWriter(user2.getOutputStream());
                            reader2 = new BufferedReader(new InputStreamReader(user2.getInputStream()));
                            str2 = reader2.readLine();
                            if (!str1.equals(str2)) {
                                //Если первый игрок подключается для игры с человеком, а второй - с AI,
                                // то второго отключаем
                                System.out.println(USER2 + " " + CLIENT_DISCONNECTED);
                                pw2.close();
                                reader2.close();
                                user2.close();
                            }
                        }while (!str1.equals(str2));
                    }
                    map = new Map(currentMapSize, currentDotsToWin);
                    System.out.println(GAME_SESSION_STARTED);
                    gameStarted = true;
                    pw1.println(map.getSize());
                    pw1.flush();
                    pw1.println(map.getDOT_X());
                    pw1.flush();
                    if (!OpponentAI) {
                        pw2.println(map.getSize());
                        pw2.flush();
                        pw2.println(map.getDOT_O());
                        pw2.flush();
                    }
                    boolean continueGame = true;
                    int winState = 0;
                    while (continueGame) {
                        pw1.println("*");
                        pw1.flush();
                        int user1_X = 0;
                        int user1_Y = 0;
                        try {
                            user1_X = Integer.parseInt(reader1.readLine());
                            user1_Y = Integer.parseInt(reader1.readLine());
                            System.out.println(USER1 + " " + MOVE + ": " + user1_X + ":" + user1_Y);
                            map.setDot(user1_X - 1, user1_Y - 1, map.getDOT_X());
                            map.printMap();
                            if (!OpponentAI) {
                                pw2.println(user1_X);
                                pw2.flush();
                                pw2.println(user1_Y);
                                pw2.flush();
                            }
                            if (map.checkWin(map.getDOT_X())) {
                                continueGame = false;
                                winState = 1;
                            } else if (map.isMapFull()) {
                                continueGame = false;
                            } else {
                                int user2_X;
                                int user2_Y;
                                int[] aiTurn = new int[2];
                                if (!OpponentAI) {
                                    pw2.println("*");
                                    pw2.flush();
                                    user2_X = Integer.parseInt(reader2.readLine());
                                    user2_Y = Integer.parseInt(reader2.readLine());
                                } else {
                                    map.AITurn(aiTurn);
                                    user2_X = aiTurn[0] + 1;
                                    user2_Y = aiTurn[1] + 1;
                                }
                                System.out.println(USER2 + " " + MOVE + ": " + user2_X + ":" + user2_Y);
                                map.setDot(user2_X - 1, user2_Y - 1, map.getDOT_O());
                                map.printMap();
                                pw1.println(user2_X);
                                pw1.flush();
                                pw1.println(user2_Y);
                                pw1.flush();
                                if (map.checkWin(map.getDOT_O())) {
                                    continueGame = false;
                                    winState = 2;
                                } else if (map.isMapFull()) {
                                    continueGame = false;
                                }
                            }
                        } catch (IOException e) {
                            continueGame = false;
                            winState = -1;
                        }

                        if (!continueGame) {
                            switch (winState) {
                                case -1: {
                                    System.out.println(CONNECTION_ERROR);
                                    pw1.println("?");
                                    pw1.flush();
                                    if (!OpponentAI) {
                                        pw2.println("?");
                                        pw2.flush();
                                    }
                                    break;
                                }
                                case 1: {
                                    System.out.println(USER1 + " " + WIN);
                                    pw1.println("+");
                                    pw1.flush();
                                    if (!OpponentAI) {
                                        pw2.println("-");
                                        pw2.flush();
                                    }
                                    break;
                                }
                                case 2: {
                                    System.out.println(USER2 + " " + WIN);
                                    pw1.println("-");
                                    pw1.flush();
                                    if (!OpponentAI) {
                                        pw2.println("+");
                                        pw2.flush();
                                    }
                                    break;
                                }
                                default: {
                                    System.out.println(DRAW);
                                    pw1.println("?");
                                    pw1.flush();
                                    if (!OpponentAI) {
                                        pw2.println("?");
                                        pw2.flush();
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    pw1.close();
                    reader1.close();
                    user1.close();
                    if (!OpponentAI) {
                        pw2.close();
                        reader2.close();
                        user2.close();
                    }
                    System.out.println(GAME_SESSION_STOPPED);
                }

            } catch (IOException e) {
            }
            System.out.println(SERVER_STOP);
        }
    }


}
