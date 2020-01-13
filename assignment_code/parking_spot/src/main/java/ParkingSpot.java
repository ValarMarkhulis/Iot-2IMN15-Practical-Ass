import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.LeshanClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ParkingSpot {

    private static final Logger LOG = LoggerFactory.getLogger(ParkingSpot.class);

    static String endpoint = "..." ; // choose an endpoint name

    private static LeshanClientBuilder builder = new LeshanClientBuilder(endpoint);
    private static LeshanClient client = builder.build();

    public static void main(String[] args) {
        LOG.info("Launching client...");
        client.start();
    }
}
