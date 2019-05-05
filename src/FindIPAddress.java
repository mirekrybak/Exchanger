import java.net.InetAddress;
import java.net.UnknownHostException;

public class FindIPAddress {
    public static void main(String[] args) {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println("Nie uzyskano adresu iP tego komputera.");
            System.exit(0);
        }

        String ip = address.getHostAddress();
        String name = address.getHostName();
        System.out.println("Adres ip: " + ip + ", nazwa hosta: " + name);


    }
}
