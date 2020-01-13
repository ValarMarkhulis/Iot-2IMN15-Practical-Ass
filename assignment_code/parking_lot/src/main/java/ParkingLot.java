import org.eclipse.leshan.server.californium.LeshanServer;
import org.eclipse.leshan.server.californium.LeshanServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.net.InetAddress;


public class ParkingLot {

    private static final Logger LOG = LoggerFactory.getLogger(ParkingLot.class);

    private static LeshanServerBuilder builder = new LeshanServerBuilder();
    private static LeshanServer server = builder.build();

    public static void main(String[] args) throws Exception {
        LOG.info("Launching server...");
        // Register a service to DNS-SD


        // Create a JmDNS instance
        final JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());

        // Publish Leshan CoAP Service
        ServiceInfo coapServiceInfo = ServiceInfo.create("_parkingserver._udp.", "P1", 5683, "");
        jmdns.registerService(coapServiceInfo);

        server.start();


        // De-register on shutdown and stop client.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                server.destroy(); // send de-registration request before destroy
                jmdns.unregisterAllServices();
            }
        });

    }
}
