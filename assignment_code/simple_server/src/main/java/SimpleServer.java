import org.eclipse.leshan.server.californium.LeshanServer;
import org.eclipse.leshan.server.californium.LeshanServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpleServer {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleServer.class);

    private static LeshanServerBuilder builder = new LeshanServerBuilder();
    private static LeshanServer server = builder.build();

    public static void main(String[] args) {
        LOG.info("Launching server...");
        server.start();
    }
}
