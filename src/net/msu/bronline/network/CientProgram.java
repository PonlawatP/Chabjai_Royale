package net.msu.bronline.network;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CientProgram {
    private Socket soc;
    private BufferedReader bufReader;
    private BufferedWriter bufWriter;
    private String Username;

    public CientProgram(Socket soc,String Username ) {
        try {
            this.soc = soc;
            this.bufWriter = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
            this.bufReader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            this.Username = Username;
        } catch (Exception e) {
            closeEverything(soc, bufReader,bufWriter);
        }
    }
    public void sendMess() {
        try {
            bufWriter.write(Username);
            bufWriter.newLine();
            bufWriter.flush();

            Scanner sc =new Scanner(System.in);
            while (soc.isConnected()) {
                String mess = sc.nextLine();
                bufWriter.write(Username +":"+mess);
                bufWriter.newLine();
                bufWriter.flush();
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

    public static void main(String[] args) throws  IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your name : ");
        String user = sc.nextLine();
        Socket soc = new Socket("localhost",50394);
        CientProgram client = new CientProgram(soc, user);
        client.ForMessage();
        client.sendMess();
    }
}
