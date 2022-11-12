package net.msu.bronline.network;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.util.HashMap;
import java.util.Iterator;

public class NetworkDevices {
    public static int port = 50394;
    public static String MULTICAST_INTERFACE = "wlp2s0";
    public static int MULTICAST_PORT = 4321;
    public static String MULTICAST_IP = "230.0.0.0";

    static HashMap<String, String[]>  fipList = new HashMap<>();
    static HashMap<String, String[]> tflp = new HashMap<>();
    static HashMap<String, Integer> counthost = new HashMap<>();
    public static HashMap<String, String[]> getHostsIP(){
        return fipList;
    }
    public static String[] getHostDetails(int id){
        return fipList.get(fipList.keySet().toArray()[id]);
    }
    public static String getHostIP(int id){
        return (fipList.keySet().size() == 0 || fipList.keySet().size() <= id ? "0.0.0.0" : (String) fipList.keySet().toArray()[id]);
    }

    public static void findNetworkInterface() {
        try{
            for(NetworkInterface s : NetworkInterface.networkInterfaces().toList()){
//              System.out.println(s.getName() + " : " + s.supportsMulticast() + " : " + s.isUp() + " : " + s.isLoopback());
                if(s.supportsMulticast() && s.isUp() && !s.isLoopback()){
                    MULTICAST_INTERFACE = s.getName();
                    break;
                }
            }
        } catch (Exception e){
            System.out.println("Finding network interface error.");
            System.out.println("You much be insert your ip address yourself");
        }
    }
    public static String receiveMulticastMessage(String ip, String iface, int port)
            throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open(StandardProtocolFamily.INET); //set protocal for multicast ip
        NetworkInterface networkInterface = NetworkInterface.getByName(iface); //get interface that can be multicasting
        datagramChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        datagramChannel.bind(new InetSocketAddress(port)); //set multicast ip
//        System.out.println("interface: " + networkInterface);
        datagramChannel.setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface);
        InetAddress inetAddress = InetAddress.getByName(ip);
        MembershipKey membershipKey = datagramChannel.join(inetAddress, networkInterface);
//        System.out.println("Waiting for the message...");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        datagramChannel.receive(byteBuffer);
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes, 0, byteBuffer.limit());
        membershipKey.drop();
        return new String(bytes);
    }
    public static void sendMulticastMessage(String ip, String iface, int port,
                                            String message) throws IOException {
        DatagramChannel datagramChannel=DatagramChannel.open();
        datagramChannel.bind(null);
        NetworkInterface networkInterface=NetworkInterface
                .getByName(iface);
        datagramChannel.setOption(StandardSocketOptions
                .IP_MULTICAST_IF,networkInterface);
        ByteBuffer byteBuffer=ByteBuffer.wrap
                (message.getBytes());
        InetSocketAddress inetSocketAddress = new
                InetSocketAddress(ip,port);
        datagramChannel.send(byteBuffer,inetSocketAddress);
    }

    public static HashMap<String, String[]> getHostPinged(){
        try {
            String msg = receiveMulticastMessage(MULTICAST_IP, MULTICAST_INTERFACE, MULTICAST_PORT);
//            System.out.println("Message received : " + msg);
            String[] msgs = msg.split(":");
            fipList.put(msgs[7], msgs);
            tflp.put(msgs[7], msgs);
        } catch (Exception e) {

        }
        return fipList;
    }

    public static void clearPingList(){
        tflp = new HashMap<>();
    }

    public static void updateHost(){
        Iterator<String> ss = fipList.keySet().iterator();
        while (ss.hasNext()){
            String s = ss.next();
            if(!tflp.containsKey(s)){
                if(!counthost.containsKey(s)){
                    counthost.put(s, 1);
                    System.out.println("cannot ping to " + s + " [1]");
                } else {
                    counthost.replace(s, counthost.get(s)+1);
                    System.out.println("cannot ping to " + s + " ["+counthost.get(s)+"]");
                    if(counthost.get(s) >= 2){
                        System.out.println("remove " + s);
//                        fipList.remove(s);
                        counthost.remove(s);
                        ss.remove();
                    }
                }
            }
        }
//        fipList = tflp;
    }
    static int retired = 0;
    public static String[] getCustomHost(String host){
        String[] temp = {};
        boolean found = false;
        Socket socket;
        DataInputStream dis;
        try {
            socket = new Socket();
            try {
                socket.connect(new InetSocketAddress(host, port), 500);
            } catch (ConnectException | SocketTimeoutException ex) {
                if(retired < 3) {
                    retired++;
                    socket.close();
                    return getCustomHost(host);
                } else {
                    retired = 0;
                    System.out.println("Socket " + host + " not found...");
                    System.out.println("\tSOCKET ERROR - " + ex.getMessage());
                }
                socket.close();
                return null;
            }

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF("ping");
            dos.flush();

            dis = new DataInputStream(socket.getInputStream());

            int r = 0;
            while (!socket.isClosed()) {
                try {
                    if (dis.available() != 0) {
                        String s = dis.readUTF();
//                                tflp.put(host, s.split(":"));
                        temp = s.split(":");
//                                    System.out.println(s);
                        System.out.println("Found socket for: " + host);
                        found = true;
                        retired = 0;

                        dis.close();
                        socket.close();
                        break;
                    } else {
                        r++;
                        if(r >= 2) break;
                    }
                    Thread.sleep(100);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                	dis.close();
                	socket.close();
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(!found) {
            if(retired < 3) {
                retired++;
                return getCustomHost(host);
            } else {
                retired = 0;
                System.out.println("Socket " + host + " not found...");
            }
        }
        return temp;
    }
}