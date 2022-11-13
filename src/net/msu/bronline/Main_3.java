package net.msu.bronline;

import net.msu.bronline.funcs.StartMenu;
import net.msu.bronline.network.NetworkDevices;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Main_3 {
    static boolean skip = true;

    public static void main(String[] args) {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(Main.class.getClassLoader().getResource("imgs/Kanit-Light.ttf").getPath())));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(Main.class.getClassLoader().getResource("imgs/Kanit-Bold.ttf").getPath())));
        } catch (IOException|FontFormatException e) {
            //Handle exception
        }
        NetworkDevices.findNetworkInterface();

        JTextField user = new JTextField();
        user.setFont(new Font("Kanit Light", Font.PLAIN, 16));
        user.setText("Default_Player 3");

        try{
            new StartMenu(user.getText(), true, skip);
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
                if(user.getText().equals("Default_Player 3")){
                    user.setText("");
                }
            }
        });

        st.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.setVisible(false);
                try{
                    new StartMenu(user.getText(), true, skip);
                }catch (IOException ex){
                    System.out.println("File Missing?");
                }
            }
        });
    }
}
