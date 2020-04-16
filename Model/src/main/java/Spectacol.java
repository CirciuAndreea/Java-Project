import java.time.LocalDateTime;

public class Spectacol extends Entity<Integer>{
    private LocalDateTime dataTimp;
    private String locatie;
    private int nr_loc_vandute;
    private int nr_loc_disponibile;
    private String artistName;

    public Spectacol(int id,LocalDateTime d,  String locatie, int nr_loc_vandute, int nr_loc_disponibile,String Artist) {
        super(id);
        this.dataTimp=d;
        this.locatie = locatie;
        this.nr_loc_vandute = nr_loc_vandute;
        this.nr_loc_disponibile = nr_loc_disponibile;
        this.artistName=Artist;
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

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public int getNr_loc_vandute() {
        return nr_loc_vandute;
    }

    public void setNr_loc_vandute(int nr_loc_vandute) {
        this.nr_loc_vandute = nr_loc_vandute;
    }

    public int getNr_loc_disponibile() {
        return nr_loc_disponibile;
    }

    public void setNr_loc_disponibile(int nr_loc_disponibile) {
        this.nr_loc_disponibile = nr_loc_disponibile;
    }

    @Override
    public String toString() {
        return super.toString()+ ";" + dataTimp +
                ";" + locatie +
                ";" + nr_loc_vandute +
                ";" + nr_loc_disponibile +
                ";" + artistName;
    }
}
