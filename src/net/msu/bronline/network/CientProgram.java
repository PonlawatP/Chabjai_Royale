package net.msu.bronline.network;
import net.msu.bronline.guis.Game;
import net.msu.bronline.guis.Present;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CientProgram extends Thread implements Runnable{
    private Socket soc;

    DataInputStream dis;
    DataOutputStream dos;

    Present ps;
    Game game;
    private String Username;

    Thread thrd_write;
    Thread thrd_read;
    Cli_write cw;
    public CientProgram(String Username, Present ps, Game game) {
        try {
            this.Username = Username;
            this.ps = ps;
            this.game = game;

            soc = new Socket("localhost",50394);

            dis = new DataInputStream(soc.getInputStream());
            dos = new DataOutputStream(soc.getOutputStream());

            cw = new Cli_write(dos, this);
            thrd_write = new Thread(cw);
            thrd_write.start();
            thrd_read = new Thread(new Cli_read(dis, this, cw));
            thrd_read.start();
        } catch (Exception e) {
            game.setGame_status(0);
            ps.setGame_status(2);
            closeEverything();
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return Username;
    }

    public void closeEverything() {
        try {
            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }
            if (soc != null) {
                soc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    boolean quit = false;
    @Override
    public void run() {
    }

}
