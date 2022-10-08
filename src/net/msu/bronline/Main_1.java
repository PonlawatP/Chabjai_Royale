package net.msu.bronline;

import net.msu.bronline.funcs.StartMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class Main_1 {

    public static void main(String[] args) {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(Main_1.class.getClassLoader().getResource("imgs/Kanit-Light.ttf").getPath())));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(Main_1.class.getClassLoader().getResource("imgs/Kanit-Bold.ttf").getPath())));
        } catch (IOException|FontFormatException e) {
            //Handle exception
        }

        JTextField user = new JTextField();
        user.setFont(new Font("Kanit Light", Font.PLAIN, 16));
        user.setText("Default_Player");

        try{
            new StartMenu(user.getText(), true);
            return;
        }catch (IOException ex){
            System.out.println("File Missing?");
        }

        JFrame f = new JFrame("Insert Username");
        f.setLayout(new BorderLayout());
        f.setSize(300, 100);
        JButton st = new JButton("Start Game");
        st.setFont(new Font("Kanit Light", Font.PLAIN, 14));
        f.add(user,BorderLayout.CENTER);
        f.add(st,BorderLayout.SOUTH);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.setVisible(true);

        user.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(user.getText().equals("Default_Player")){
                    user.setText("");
                }
            }
        });

        st.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.setVisible(false);
                try{
                    new StartMenu(user.getText(), true);
                }catch (IOException ex){
                    System.out.println("File Missing?");
                }
            }
        });
    }
}