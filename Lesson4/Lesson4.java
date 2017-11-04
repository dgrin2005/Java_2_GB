/**
 * Java 2. Lesson 4.
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-09-10
 */

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;

public class Lesson4 {

    public static void main(String[] args) {
        new Chat();
    }
}

class Chat extends JFrame implements ActionListener, ChatSettings {

    JTextField enterTextField;
    JTextPane chatText;
    BufferedWriter bw;
    SimpleAttributeSet ChatStyle;
    boolean currentChatTextItalic = CHAT_TEXT_ITALIC;
    boolean currenetChatTextBold = CHAT_TEXT_BOLD;
    int currentChatTextSize = CHAT_TEXT_SIZE;
    String currentChatTextColor = CHAT_TEXT_COLOR;
    String currentChatBackgroundColor = CHAT_BACKGROUND_COLOR;
    String currentFontName = CHAT_FONT_NAME;


    Chat() {
        setTitle(CHAT_TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(CHAT_CORNER_X, CHAT_CORNER_Y, CHAT_WIDTH, CHAT_HEIGHT);
        setLayout(new BorderLayout());

        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatText = new JTextPane();
        chatText.setEditable(false);
        chatText.setContentType("text/html");
        chatText.setBackground(getColor(currentChatBackgroundColor));
        JScrollPane chatScroll = new JScrollPane(chatText);
        chatPanel.add(chatScroll);
        add(chatPanel, BorderLayout.CENTER);

        JPanel enterPanel = new JPanel();
        enterPanel.setLayout(new BorderLayout());
        enterTextField = new JTextField();
        enterPanel.add(enterTextField, BorderLayout.CENTER);

        enterTextField.addActionListener (this);
        JButton enterButton = new JButton ("Enter");
        enterButton.addActionListener (this);
        enterPanel.add(enterButton, BorderLayout.EAST);
        add(enterPanel, BorderLayout.SOUTH);

        ChatStyle = new SimpleAttributeSet();
        StyleConstants.setItalic(ChatStyle, currentChatTextItalic);
        StyleConstants.setBold(ChatStyle, currenetChatTextBold);
        StyleConstants.setForeground(ChatStyle, getColor(currentChatTextColor));
        StyleConstants.setFontSize(ChatStyle, currentChatTextSize);
        StyleConstants.setFontFamily(ChatStyle, currentFontName);

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.X_AXIS));
        JLabel lSize = new JLabel();
        lSize.setText(" Размер шрифта " + currentChatTextSize + " ");
        settingsPanel.add(lSize);
        JButton bSizeUp = new JButton();
        bSizeUp.setText("A+");
        bSizeUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentChatTextSize++;
                if (currentChatTextSize > 25) currentChatTextSize = 25;
                StyleConstants.setFontSize(ChatStyle, currentChatTextSize);
                lSize.setText(" Размер шрифта " + currentChatTextSize + " ");
            }
        });
        settingsPanel.add(bSizeUp);
        JButton bSizeDown = new JButton();
        bSizeDown.setText("A-");
        settingsPanel.add(bSizeDown);
        bSizeDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentChatTextSize--;
                if (currentChatTextSize < 8) currentChatTextSize = 8;
                StyleConstants.setFontSize(ChatStyle, currentChatTextSize);
                lSize.setText(" Размер шрифта " + currentChatTextSize + " ");
            }
        });
        JComboBox<String> colorChooserFontName = new JComboBox<>(FONT_NAMES);
        colorChooserFontName.setSelectedIndex(Arrays.asList(FONT_NAMES).indexOf(currentFontName));
        colorChooserFontName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Font font = new Font((String) colorChooserFontName.getSelectedItem(), 0, currentChatTextSize);
                currentFontName = font.getFontName();
                StyleConstants.setFontFamily(ChatStyle, currentFontName);
            }
        });
        settingsPanel.add(colorChooserFontName);
        JCheckBox bItalic = new JCheckBox();
        bItalic.setText("Курсив");
        bItalic.setBorderPainted(true);
        bItalic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentChatTextItalic = !currentChatTextItalic;
                StyleConstants.setItalic(ChatStyle, currentChatTextItalic);
            }
        });
        settingsPanel.add(bItalic);
        JCheckBox bBold = new JCheckBox();
        bBold.setText("Жирный");
        bBold.setBorderPainted(true);
        bBold.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currenetChatTextBold = !currenetChatTextBold;
                StyleConstants.setBold(ChatStyle, currenetChatTextBold);
            }
        });
        settingsPanel.add(bBold);
        JLabel lColorChooserFont = new JLabel();
        lColorChooserFont.setText(" Цвет шрифта ");
        settingsPanel.add(lColorChooserFont);
        JComboBox<String> colorChooserFont = new JComboBox<>(COLOR_CHOOSER);
        colorChooserFont.setSelectedIndex(Arrays.asList(COLOR_CHOOSER).indexOf(currentChatTextColor));
        colorChooserFont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StyleConstants.setForeground(ChatStyle, getColor((String) colorChooserFont.getSelectedItem()));
            }
        });
        settingsPanel.add(colorChooserFont);
        JLabel lColorChooserBack = new JLabel();
        lColorChooserBack.setText(" Цвет фона ");
        settingsPanel.add(lColorChooserBack);
        JComboBox<String> colorChooserBack = new JComboBox<>(COLOR_CHOOSER);
        colorChooserBack.setSelectedIndex(Arrays.asList(COLOR_CHOOSER).indexOf(currentChatBackgroundColor));
        colorChooserBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chatText.setBackground(getColor((String) colorChooserBack.getSelectedItem()));
            }
        });
        settingsPanel.add(colorChooserBack);
        add(settingsPanel, BorderLayout.NORTH);

        setVisible (true);

        try {
            FileOutputStream fos = new FileOutputStream(FILE_PATH, true);
            bw = new BufferedWriter(new OutputStreamWriter(fos, "Cp1251"));

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!enterTextField.getText().trim().equals("")) {
            try {
                bw.append(enterTextField.getText() + "\n");
                bw.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            StyledDocument doc = chatText.getStyledDocument();
            try {
                doc.insertString(doc.getLength(), enterTextField.getText() + "\n", ChatStyle);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
            enterTextField.setText("");
            enterTextField.requestFocusInWindow();
        }
    }

    private Color getColor(String color) {
        switch (color) {
            case "RED" : return Color.RED;
            case "ORANGE" : return Color.ORANGE;
            case "YELLOW" : return Color.YELLOW;
            case "GREEN" : return Color.GREEN;
            case "CYAN" : return Color.CYAN;
            case "BLUE" : return Color.BLUE;
            case "MAGENTA" : return Color.MAGENTA;
            case "PINK" : return Color.PINK;
            case "WHITE" : return Color.WHITE;
            case "LIGHT GRAY" : return Color.LIGHT_GRAY;
            case "DARK GRAY" : return Color.DARK_GRAY;
            case "BLACK" : return Color.BLACK;
            default : return Color.WHITE;
        }
    }
}

interface ChatSettings {

    final static int CHAT_CORNER_X = 200;
    final static int CHAT_CORNER_Y = 200;
    final static int CHAT_WIDTH = 920;
    final static int CHAT_HEIGHT = 600;
    final static String CHAT_TITLE = "Chat Window";
    final static String CHAT_BACKGROUND_COLOR = "DARK GRAY";
    final static String CHAT_TEXT_COLOR = "WHITE";
    final static boolean CHAT_TEXT_ITALIC = false;
    final static boolean CHAT_TEXT_BOLD = false;
    final static int CHAT_TEXT_SIZE = 16;
    final static String FILE_PATH = "history.txt";
    final static String[] COLOR_CHOOSER = {"RED", "ORANGE", "YELLOW", "GREEN", "CYAN", "BLUE" , "MAGENTA", "PINK",
            "WHITE", "LIGHT GRAY", "DARK GRAY", "BLACK"};
    final static String CHAT_FONT_NAME = "Arial";
    final static String[] FONT_NAMES = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

}