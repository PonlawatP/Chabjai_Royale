package net.msu.bronline.network;
import net.msu.bronline.comps.Scene;
import net.msu.bronline.guis.Game;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerProgram implements Runnable{
    private ServerSocket sev;
    public ServerProgram(ServerSocket sev) {
        this.sev = sev;
    }
    Scene scene;
    Game game;
    public ServerProgram(Scene scene, Game game) {
        this.scene = scene;
        this.game = game;
    }
    public void startSev() {
        System.out.println("Server starting...");
        try {
            while (!sev.isClosed()){
                Socket soc = sev.accept();
                System.out.println("Client has connected!");

                ClientHandler clientHandler = new ClientHandler(soc, scene, game);
                Thread t = new Thread(clientHandler);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Server stop...");
    }

    public void close() {
        try {
            if (sev != null) {
                sev.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            sev = new ServerSocket(50394);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        startSev();
    }
}