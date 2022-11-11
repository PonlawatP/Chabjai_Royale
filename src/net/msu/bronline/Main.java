package net.msu.bronline;

import net.msu.bronline.funcs.StartMenu;
import net.msu.bronline.funcs.Utils;
import net.msu.bronline.network.NetworkDevices;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class Main {
    static String sn = "";
    public static void main(String[] args) {
        for (String s : args){

            if(s.equalsIgnoreCase("score_win")){
                int i = Integer.parseInt(s.split("=")[1]);
                Utils.score_win=i;
            } else if(s.equalsIgnoreCase("allowed_start")){
                int i = Integer.parseInt(s.split("=")[1]);
                Utils.allowed_start=i;
            } else if(s.equalsIgnoreCase("port")){
                int i = Integer.parseInt(s.split("=")[1]);
                NetworkDevices.port=i;
            } else if(s.equalsIgnoreCase("mc_port")){
                int i = Integer.parseInt(s.split("=")[1]);
                NetworkDevices.MULTICAST_PORT=i;
            } else if(s.equalsIgnoreCase("mc_ip")){
                NetworkDevices.MULTICAST_IP=s.split("=")[1];
            } else if(s.equalsIgnoreCase("username")){
                sn=s.split("=")[1];
            }
        }

        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Main.class.getClassLoader().getResourceAsStream("imgs/Kanit-Light.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Main.class.getClassLoader().getResourceAsStream("imgs/Kanit-Bold.ttf")));
        } catch (IOException|FontFormatException e) {
            //Handle exception
        }

        if(sn.length() > 0){
            try{
                new StartMenu(sn, true);
            }catch (IOException ex){
                System.out.println("File Missing?");
            }
            return;
        }

        JTextField user = new JTextField();
        user.setFont(new Font("Kanit Light", Font.PLAIN, 16));
        user.setText("Default_Player");

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