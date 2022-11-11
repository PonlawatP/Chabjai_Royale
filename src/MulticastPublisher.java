import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
public class MulticastPublisher {
    private static String MULTICAST_INTERFACE = "eth0";
    private static final int MULTICAST_PORT = 4321;
    private static final String MULTICAST_IP = "230.0.0.0";
    public void sendMessage(String ip, String iface, int port,
                            String message) throws IOException {
        DatagramChannel datagramChannel=DatagramChannel.open();
        datagramChannel.bind(null);
        NetworkInterface networkInterface=NetworkInterface
                .getByName(iface);
        datagramChannel.setOption(StandardSocketOptions
                .IP_MULTICAST_IF,networkInterface);
        ByteBuffer byteBuffer=ByteBuffer.wrap
                (message.getBytes());
        InetSocketAddress inetSocketAddress=new
                InetSocketAddress(ip,port);
        datagramChannel.send(byteBuffer,inetSocketAddress);
    }
    public static void main(String[] args) throws IOException {
        for(NetworkInterface s : NetworkInterface.networkInterfaces().toList()){
//            System.out.println(s.getName() + " : " + s.supportsMulticast() + " : " + s.isUp() + " : " + s.isLoopback());
            if(s.supportsMulticast() && s.isUp() && !s.isLoopback()){
                MULTICAST_INTERFACE = s.getName();
                break;
            }
        }
        MulticastPublisher mp=new MulticastPublisher();
        mp.sendMessage(MULTICAST_IP,MULTICAST_INTERFACE,
                MULTICAST_PORT,"Hi there!");
    }
}