import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LudzikGra implements KeyListener{

    JFrame gameMainFrame = new JFrame();
    JPanel gameMainPanel = new JPanel();
    JPanel wordPanel = new JPanel();

    BufferedImage ludzikIcon;
    JLabel picLabel;
    String kategoria;
    String[] wordLetters;
    String[] guess;
    JLabel letter;

    JPanel buttonPanel;
    JPanel buttonTopPanel;
    JPanel buttonMidPanel;
    JPanel buttonBottomPanel;

    boolean hit;
    int failCount=0;
    boolean win;
    boolean lose;

    String hasloGlob;

    LudzikGra(String haslo) throws IOException {
        hasloGlob = haslo;

        gameMainFrame.addKeyListener(this);

//        gameMainPanel
        gameMainPanel.setLayout(new BoxLayout(gameMainPanel, BoxLayout.PAGE_AXIS));
        wordPanel.setLayout(new BoxLayout(wordPanel, BoxLayout.PAGE_AXIS));
//        gameMainFrame Part 1
        kategoria = Ludzik.getCategory();
        gameMainFrame.setTitle(kategoria.substring(0,(kategoria.length()-4)));
        gameMainFrame.add(gameMainPanel);
        gameMainFrame.setResizable(true);
        gameMainFrame.setVisible(true);
        gameMainPanel.setBackground(new Color(100,100,100));
//        Image
        ludzikIcon = ImageIO.read(new File(System.getProperty("user.dir")+"/Image/Ludzik("+0+").png"));
        picLabel = new JLabel(new ImageIcon(ludzikIcon));
        picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        picLabel.setBorder(new EmptyBorder(30,0,0,0));
        gameMainPanel.add(picLabel);
//        Hidden word
        wordLetters = new String[hasloGlob.length()];
        for(int i=0; i<hasloGlob.length();i++){
            String temp = hasloGlob.substring(i, i + 1);
            wordLetters[i] = temp;
        }
        guess = new String[wordLetters.length];
        for(int i=0; i<wordLetters.length; i++) {
            if (" ".equals(wordLetters[i]) ) {
                guess[i] = " ";
            } else {
                guess[i] = "-";
            }
        }
        letter = new JLabel();
        letter.setFont(new Font("Arial", Font.BOLD, 60));
        letter.setAlignmentX(Component.CENTER_ALIGNMENT);
        LudzikGra.updateGuessedWord(letter, guess, failCount, hasloGlob, win);
        wordPanel.setBorder(new EmptyBorder(40,0,0,0));
        wordPanel.setPreferredSize(new Dimension(1000,150));
        wordPanel.setMaximumSize(wordPanel.getPreferredSize());
        wordPanel.setBackground(new Color(100,100,100));
        wordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        wordPanel.add(letter);
        gameMainPanel.add(wordPanel);
//        Panels for buttons
        //Main buttonPanel
        buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(100,100,100));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        //Top buttonPanel
        buttonTopPanel = new JPanel();
        buttonTopPanel.setBackground(new Color(100,100,100));
        //Mid buttonPanel
        buttonMidPanel = new JPanel();
        buttonMidPanel.setBackground(new Color(100,100,100));
        //Bottom buttonPanel
        buttonBottomPanel = new JPanel();
        buttonBottomPanel.setBackground(new Color(100,100,100));
//        Buttons
        char[] keyboard = {'Q','W','E','R','T','Y','U','I','O','P','A','S','D','F','G','H','J','K','L','Z','X','C','V','B','N','M'};
        for(int i=0; i<keyboard.length; i++){
            JButton key = new JButton();
            key.setText(String.valueOf(keyboard[i]));
            key.setPreferredSize(new Dimension(80,80));
            key.setMaximumSize(key.getPreferredSize());
            key.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    checkForHit(key, hasloGlob);
                    gameMainFrame.requestFocus();
                }
            });
            if(i<10){buttonTopPanel.add(key);}
            else if(i<19){buttonMidPanel.add(key);}
            else{buttonBottomPanel.add(key);}

        }

        buttonPanel.add(buttonTopPanel);
        buttonPanel.add(buttonMidPanel);
        buttonPanel.add(buttonBottomPanel);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameMainPanel.add(buttonPanel);
