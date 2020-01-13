import org.eclipse.leshan.LwM2m;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.server.californium.LeshanServer;
import org.eclipse.leshan.server.californium.LeshanServerBuilder;
import org.eclipse.leshan.server.model.LwM2mModelProvider;
import org.eclipse.leshan.server.model.VersionedModelProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.EventServlet;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.net.InetAddress;
import java.util.List;

public class ParkingLot {

    private static final Logger LOG = LoggerFactory.getLogger(ParkingLot.class);

    private final static String[] modelPaths = new String[] { "32702.xml"};

    private static LeshanServerBuilder builder = new LeshanServerBuilder();
    private static LeshanServer server = builder.build();

    public static void main(String[] args) throws Exception {
        LOG.info("Launching server...");

        // Define model provider
        List<ObjectModel> models = ObjectLoader.loadDefault();
        models.addAll(ObjectLoader.loadDdfResources("/models/", modelPaths));
        LwM2mModelProvider modelProvider = new VersionedModelProvider(models);
        builder.setObjectModelProvider(modelProvider);

        // Create Servlet
        EventServlet eventServlet = new EventServlet(server, 1337);

        // Create a JmDNS instance
        final JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost().getCanonicalHostName());

        // Publish Leshan CoAP Service
        ServiceInfo coapServiceInfo = ServiceInfo.create("_parkingserver._udp.", "P1", LwM2m.DEFAULT_COAP_PORT, "");
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
