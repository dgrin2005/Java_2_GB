/**
 * Java 2. Lesson 8. TicTacToe. Client part.
 *
 *  Крестики-нолики по сети
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-09-24
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class TicTacToeClient {
    public static void main(String[] args) {
        new TTTClient();
    }
}

class TTTClient implements TicTacToeConst {

    private int jbsLength;
    private JButton[] jbs;
    private JFrame formGame;
    private JPanel mapPanel;
    private JPanel statusBar;
    private JLabel statusText;
    private Socket socket;
    private PrintWriter pw;
    private BufferedReader reader;
    private Map map;
    private boolean needTurn; // признак необходимости хода текущего игрока
    private boolean endGame; // признак окончания игровой сессии
    private String str;
    private Player player;
    private int currentServerPort;
    private String currentServerAddress;
    private boolean OpponentAI;

    TTTClient() {
        endGame = true;
        currentServerAddress = SERVER_ADDRESS;
        currentServerPort = SERVER_PORT;
        formGame = new JFrame();
        formGame.setTitle(GAME_TITLE);
        formGame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        formGame.setBounds(300, 300, 400, 450);
        mapPanel = new JPanel();
        statusBar = new JPanel();
        statusText = new JLabel();
        statusText.setText(NEW_GAME);
        statusBar.add(statusText);
        formGame.setLayout(new BorderLayout());
        formGame.add(mapPanel, BorderLayout.CENTER);
        formGame.add(statusBar,BorderLayout.SOUTH);

        // создаем верхнее меню
        JMenuBar menuBar = new JMenuBar();
        JMenu mainMenu = new JMenu(MENU_TITLE);
        JMenuItem newGame = new JMenuItem(NEW_GAME_TITLE);
        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (endGame) {
                    OpponentAI = false;
                    Thread gameSession = new GameSession();
                    gameSession.start();
                }
            }
        });
        mainMenu.add(newGame);
        JMenuItem newGameAI = new JMenuItem(NEW_GAME_AI_TITLE);
        newGameAI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (endGame) {
                    OpponentAI = true;
                    Thread gameSession = new GameSession();
                    gameSession.start();
                }
            }
        });
        mainMenu.add(newGameAI);

        JMenuItem options = new JMenuItem(OPTIONS_TITLE);
        options.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {

                //отдельное окно с настройками игры
                JFrame formOptions = new JFrame(OPTIONS_TITLE);
                formOptions.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                formOptions.setBounds(300, 300, 250, 130);

                formOptions.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent event) {
                        formOptions.setVisible(false);
                        formGame.setVisible(true);
                    }
                });

                // Создание текстовых полей
                JTextField serverAddressField = new JTextField(20);
                serverAddressField.setToolTipText(SERVER_ADDRESS_TITLE);
                serverAddressField.setText(currentServerAddress);
                JTextField serverPortField = new JTextField(4);
                serverPortField.setToolTipText(SERVER_PORT_TITLE);
                serverPortField.setText(""+currentServerPort);
                JButton buttonOK = new JButton("OK");
                buttonOK.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (endGame) {
                            try {
                                currentServerAddress = serverAddressField.getText();
                                currentServerPort = Integer.parseInt(serverPortField.getText());
                                formGame.setVisible(true);
                                formOptions.setVisible(false);
                            } catch (NumberFormatException ex) {
                                // при некорректном вводе данных параметр не меняется
                            }
                        }
                    }
                });
                // Создание панели с текстовыми полями
                JPanel contents = new JPanel();
                contents.add(new JLabel(SERVER_ADDRESS_TITLE + ": "));
                contents.add(serverAddressField);
                contents.add(new JLabel(SERVER_PORT_TITLE + ":"));
                contents.add(serverPortField);
                contents.add(buttonOK);
                formOptions.add(contents);

                formGame.setVisible(false);
                formOptions.setVisible(true);
            }
        });
        mainMenu.add(options);

        mainMenu.addSeparator();
        JMenuItem exit = new JMenuItem(EXIT_TITLE);
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        mainMenu.add(exit);
        menuBar.add(mainMenu);
        formGame.setJMenuBar(menuBar);
        formGame.setVisible(true);
    }

    class GameSession extends Thread {

        @Override
        public void run() {
            try {
                jbsLength = 0;
                endGame = false;
                socket = new Socket(currentServerAddress, currentServerPort);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pw = new PrintWriter(socket.getOutputStream());
                statusText.setText(CONNECTION_WATING);
                pw.println(OpponentAI ? "1" : "0");
                pw.flush();
                str = reader.readLine();
                map = new Map(Integer.parseInt(str));
                needTurn = false;
                str = reader.readLine();
                player = new Player(str.charAt(0));
                statusText.setText(STATUS_WAITING_OPPONENT);

                // создаем и заполняем игровое поле
                jbsLength = map.getSize() * map.getSize();
                jbs = new JButton[jbsLength];
                mapPanel.setLayout(new GridLayout(map.getSize(), map.getSize()));
                for (int i = 0; i < jbsLength; i++) {
                    jbs[i] = new JButton("" + map.getDOT_EMPTY());
                    mapPanel.add(jbs[i]);
                    JButton jb = jbs[i];
                    jb.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (needTurn) {
                                for (int j = 0; j < jbsLength; j++) {
                                    if (jbs[j] == e.getSource()) {
                                        int x = ((j + 1) % map.getSize() == 0) ? map.getSize() : (j + 1) % map.getSize();
                                        int y = j / map.getSize() + 1;
                                        needTurn = player.turn(j, x - 1, y - 1);
                                        pw.println(x);
                                        pw.flush();
                                        pw.println(y);
                                        pw.flush();
                                        break;
                                    }
                                }
                                statusText.setText(STATUS_WAITING_OPPONENT);
                            }
                        }
                    });
                }
                formGame.setVisible(true);
                do {
                    str = "";
                    try {
                        str = reader.readLine();
                        if (str.equals("*")) {
                            needTurn = true;
                            statusText.setText(STATUS_WAITING_YOU);
                        } else if (str.equals("+")) {
                            needTurn = false;
                            endGame = true;
                            statusText.setText(NEW_GAME);
                            JOptionPane.showMessageDialog(null, STATUS_WIN, GAME_OVER, JOptionPane.INFORMATION_MESSAGE);
                        } else if (str.equals("-")) {
                            needTurn = false;
                            endGame = true;
                            statusText.setText(NEW_GAME);
                            JOptionPane.showMessageDialog(null, STATUS_LOST, GAME_OVER, JOptionPane.INFORMATION_MESSAGE);
                        } else if (str.equals("?")) {
                            needTurn = false;
                            endGame = true;
                            statusText.setText(NEW_GAME);
                            JOptionPane.showMessageDialog(null, STATUS_DRAW, GAME_OVER, JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            //ход противника
                            int opponent_X = Integer.parseInt(str);
                            int opponent_Y = Integer.parseInt(reader.readLine());
                            int j = (opponent_X - 1)  + (opponent_Y - 1) * map.getSize();
                            player.turnOpponent(j, opponent_X - 1, opponent_Y - 1);
                        }
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, CONNECTION_ERROR_CLIENT, GAME_OVER, JOptionPane.ERROR_MESSAGE);
                        statusText.setText(NEW_GAME);
                        endGame = true;
                    }
                } while (!endGame);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, CONNECTION_ERROR_CLIENT, GAME_OVER, JOptionPane.ERROR_MESSAGE);
                statusText.setText(NEW_GAME);
                endGame = true;
            }
            for (int i = 0; i < jbsLength; i++)
                mapPanel.remove(jbs[i]);
            formGame.repaint();
        }
    }

    class Player {
        private char dotType;
        private char dotTypeOpponent;

        Player(char dotType) {
            this.dotType = dotType;
            dotTypeOpponent = (dotType == map.DOT_X) ? map.DOT_O : map.DOT_X;
        }

        boolean turn(int j, int x, int y) {
            JButton jb = jbs[j];
            if (map.isCellValid(x, y)) {
                jb.setText("" + getDotType());
                map.setDot(x, y, getDotType());
                return false;
            } else return true;
        }

        void turnOpponent(int j, int x, int y) {
            JButton jb = jbs[j];
            jb.setText("" + getDotTypeOpponent());
            map.setDot(x, y, getDotTypeOpponent());
        }

        char getDotType() {
            return dotType;
        }

        char getDotTypeOpponent() {
            return dotTypeOpponent;
        }
    }

    class Map {

        private int size;
        private char[][] map;
        private final char DOT_EMPTY = '.';
        private final char DOT_X = 'x';
        private final char DOT_O = 'o';

        Map(int size) {
            this.size = size;
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

        void setDot(int x, int y, char dotType) {
            map[y][x] = dotType;
        }

        int getSize() {
            return size;
        }

        char getDOT_EMPTY() {
            return DOT_EMPTY;
        }

    }

}
