public class DTOUtils {
    public static Angajat getFromDTO(UserDTO usdto){
        int id=usdto.getId();
        String pass=usdto.getPassword();
        String username=usdto.getUsername();
        Angajat client=new Angajat(id,username,pass);
        return client;
    }
    public static UserDTO getDTO(Angajat user){
        int id=user.getId();
        String pass=user.getPassword();
        String nume=user.getUsername();
        return new UserDTO(id,pass, nume);
    }

    public static UserDTO[] getDTO(Angajat[] users){
        UserDTO[] frDTO= new UserDTO[users.length];
        for(int i=0;i<users.length;i++)
            frDTO[i]=getDTO(users[i]);
        return frDTO;
    }

    public static Angajat[] getFromDTO(UserDTO[] users){
        Angajat[] friends= new Angajat[users.length];
        for(int i=0;i<users.length;++i){
            friends[i]=getFromDTO(users[i]);
        }
        return friends;
    }

    public static Spectacol getFromDTO(ShowDTO showDTO){
        return new Spectacol(showDTO.getId(),showDTO.getDataTimp(),showDTO.getLocation(),showDTO.getNrSoldSeats(),showDTO.getNrAvailableSeats(),showDTO.getArtistName());

    }

    public static Spectacol[] getFromDTO(ShowDTO[] showDTOs){
        Spectacol[] showuri=new Spectacol[showDTOs.length];
        for(int i=0;i< showDTOs.length;++i){
            showuri[i]=getFromDTO(showDTOs[i]);
        }
        return showuri;
    }

    public static ShowDTO getDTO(Spectacol spectacol){
        return new ShowDTO(spectacol.getId(),spectacol.getDataTimp(),spectacol.getLocatie(),spectacol.getNr_loc_vandute(),spectacol.getNr_loc_disponibile(),spectacol.getArtistName());
    }

    public static ShowDTO[] getDTO(Spectacol[] showuri){
        ShowDTO[] showuriDTO=new ShowDTO[showuri.length];
        for(int i=0;i< showuri.length;i++){
            showuriDTO[i]= getDTO(showuri[i]);
        }
        return showuriDTO;
    }

    public static Bilet[] getFromDTO(TicketDTO[] bileteDTOs){
        Bilet[] bilete= new Bilet[bileteDTOs.length];
        for(int i=0;i<bilete.length;i++){
            bilete[i]=getFromDTO(bileteDTOs[i]);
        }
        return bilete;
    }

    public static TicketDTO[] getDTO(Bilet[] bilete){
        TicketDTO[] biletdtos = new TicketDTO[bilete.length];
        for (int i = 0; i < bilete.length; i++) {
            biletdtos[i] = getDTO(bilete[i]);
        }
        return biletdtos;
    }
    public static TicketDTO getDTO(Bilet bilet){
        return new TicketDTO(bilet.getId(),bilet.getNrLocuriDorite(),bilet.getBuyerName(),bilet.getId_spectacol());
    }
    public static Bilet getFromDTO(TicketDTO biletDTO){
        return new Bilet(biletDTO.getId(),biletDTO.getNrWantedSeats(),biletDTO.getBuyerName(),biletDTO.getIdShow());
    }
}
