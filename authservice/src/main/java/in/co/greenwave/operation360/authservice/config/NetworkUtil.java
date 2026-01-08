package in.co.greenwave.operation360.authservice.config;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Utility class for fetching network details.
 * 
 * This class provides methods to retrieve network-related information, such as
 * the IP address and MAC address of the local machine. It includes a method to
 * obtain these details and a nested class to encapsulate the network information.
 * 
 * This class is made to store the network-related information of the login user but not used yet. 
 */
public class NetworkUtil {

    /**
     * Retrieves network details including IP address and MAC address.
     * 
     * @return a {@link NetworkDetails} object containing the IP address and MAC address.
     *         If the MAC address cannot be found or an error occurs, appropriate error messages
     *         are returned.
     */
    public static NetworkDetails getNetworkDetails() {
        try {
            // Get the local host's InetAddress
            InetAddress ip = InetAddress.getLocalHost();
            
            // Get the NetworkInterface for the given InetAddress
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            // Retrieve the hardware (MAC) address
            byte[] mac = network.getHardwareAddress();
            if (mac == null) {
                // Return IP address and a message indicating MAC address was not found
                return new NetworkDetails(ip.getHostAddress(), "MAC address not found!");
            }

            // Convert the MAC address to a readable string format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            return new NetworkDetails(ip.getHostAddress(), sb.toString());

        } catch (UnknownHostException | SocketException e) {
            // Log any exceptions and return error messages
            e.printStackTrace();
            return new NetworkDetails("Error fetching IP address!", "Error fetching MAC address!");
        }
    }

    /**
     * Encapsulates network details including IP address and MAC address.
     */
    public static class NetworkDetails {
        private String ipAddress;
        private String macAddress;

        /**
         * Constructs a {@link NetworkDetails} object with the specified IP address and MAC address.
         * 
         * @param ipAddress the IP address
         * @param macAddress the MAC address
         */
        public NetworkDetails(String ipAddress, String macAddress) {
            this.ipAddress = ipAddress;
            this.macAddress = macAddress;
        }

        /**
         * Gets the IP address.
         * 
         * @return the IP address
         */
        public String getIpAddress() {
            return ipAddress;
        }

        /**
         * Gets the MAC address.
         * 
         * @return the MAC address
         */
        public String getMacAddress() {
            return macAddress;
        }
    }
}
