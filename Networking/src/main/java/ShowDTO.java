import java.io.Serializable;
import java.time.LocalDateTime;

public class ShowDTO implements Serializable {
    private int id;
    private LocalDateTime dataTimp;
    private String location;
    private int nrAvailableSeats;
    private int nrSoldSeats;
    private String artistName;

    public ShowDTO(int ID, LocalDateTime d,String location, int nrSoldSeats,  int nrAvailableSeats, String Artist) {
        this.id=ID;
        this.dataTimp=d;
        this.location = location;
        this.nrSoldSeats = nrSoldSeats;
        this.nrAvailableSeats = nrAvailableSeats;
        this.artistName = Artist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public LocalDateTime getDataTimp() {
        return dataTimp;
    }

    public void setDataTimp(LocalDateTime dataTimp) {
        this.dataTimp = dataTimp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNrAvailableSeats() {
        return nrAvailableSeats;
    }

    public void setNrAvailableSeats(int nrAvailableSeats) {
        this.nrAvailableSeats = nrAvailableSeats;
    }

    public int getNrSoldSeats() {
        return nrSoldSeats;
    }

    public void setNrSoldSeats(int nrSoldSeats) {
        this.nrSoldSeats = nrSoldSeats;
    }

}

