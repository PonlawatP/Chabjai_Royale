package net.msu.bronline.network;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.Stack;

public class Cli_write extends Thread implements Runnable{
    DataOutputStream dos;
    CientProgram cp;
    Stack<String> st = new Stack<>();

    public Cli_write(DataOutputStream dos, CientProgram cp){
        this.dos = dos;
        this.cp = cp;
//        System.out.println(cp.getUsername()+" read thread run!");
    }

    public void sendMessage(String mess) {
        if (st.empty()){
            st.add(0, mess);
        } else {
            st.add(0, st.pop()+"::ln::"+mess);
        }
    }

    public void sendMessageToServer(){
        if (st.empty()) return;

        while (!st.empty()){
            String mess = st.pop();

            for (String Mdata : mess.split("::ln::")){
                String[] data = Mdata.split(":");
                if (!data[1].equalsIgnoreCase("player")) System.out.println(Mdata);
            }

            try {
                dos.writeUTF(mess);
                dos.flush();
            } catch (EOFException ex){
                ex.printStackTrace();
            }
            catch (SocketException es){
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public void run() {
        while (!cp.quit){
            try {
                sendMessage(cp.getUsername() + ":" + cp.game.getPlayerOwn().getPacket());

                sendMessageToServer();
                sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
