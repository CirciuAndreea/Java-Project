public class Bilet extends Entity<Integer> {
    private int nrLocuriDorite;
    private String buyerName;
    private int id_spectacol;

    public Bilet(int ID,int nrLocuriDorite,String bName, int id_spectacol) {
        super(ID);
        this.nrLocuriDorite = nrLocuriDorite;
        this.buyerName=bName;
        this.id_spectacol = id_spectacol;
    }

    public int getNrLocuriDorite() {
        return nrLocuriDorite;
    }

    public void setNrLocuriDorite(int nrLocuriDorite) {
        this.nrLocuriDorite = nrLocuriDorite;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public int getId_spectacol() {
        return id_spectacol;
    }

    public void setId_spectacol(int id_spectacol) {
        this.id_spectacol = id_spectacol;
    }
}
