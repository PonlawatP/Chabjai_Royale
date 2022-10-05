package net.msu.bronline.network;
import  java.io.BufferedReader;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandler = new ArrayList<>();
    private Socket soc;
    private BufferedReader bufReader;
    private BufferedWriter bufWriter;
    private String clientUser;

    public ClientHandler(Socket soc) {
        try {
            this.soc = soc;
            this.bufWriter = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
            this.bufReader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            this.clientUser = bufReader.readLine();
            clientHandler.add(this);
            broadcastMessage("SERVER : "+clientUser+" enter the chat");
        } catch (Exception e) {
            closeEverything(soc, bufReader,bufWriter);
        }
    }

    @Override
    public void run() {
        String mess;
        while (soc.isConnected()) {
            try {
                mess =  bufReader.readLine();
                broadcastMessage(mess);
            } catch (Exception e) {
                closeEverything(soc, bufReader,bufWriter);
                break;
            }

        }

    }

    public void broadcastMessage(String mess) {
        for (ClientHandler clientHandler : clientHandler) {
            try {
                if (!clientHandler.clientUser.equals(clientUser)) {
                    clientHandler.bufWriter.write(mess);
                    clientHandler.bufWriter.newLine();
                    clientHandler.bufWriter.flush();
                }
            } catch (Exception e) {
                closeEverything(soc, bufReader,bufWriter);
            }
        }

    }


    private void closeEverything(Socket soc2, BufferedReader bufReader2, BufferedWriter bufWriter2) {
        removeClientHandler();
        try {
            if (bufReader2 != null) {
                bufReader2.close();
            }
            if (bufWriter2 != null) {
                bufWriter2.close();
            }
            if (soc2 != null) {
                soc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void removeClientHandler() {
        clientHandler.remove(this);
        broadcastMessage("SERVER : "+ clientUser + "Left!");

    }

}


