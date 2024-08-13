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

public class LudzikGra{

    JFrame gameMainFrame = new JFrame();
    JPanel gameMainPanel = new JPanel();
    JLabel guessedWord = new JLabel();
    JPanel wordPanel = new JPanel();

    int failCount=0;
    boolean win;
    boolean lose;

    LudzikGra(String haslo) throws IOException {
//        gameMainPanel
        gameMainPanel.setLayout(new BoxLayout(gameMainPanel, BoxLayout.PAGE_AXIS));
        wordPanel.setLayout(new BoxLayout(wordPanel, BoxLayout.PAGE_AXIS));
//        gameMainFrame Part 1
        gameMainFrame.setTitle("Ludzik");
        gameMainFrame.add(gameMainPanel);
        gameMainFrame.setResizable(false);
        gameMainFrame.setVisible(true);
        gameMainPanel.setBackground(new Color(100,100,100));
//        guessedWord
        guessedWord.setText(haslo);
        guessedWord.setVisible(false);
        gameMainPanel.add(guessedWord);
//        Image
        BufferedImage ludzik = ImageIO.read(new File(System.getProperty("user.dir")+"/Image/Ludzik("+0+").png"));
        JLabel picLabel = new JLabel(new ImageIcon(ludzik));
        picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        picLabel.setBorder(new EmptyBorder(30,0,0,0));
        gameMainPanel.add(picLabel);
//        Hidden word
        String substr = guessedWord.getText();
        String[] wordLetters = new String[substr.length()];
        for(int i=0; i<substr.length();i++){
            wordLetters[i] = substr.substring(i, i + 1);
        }
        String[] guess = new String[wordLetters.length];
        for(int i=0; i<wordLetters.length; i++) {
            if (" ".equals(wordLetters[i]) ) {
                guess[i] = " ";
            } else {
                guess[i] = "-";
            }
        }
        JLabel letter = new JLabel();
        letter.setFont(new Font("Arial", Font.BOLD, 60));
        letter.setAlignmentX(Component.CENTER_ALIGNMENT);
        LudzikGra.updateGuessedWord(letter, guess, failCount, haslo, win);
        wordPanel.setBorder(new EmptyBorder(40,0,0,0));
        wordPanel.setPreferredSize(new Dimension(1000,150));
        wordPanel.setMaximumSize(wordPanel.getPreferredSize());
        wordPanel.setBackground(new Color(100,100,100));
        wordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        wordPanel.add(letter);
        gameMainPanel.add(wordPanel);
//        Panels for buttons
        //Main buttonPanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(100,100,100));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        //Top buttonPanel
        JPanel buttonTopPanel = new JPanel();
        buttonTopPanel.setBackground(new Color(100,100,100));
        //Mid buttonPanel
        JPanel buttonMidPanel = new JPanel();
        buttonMidPanel.setBackground(new Color(100,100,100));
        //Bottom buttonPanel
        JPanel buttonBottomPanel = new JPanel();
        buttonBottomPanel.setBackground(new Color(100,100,100));
//        Buttons
        char[] keyboard = {'Q','W','E','R','T','Y','U','I','O','P','A','S','D','F','G','H','J','K','L','Z','X','C','V','B','N','M'};
        for(int i=0; i<keyboard.length; i++){
            JButton key = new JButton();
            key.setText(String.valueOf(keyboard[i]));
            key.setPreferredSize(new Dimension(80,80));
            key.setMaximumSize(key.getPreferredSize());
//            CHECK FOR LETTERS IN WORD
            key.addActionListener(new ActionListener() {
                boolean hit;
                public void actionPerformed(ActionEvent e){
                    for(int i=0; i<wordLetters.length; i++){
                        if(key.getText().equals(wordLetters[i])){
                            guess[i] = key.getText();
                            key.setEnabled(false);
                            key.setBackground(Color.GREEN);
                            hit = true;
                            continue;
                        }else{
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
                        updateImage(ludzik, picLabel, failCount);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    for(int i=0; i<guess.length; i++){
                        if("-".equals(guess[i])){
                            win = false;
                            break;
                        }else{
                            win = true;
                        }
                    }

                    if(win){
                        gameOver(buttonTopPanel, buttonMidPanel, buttonBottomPanel);
                    }else if(lose){
                        gameOver(buttonTopPanel, buttonMidPanel, buttonBottomPanel);
                    }

                    updateGuessedWord(letter, guess, failCount, haslo, win);
                }
            });
            if(i<10){buttonTopPanel.add(key);}
            else if(i>9 && i<19){buttonMidPanel.add(key);}
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
}