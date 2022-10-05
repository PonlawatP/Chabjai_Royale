package net.msu.bronline.network;
import net.msu.bronline.comps.Scene;
import net.msu.bronline.guis.Game;

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

    public void setScene(Scene scene) {
        this.scene = scene;
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

//    public static void main(String[] args) throws IOException {
//        ServerSocket sev;
//        sev = new ServerSocket(50394);
//        ServerProgram server = new ServerProgram(sev);
//        server.startSev();
//    }

    @Override
    public void run() {
        ServerSocket sev;
        try {
            sev = new ServerSocket(50394);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ServerProgram server = new ServerProgram(sev);
        server.setScene(scene);
        server.startSev();
    }
}