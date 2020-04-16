public interface IService {
    void login(Angajat user, IObserver client) throws ServiceException;
    void logout(Angajat user,IObserver client) throws ServiceException;
    Spectacol[] findAllShows() throws ServiceException;
    Spectacol ticketsSold(Spectacol spectacol, Bilet bilet) throws ServiceException;
}
