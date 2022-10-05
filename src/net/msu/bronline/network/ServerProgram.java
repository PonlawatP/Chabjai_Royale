package net.msu.bronline.network;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerProgram {
    private ServerSocket sev;
    public ServerProgram(ServerSocket sev) {
        this.sev = sev;
    }

    public void startSev() {
        try {
            while (!sev.isClosed()){
                Socket soc = sev.accept();
                System.out.println("Client has connected!");
                ClientHandler clientHandler = new ClientHandler(soc);
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

    public static void main(String[] args) throws IOException {
        ServerSocket sev;
        sev = new ServerSocket(50394);
        ServerProgram server = new ServerProgram(sev);
        server.startSev();
    }
}