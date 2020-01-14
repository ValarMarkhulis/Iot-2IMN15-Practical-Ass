import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.leshan.LwM2m;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.LeshanClientBuilder;
import org.eclipse.leshan.client.object.Server;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.model.StaticModel;
import org.eclipse.leshan.core.request.BindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import static org.eclipse.leshan.LwM2mId.SECURITY;
import static org.eclipse.leshan.LwM2mId.SERVER;
import static org.eclipse.leshan.client.object.Security.noSec;
import static org.eclipse.leshan.client.object.Security.noSecBootstap;


public class ParkingSpot {

    private static final Logger LOG = LoggerFactory.getLogger(ParkingSpot.class);

    static String endpoint = "Megalulz-ParkingSpot" ; // choose an endpoint name
    private final static String[] modelPaths = new String[]{"32700.xml"};

    private static final int OBJECT_ID_PARKING_SPOT = 32700;

    public static void main(String[] args) throws Exception {
        LOG.info("Launching client...");

        try {
            endpoint = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {

        }



        // Create a JmDNS instance
        final JmDNS jmdns = JmDNS.create();
        ServiceInfo p = null;
        for (int i = 1; i < 2; i++) {
            p = jmdns.getServiceInfo("_parkingserver._udp.", "P"+i,3000);
            if(p != null){
                System.out.println(p.toString());
                System.out.println("p"+1+" is ="+ Arrays.toString(p.getHostAddresses()));
            }
        }

        //get serverURI
        String serverURI = "coap://192.168.0.103:" + LwM2m.DEFAULT_COAP_PORT; //TODO: SHOULD GET FROM JmDNS

        // get local address
        String localAddress = "localhost";
        int localPort = 7331;

        try {
            createAndStartClient(endpoint, localAddress, localPort, serverURI);
        } catch (Exception e) {
            System.err.println("Unable to create and start client ...");
            e.printStackTrace();
            return;
        }

        System.out.println("Ended");

    }

    private static void createAndStartClient(String endpoint, String localAddress, int localPort, String serverURI) {

        // Initialize model
        List<ObjectModel> models = ObjectLoader.loadDefault();
        models.addAll(ObjectLoader.loadDdfResources("/models", modelPaths));

        // Initialize object list
        ObjectsInitializer initializer = new ObjectsInitializer(new StaticModel(models));
        initializer.setInstancesForObject(OBJECT_ID_PARKING_SPOT, new ParkingSpot_model());
        initializer.setInstancesForObject(SECURITY, noSecBootstap(serverURI));
        initializer.setClassForObject(SERVER, Server.class);
        initializer.setInstancesForObject(SECURITY, noSec(serverURI, 123));
        initializer.setInstancesForObject(SERVER, new Server(123, 30, BindingMode.U, false));

        List<LwM2mObjectEnabler> enablers = initializer.createAll();

        // Create CoAP Config
        NetworkConfig coapConfig;
        File configFile = new File(NetworkConfig.DEFAULT_FILE_NAME);
        if (configFile.isFile()) {
            coapConfig = new NetworkConfig();
            coapConfig.load(configFile);
        } else {
            coapConfig = LeshanClientBuilder.createDefaultNetworkConfig();
            coapConfig.store(configFile);
        }

        // Create client
        LeshanClientBuilder builder = new LeshanClientBuilder(endpoint);
        builder.setLocalAddress(localAddress, localPort);
        builder.setObjects(enablers);
        builder.setCoapConfig(coapConfig);
        final LeshanClient client = builder.build();

        // De-register on shutdown and stop client.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                client.destroy(true); // send de-registration request before destroy
            }
        });
        client.start();

        String registrationId = client.getRegistrationId();
        System.out.printf("Registered as %s\n", registrationId);


    }
}
