package net.msu.bronline.network;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NetworkDevices {
    private static int port = 50394;

    static HashMap<String, String[]>  fipList = new HashMap<>();
    static HashMap<String, String[]> tflp = new HashMap<>();
    public static HashMap<String, String[]> getHostsIP(){
        return fipList;
    }
    public static String[] getHostDetails(int id){
        return fipList.get(fipList.keySet().toArray()[id]);
    }
    public static String getHostIP(int id){
        return (String) fipList.keySet().toArray()[id];
    }

    public static HashMap<String, String[]>  getNetworkDevices(){
//        System.out.println("------------------------");
//        System.out.println("Lookup-ip process");
//        fipList.clear();
        try {
            Process process = Runtime.getRuntime().exec("arp -a");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String ip;
            List<String> ipList = new ArrayList<>(); // List<> Array to hold dynamic IP Addresses
            boolean isint = false;
            while ((ip = reader.readLine()) != null) {
                ip = ip.trim();     // Trim the data
                if (!ip.equals("")) {
                    if (!ip.equals("")) {
                        // Remove all the unwanted spaces between data provided by
                        // the ARP Table when it is generated.
                        while (ip.contains("  ")) { ip = ip.trim().replace("  ", " "); }
                        // Split each data line into a String Array for processing
                        String[] dataArray = ip.split(" ");
                        // For console output display only...
                        if (dataArray[0].toLowerCase().startsWith("interface:")) {
//                            System.out.println("Locating Devices Connected To: " + dataArray[1]);
                            if(!isint){
                                isint = true;
                                ipList.add(dataArray[1]);
                            }
                        }
                        // If the data line contains the word "dynamic"
                        // then add the IP address on that line to the
                        // List<> Array...
                        if (dataArray[2].equalsIgnoreCase("dynamic")) {
                            ipList.add(dataArray[0]);
                            // For console output display only...
//                            System.out.println("Device Located On IP: " + dataArray[0]);
                        }
                    }
                }
            }
            // Close the Reader
            reader.close();
//            System.out.println("------------------------");

            // try to connect to the device....
            // You'll need to play with this.
            tflp = new HashMap<>();
            for (int i = 0; i < ipList.size(); i++) {
                Socket socket = new Socket();
                try {
                    ip = ipList.get(i);
                    socket.connect(new InetSocketAddress(ip, port), 500);
                } catch (ConnectException | SocketTimeoutException ex) {
//                    System.out.println("\tSOCKET ERROR - " + ex.getMessage());
                    continue;
                }

                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF("ping");
                dos.flush();

                DataInputStream dis = new DataInputStream(socket.getInputStream());
                String tip[] = {ip};
                new Thread(new Runnable() {
                    int r = 0;
                    @Override
                    public void run() {
                        while (!socket.isClosed()) {
                            try {
                                if (dis.available() != 0) {
                                    String s = dis.readUTF();
                                    tflp.put(tip[0], s.split(":"));
//                                    System.out.println(s);

                                    socket.close();
                                    break;
                                }
                                Thread.sleep(100);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }).start();

                System.out.println("Found socket for: " + ip);
            }
        } catch (IOException | InterruptedException e) {
//            System.out.println("\nPROCESS/READER ERROR - " + e.getMessage());
        }
//        System.out.println("Ended lookup-ip process");
//        System.out.println("------------------------");
        return fipList;
    }

    public static void updateHost(){
        fipList = tflp;
    }
}