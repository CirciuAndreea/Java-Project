import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServiceImpl implements IService{
    static final Logger logger= LogManager.getLogger(ServiceImpl.class);
    private AngajatRepository userRepository;
    private SpectacolRepository showRepository;
    private BiletRepository biletRepository;
    private Map<String, IObserver> loggedClients;

    public ServiceImpl(AngajatRepository uRepo,SpectacolRepository sRepo,BiletRepository bRepo){
        userRepository=uRepo;
        showRepository=sRepo;
        biletRepository=bRepo;
        loggedClients= new ConcurrentHashMap<>();
    }

    private static final int defaultThreadsNo = 5;

    private void notifyTicketsBought(Spectacol show) throws ServiceException{
        List<Angajat> users=userRepository.findAll();
        logger.trace("FULL SERVER: notify tickets bought!");
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);

        for(Angajat us: users){
            IObserver chatClient=loggedClients.get(us.getUsername());
            if(chatClient!= null)
                executor.execute(() -> {
                    try{
                        logger.trace("I AM IN NOTIFY TICKETS BOUGHT");
                        System.out.println("Notifying [" + us.getUsername() + "] tickets were bought for show [" + + show.getId() + "].");
                        chatClient.notifyTicketsSold(show);
                    }catch (ServiceException e){
                        System.out.println("Error notifying friend" + e);
                    }
                });
        }
        executor.shutdown();

    }

    public synchronized void login(Angajat user,IObserver client) throws ServiceException{
        logger.trace("FULL SERVER: login RECEIVED COMMAND @"+ LocalDate.now());
        Angajat userR = userRepository.FindByUsernameAndPassword(user.getUsername(),user.getPassword());
        if (userR != null) {
            if (loggedClients.get(userR.getUsername()) != null)
                throw new ServiceException("User already logged in.");
            loggedClients.put(userR.getUsername(), client);
        } else
            throw new ServiceException("Authentication failed.");
        logger.trace("FULL SERVER: login SENT RETURN TO SERVER PROXY");
    }

    public synchronized void logout(Angajat user,IObserver client) throws ServiceException{
        logger.trace("FULL SERVER: logout request");
        IObserver localClient=loggedClients.remove(user.getPassword());
        if (localClient==null)
            throw new ServiceException("User "+user.getUsername()+" is not logged in.");
        logger.trace("FULL SERVER: logout sent response to SERVER PROXY");
    }

    @Override
    public synchronized Spectacol[] findAllShows() throws ServiceException{
        logger.trace("FULL SERVER: findAllShows RECEIVED COMMAND");
        List<Spectacol> fromDBresult = StreamSupport.stream(showRepository.findAll().spliterator(),false).collect(Collectors.toList());

        Spectacol[] shows= new Spectacol[fromDBresult.size()];
        for(int i=0;i< fromDBresult.size();i++){
            shows[i]=fromDBresult.get(i);
        }
        logger.trace("FULL SERVER: findAllShows sent response to SERVER PROXY");
        return shows;
    }

    @Override
    public synchronized Spectacol ticketsSold(Spectacol spectacol,Bilet bilet) throws ServiceException{
        logger.trace("FULL SERVER: ticketsSold RECEIVED COMMAND @"+ LocalDate.now());
        Spectacol sh = showRepository.findOne(spectacol.getId());
        Spectacol shUpd=new Spectacol(sh.getId(),sh.getDataTimp(),sh.getLocatie(),sh.getNr_loc_vandute()+bilet.getNrLocuriDorite(),sh.getNr_loc_disponibile()-
                bilet.getNrLocuriDorite(),sh.getArtistName());
        showRepository.update(sh.getId(),shUpd);
        biletRepository.save(bilet);
        Runnable runnable = () -> {// hopefully not a deadlock (has enough time to finish)
            try {
                Thread.sleep(1000);
                notifyTicketsBought(shUpd);
            } catch (ServiceException | InterruptedException e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        logger.trace("FULL SERVER: ticketsSold SENT OBSERVER COMMAND TO notifyTicketsBought");
        return shUpd;
    }
}
