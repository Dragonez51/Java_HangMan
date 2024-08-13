import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.awt.Color;

public class Ludzik implements ActionListener {

    final JFrame mainFrame = new JFrame();
    JPanel mainPanel = new JPanel();
    JLabel pickCategory = new JLabel();
    JButton generate = new JButton();

    String chosenCategory;

    Ludzik() {
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        File categoriesPath = new File(System.getProperty("user.dir")+"/Categories");
        String[] categories = categoriesPath.list();
        String[] words = new String[]{};

        mainPanel.setBackground(new Color(100,100,100));

//          MainFrame Part 1:
        mainFrame.setTitle("Lobby");
        mainFrame.add(mainPanel);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//          Main Text
        pickCategory.setText("Wybierz kategorię:");
        pickCategory.setFont(new Font("Verdana", Font.BOLD, 30));
        pickCategory.setHorizontalAlignment(SwingConstants.CENTER);
        pickCategory.setAlignmentX(Component.CENTER_ALIGNMENT);
        pickCategory.setBorder(new EmptyBorder(40, 0, 0, 0));
        mainPanel.add(pickCategory);
//        Spacing between main text and list
        mainPanel.add(Box.createRigidArea(new Dimension(100, 50)));
//          List Part1
        JComboBox comboBox = new JComboBox(categories);
        comboBox.setFont(new Font("Arial", Font.BOLD, 26));
        comboBox.setSelectedIndex(0);
        chosenCategory = String.valueOf(comboBox.getSelectedItem());
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chosenCategory = comboBox.getSelectedItem().toString();
            }
        });
        ((JLabel) comboBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(comboBox);
//        Spacing between list and button
        mainPanel.add(Box.createRigidArea(new Dimension(100, 50)));
//        Button to generate word
        generate.setText("Losuj");
        generate.setFont(new Font("Arial", Font.BOLD, 40));
        generate.setPreferredSize(new Dimension(250, 100));
        generate.setMaximumSize(generate.getPreferredSize());
        generate.setMinimumSize(generate.getPreferredSize());
        generate.setAlignmentX(Component.CENTER_ALIGNMENT);
//        ActionListener for button
        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int wordsNumber = 0;
                try {
                    File category =  new File(System.getProperty("user.dir")+"/Categories/"+chosenCategory);
                    Scanner reader = new Scanner(category);
                    while (reader.hasNextLine()) {
                        String word = reader.nextLine();
                        wordsNumber++;
                    }
                    String[] words = new String[wordsNumber];
                    int i=0;
                    Scanner reader2 = new Scanner(category);
                    while(reader2.hasNextLine()){
                        String word = reader2.nextLine();
                        words[i] = word;
                        i++;
                    }

                    LudzikGra ludzik = new LudzikGra(words[(int)(Math.random()*wordsNumber)]);

                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        mainPanel.add(generate);
//        MainFrame Part 2
        mainFrame.pack();
        mainFrame.setSize(new Dimension(400, 460));
//        ComboBox Part 2
        comboBox.setPreferredSize(new Dimension(300, 100));
        comboBox.setMaximumSize(comboBox.getPreferredSize());
        comboBox.setMinimumSize(comboBox.getPreferredSize());
    }
    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
