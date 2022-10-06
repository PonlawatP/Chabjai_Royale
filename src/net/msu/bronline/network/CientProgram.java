package net.msu.bronline.network;
import net.msu.bronline.guis.Game;
import net.msu.bronline.guis.Present;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CientProgram implements Runnable{
    private Socket soc;
    private BufferedReader bufReader;
    private BufferedWriter bufWriter;


    private Socket socket            = null;
    private DataInputStream input   = null;
    private DataOutputStream out     = null;
    private String Username;

    Present ps;
    Game game;
    boolean read_send;

    public CientProgram(Socket soc,String Username, Present ps, Game game, boolean read_send) {
        try {
            this.soc = soc;
            this.bufWriter = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
            this.bufReader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            this.Username = Username;
            this.ps = ps;
            this.game = game;
            this.read_send = read_send;
        } catch (Exception e) {
            closeEverything(soc, bufReader,bufWriter);
        }
    }
    public CientProgram(String Username, Present ps, Game game, boolean read_send) {
            this.Username = Username;
            this.ps = ps;
            this.game = game;
            this.read_send = read_send;
    }
    public void sendMess() {
        try {
            bufWriter.write(Username +":"+game.getPlayerOwn().getPacket());
            bufWriter.newLine();
            bufWriter.flush();

            while (soc.isConnected()) {
                if(game.getGame_status() == 1){
//                    System.out.println(game.getGame_status());
                    bufWriter.write(Username +":"+game.getPlayerOwn().getPacket());
                    bufWriter.newLine();
                    bufWriter.flush();

//                    String msgGroup;
//                    msgGroup =  bufReader.readLine();
//                    System.out.println(msgGroup);
                }
            }
        } catch (Exception e) {
            closeEverything(soc, bufReader,bufWriter);
        }
    }

    private void closeEverything(Socket soc2, BufferedReader bufReader2, BufferedWriter bufWriter2) {
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
    public void ForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgGroup;
                while (soc.isConnected()) {
                    try {
                        msgGroup =  bufReader.readLine();
                        System.out.println(msgGroup);
                    } catch (Exception e) {
                        closeEverything(soc, bufReader,bufWriter);
                    }
                }

            }
        }).start();
    }

    @Override
    public void run() {
//        Socket soc = null;
//        try {
//            soc = new Socket("localhost",50394);
//        } catch (IOException e) {
//            game.setGame_status(0);
//            ps.setGame_status(2);
//            throw new RuntimeException(e);
//        }
//        CientProgram client = new CientProgram(soc, Username, ps, game, read_send);
////        client.ForMessage();
//        client.sendMess();

        // establish a connection
        try
        {
            socket = new Socket("localhost",50394);
            System.out.println("Connected");

            // takes input
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            // sends output to the socket
            out    = new DataOutputStream(socket.getOutputStream());
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }

        // string to read message from input
        String line = "";

        // keep reading until "Over" is input
        while (!line.equals("close"))
        {
            try
            {
                out.writeUTF(Username +":"+game.getPlayerOwn().getPacket());
//                line = input.readLine();

//                if(input.read())
//                line = input.readUTF();

//                System.out.println((String)(ois.readObject()));
            }
            catch(IOException i)
            {
                System.out.println(i);
            }
        }

        // close the connection
        try
        {
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

//    public static void main(String[] args) throws  IOException {
//        Scanner sc = new Scanner(System.in);
//        System.out.print("Enter your name : ");
//        String user = sc.nextLine();
//        Socket soc = new Socket("localhost",50394);
//        CientProgram client = new CientProgram(soc, user);
//        client.ForMessage();
//        client.sendMess();
//    }


}
