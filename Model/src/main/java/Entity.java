public class Entity<ID> {
    private ID id;
    public Entity(ID id) {this.id=id;}

    @Override
    public String toString() {
        return  id.toString();
    }

    public ID getId(){ return id;}
    public void setId(ID id){this.id=id;}

}
