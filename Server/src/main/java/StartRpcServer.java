import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;

    public static void main(String[] args){
        Properties serverProps= new Properties();
        try{
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set.");
            serverProps.list(System.out);
        }catch (IOException e){
            System.err.println("Cannot find server.properties " + e);
            return;
        }

        Properties properties= new Properties();
        try{
            System.out.println(properties);
            InputStream a=JdbcUtils.class.getResourceAsStream("/bd.config");
            System.out.println(a);
            properties.load(a);
            properties.list(System.out);
        }catch (IOException e){
            System.err.println("Cannot find db.config " + e);
            return;
        }
        JdbcUtils jdbcInv= new JdbcUtils(properties);

        AngajatRepository userRepo= new AngajatRepository(properties);
        SpectacolRepository showRepo= new SpectacolRepository(properties);
        BiletRepository ticketRepo= new BiletRepository(properties);

        IService chatServerImpl= new ServiceImpl(userRepo, showRepo,ticketRepo);
        int serverPort= defaultPort;
        try{
            serverPort=Integer.parseInt(serverProps.getProperty("server.port"));

        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new RpcConcurrentServer(serverPort, chatServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        } finally {
            try {
                server.stop();
            } catch (ServerException e) {
                System.err.println("Error stopping server " + e.getMessage());
            }
        }
    }
}
