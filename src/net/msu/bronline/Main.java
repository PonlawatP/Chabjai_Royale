package net.msu.bronline;

import net.msu.bronline.funcs.StartMenu;
import net.msu.bronline.funcs.Utils;
import net.msu.bronline.network.NetworkDevices;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Main {
    static String sn = "";
    static boolean skip = false;
    public static void main(String[] args) {
        boolean intf = false;
        for (String str : args){
            String[] s = str.split("=");
            if(s[0].equalsIgnoreCase("score_win")){
                int i = Integer.parseInt(s[1]);
                Utils.score_win=i;
            } else if(s[0].equalsIgnoreCase("allowed_start")){
                int i = Integer.parseInt(s[1]);
                Utils.allowed_start=i;
            } else if(s[0].equalsIgnoreCase("port")){
                int i = Integer.parseInt(s[1]);
                NetworkDevices.port=i;
            } else if(s[0].equalsIgnoreCase("mc_port")){
                int i = Integer.parseInt(s[1]);
                NetworkDevices.MULTICAST_PORT=i;
            } else if(s[0].equalsIgnoreCase("mc_ip")){
                NetworkDevices.MULTICAST_IP=s[1];
            } else if(s[0].equalsIgnoreCase("username")){
                sn=s[1];
            } else if(str.equalsIgnoreCase("skip_intro")){
                skip=true;
            } else if(str.equalsIgnoreCase("inlist")){
                NetworkDevices.findNetworkInterface();
                System.out.println("- - - - Interface List - - - -");
                for(String a : NetworkDevices.interfaceList.keySet()) {
                    System.out.println(" - " + a + " : " + NetworkDevices.interfaceList.get(a));
                }
                System.out.println("\nSelect your right interface name then use this Argument to custom interface \n\"interf=<interface_name>\"");
                return;
            } else if(s[0].equalsIgnoreCase("interf")){
                System.out.println("Selecting custom Interface: " + s[1]);
                NetworkDevices.MULTICAST_INTERFACE = s[1];
                NetworkDevices.findNetworkInterface();

                if(NetworkDevices.interfaceList.get(s[1]) != null){
                    intf = true;
                    System.out.println("selected interface: " + s[1] + " : " + NetworkDevices.interfaceList.get(s[1]));
                } else {
                    System.out.println("Error: Invalid Interface");
                    System.out.println("To check network interface please use Argument \n\"inlist\"");
                    return;
                }
            }
        }

        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Main.class.getClassLoader().getResourceAsStream("imgs/Kanit-Light.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Main.class.getClassLoader().getResourceAsStream("imgs/Kanit-Bold.ttf")));
        } catch (IOException|FontFormatException e) {
            //Handle exception
        }
        if(!intf) NetworkDevices.findNetworkInterface();

        if(sn.length() > 0){
            try{
                new StartMenu(sn, true, skip);
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
                    new StartMenu(user.getText(), true, skip);
                }catch (IOException ex){
                    System.out.println("File Missing?");
                }
            }
        });
    }
}