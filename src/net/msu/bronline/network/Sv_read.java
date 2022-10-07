package net.msu.bronline.network;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class Sv_read extends Thread implements Runnable{
    DataInputStream dis;
    ClientHandler ch;
    Sv_write cw;
    public Sv_read(DataInputStream dis, ClientHandler ch, Sv_write cw){
        this.dis = dis;
        this.ch = ch;
        this.cw = cw;
    }

    @Override
    public void run() {
        while (!ch.quit){
            try {
                for(String mess : dis.readUTF().split("::ln::")) {
                    String[] data = mess.split(":");
                    if (data[1].equalsIgnoreCase("player")) {
                        ch.getPlayer().updateFromPacket(data);
                    }
                }
                sleep(2);
            } catch (EOFException | SocketException ex){
//                ex.printStackTrace();
                ch.closeEverything();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
