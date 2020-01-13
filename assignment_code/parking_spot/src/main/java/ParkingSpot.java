import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.LeshanClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.net.InetAddress;
import java.util.Arrays;


public class ParkingSpot {

    private static final Logger LOG = LoggerFactory.getLogger(ParkingSpot.class);

    static String endpoint = "..." ; // choose an endpoint name

    private static LeshanClientBuilder builder = new LeshanClientBuilder(endpoint);
    private static LeshanClient client = builder.build();

    public static void main(String[] args) throws Exception {
        LOG.info("Launching client...");

        client.start();
        // Create a JmDNS instance
        final JmDNS jmdns = JmDNS.create();
        for (int i = 1; i < 4; i++) {
            ServiceInfo p = jmdns.getServiceInfo("_parkingserver._udp.", "P"+i,3000);
            if(p != null){
                System.out.println(p.toString());
                System.out.println("p"+1+" is ="+ Arrays.toString(p.getHostAddresses()));
            }
        }

        System.out.println("Ended");
    }
}