//          gameMainFrame Part 2
        gameMainFrame.pack();
        gameMainFrame.setSize(new Dimension(1000, 1000));

        gameMainFrame.requestFocus();
    }

    public void checkForHit(JButton key, String haslo){
        for(int i=0; i<wordLetters.length; i++) {
            if (key.getText().equals(wordLetters[i])) {
                guess[i] = key.getText();
                key.setEnabled(false);
                key.setBackground(Color.GREEN);
                hit = true;
            } else {
                key.setEnabled(false);
            }
        }
        if(!hit){
            failCount++;
            key.setBackground(Color.DARK_GRAY);
            if(failCount == 10){
                lose = true;
            }
        }
        try {
            updateImage(ludzikIcon, picLabel, failCount);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        for (String s : guess) {
            if ("-".equals(s)) {
                win = false;
                break;
            } else {
                win = true;
            }
        }

        if(win){
            gameOver(buttonTopPanel, buttonMidPanel, buttonBottomPanel);
            gameMainFrame.removeKeyListener(this);
        }else if(lose){
            gameOver(buttonTopPanel, buttonMidPanel, buttonBottomPanel);
            gameMainFrame.removeKeyListener(this);
        }

        updateGuessedWord(letter, guess, failCount, haslo, win);

        hit=false;
    }

    public static void updateGuessedWord(JLabel letter, String[] guess, int failCount, String haslo, boolean win){
        if(failCount!=10) {
            letter.setText("");
            int i = 0;
            for (String temp : guess) {
                letter.setText(letter.getText() + guess[i]);
                i++;
            }

            if(win){
                letter.setText("WYGRANA, HASŁO TO: "+haslo);
                letter.setFont(new Font("Arial", Font.BOLD, 30));
                letter.setForeground(Color.GREEN);

            }

        }else{
            letter.setText("PRZEGRANA, HASŁO TO: "+haslo);
            letter.setFont(new Font("Arial", Font.BOLD, 30));
            letter.setForeground(Color.RED);
        }

    }
    public static void updateImage(BufferedImage ludzik, JLabel picLabel, int  failCounter) throws IOException {
        ludzik = ImageIO.read(new File(System.getProperty("user.dir")+"/Image/Ludzik("+failCounter+").png"));
        picLabel.setIcon(new ImageIcon(ludzik));
    }
    public static void gameOver(JPanel buttonTopPanel, JPanel buttonMidPanel, JPanel buttonBottomPanel){
        for(int i=0; i<buttonTopPanel.getComponentCount(); i++){
            Component c = buttonTopPanel.getComponent(i);
            c.setEnabled(false);
        }
        for(int i=0; i<buttonMidPanel.getComponentCount(); i++){
            Component c = buttonMidPanel.getComponent(i);
            c.setEnabled(false);
        }
        for(int i=0; i<buttonBottomPanel.getComponentCount(); i++){
            Component c = buttonBottomPanel.getComponent(i);
            c.setEnabled(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        for(int i=0; i<buttonTopPanel.getComponentCount();i++){
            JButton c = (JButton) buttonTopPanel.getComponent(i);
            String temp = String.valueOf(e.getKeyChar());
            if(temp.toUpperCase().equals(c.getText())&&c.isEnabled()){
                checkForHit(c, hasloGlob);
            }
        }
        for(int i=0; i<buttonMidPanel.getComponentCount();i++){
            JButton c = (JButton) buttonMidPanel.getComponent(i);
            String temp = String.valueOf(e.getKeyChar());
            if(temp.toUpperCase().equals(c.getText())&&c.isEnabled()){
                checkForHit(c, hasloGlob);
            }
        }
        for(int i=0; i<buttonBottomPanel.getComponentCount();i++){
            JButton c = (JButton) buttonBottomPanel.getComponent(i);
            String temp = String.valueOf(e.getKeyChar());
            if(temp.toUpperCase().equals(c.getText())&&c.isEnabled()){
                checkForHit(c, hasloGlob);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}