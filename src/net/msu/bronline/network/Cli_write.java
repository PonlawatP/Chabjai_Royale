package net.msu.bronline.network;

import net.msu.bronline.funcs.Utils;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.EmptyStackException;
import java.util.Stack;

import static net.msu.bronline.guis.Game.getGame;

public class Cli_write extends Thread implements Runnable{
    DataOutputStream dos;
    CientProgram cp;
    Stack<String> st = new Stack<>();
    Socket soc;

    public Cli_write(DataOutputStream dos, CientProgram cp, Socket soc){
        this.dos = dos;
        this.cp = cp;
        this.soc = soc;
        sendMessage(cp.getUsername()+":join:"+getGame().getPlayerOwn().getCharacterID());
    }

    public void sendMessage(String mess) {
        try {
            if (!st.empty())
                st.add(0, st.pop()+"::ln::"+mess);
            else
                st.add(0, mess);
        } catch (EmptyStackException ee){
            st.add(0, mess);
        }
    }

    public void sendMessageToServer() {
        if (st.empty()) return;

        while (!st.empty()) {
            try {
                String mess = st.pop();
                for (String Mdata : mess.split("::ln::")) {
                    String[] data = Mdata.split(":");
                    if (!data[1].equalsIgnoreCase("player") && Utils.dev) System.out.println("<- " + Mdata);
                }

                dos.writeUTF(mess);
                dos.flush();
            } catch (EOFException | EmptyStackException ex) {
//                ex.printStackTrace();
            } catch (SocketException es) {
//                es.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public void run() {
        while (!cp.quit && !soc.isClosed()){
            try {
                if(getGame().getGame_status() == 2 || getGame().getGame_status() == 3)
                    if(getGame().getPlayerOwn().isMove()) sendMessage(cp.getUsername() + ":" + getGame().getPlayerOwn().getPacket());

                sendMessageToServer();
                sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
