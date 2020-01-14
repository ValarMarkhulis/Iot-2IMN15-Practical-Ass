import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.LeshanClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.UnknownHostException;
import java.net.InetAddress;

import java.util.ArrayList;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.jmdns.impl.ServiceEventImpl;


public class ParkingSpot {

    private static final Logger LOG = LoggerFactory.getLogger(ParkingSpot.class);
    private static ArrayList parkingServerNames = new ArrayList<String>();

    // static String endpoint = "..." ; // choose an endpoint name

    // private static LeshanClientBuilder builder = new LeshanClientBuilder(endpoint);
    // private static LeshanClient client = builder.build();

    private static class SampleListener implements ServiceListener {
        @Override
        public void serviceAdded(ServiceEvent event) {
            System.out.println("Service added: " + event.getInfo());
            parkingServerNames.add(event.getName());
        }

        @Override
        public void serviceRemoved(ServiceEvent event) {
            System.out.println("Service removed: " + event.getInfo());
        }

        @Override
        public void serviceResolved(ServiceEvent event) {
            System.out.println("Service resolved: " + event.getInfo());
        }
    }

    public static void main(String[] args) {
        try {
            // Create a JmDNS instance
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());

            // Add a service listener
            jmdns.addServiceListener("_parkingserver._udp.local.", new SampleListener());

            // Wait a bit
            Thread.sleep(3000);

            for (int i = 0; i < parkingServerNames.size(); i++) {
               System.out.println(parkingServerNames.get(i));
            }
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Interrupt receiverd");
        }

        // LOG.info("Launching client...");
        // client.start();
    }
}
