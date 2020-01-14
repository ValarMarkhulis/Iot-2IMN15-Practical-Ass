import org.eclipse.leshan.LwM2m;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.node.LwM2mObject;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.node.LwM2mSingleResource;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.request.ReadRequest;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.server.californium.LeshanServer;
import org.eclipse.leshan.server.californium.LeshanServerBuilder;
import org.eclipse.leshan.server.model.LwM2mModelProvider;
import org.eclipse.leshan.server.model.VersionedModelProvider;
import org.eclipse.leshan.server.registration.Registration;
import org.eclipse.leshan.server.registration.RegistrationIdProvider;
import org.eclipse.leshan.server.registration.RegistrationListener;
import org.eclipse.leshan.server.registration.RegistrationUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.EventServlet;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.net.InetAddress;
import java.util.Collection;
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

        server.getRegistrationService().addListener(new RegistrationListener() {

            public void registered(Registration registration, Registration previousReg,
                                   Collection<Observation> previousObsersations) {
                System.out.println("new device: " + registration.getEndpoint());
                try {
//                    ReadResponse response = server.send(registration, new ReadRequest(3,0,13));
                    ReadResponse response = server.send(registration, new ReadRequest(6,0,0));
                    ReadResponse response2 = server.send(registration, new ReadRequest(6,0,1));

                    if (response.isSuccess()) {
                        LwM2mSingleResource content = (LwM2mSingleResource) response.getContent();
                        //System.out.println("Here!");
                        //System.out.println("Device time:" + content.getValue().toString());
                        System.out.println(content.getValue().toString());

                    }else {
                        System.out.println("Failed to read:" + response.getCode() + " " + response.getErrorMessage());
                    }
                    if (response2.isSuccess()) {
                        LwM2mSingleResource content = (LwM2mSingleResource) response2.getContent();
                        //System.out.println("Here!");
                        //System.out.println("Device time:" + content.getValue().toString());
                        System.out.println(content.getValue().toString());

                    }else {
                        System.out.println("Failed to read:" + response2.getCode() + " " + response2.getErrorMessage());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            public void updated(RegistrationUpdate update, Registration updatedReg, Registration previousReg) {
                System.out.println("device is still here: " + updatedReg.getEndpoint());
            }

            public void unregistered(Registration registration, Collection<Observation> observations, boolean expired,
                                     Registration newReg) {
                System.out.println("device left: " + registration.getEndpoint());
            }
        });

    }

}
