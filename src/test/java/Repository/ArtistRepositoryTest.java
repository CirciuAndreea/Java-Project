//package Repository;
//
//import Domain.Artist;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class ArtistRepositoryTest {
//    ArtistRepository artRepo=new ArtistRepository();
//
//    @Test
//    public void save() {
//        Artist art= new Artist("Brad Pitt");
//        art.setId(3);
//        artRepo.save(art);
//        Artist art_gasit=artRepo.findOne(3);
//        assertEquals(art_gasit.getNume(),"Brad Pitt");
//
//    }
//
//    @Test
//    public void findOne() {
//        Artist art2= new Artist("John");
//        art2.setId(5);
//        artRepo.save(art2);
//        Artist gasit= artRepo.findOne(5);
//        assertEquals(art2.getNume(),"John");
//    }
//
//    @Test
//    public void findAll() {
//        Iterable<Artist> art=artRepo.findAll();
//
//        //assertEquals();
//    }
//
//    @Test
//    public void delete(){
//        Artist art3= new Artist("Lavinia");
//        art3.setId(6);
//        artRepo.save(art3);
//        artRepo.delete(6);
//        Artist gasit= artRepo.findOne(6);
//        assertEquals(gasit,null);
//    }
//
//    @Test
//    public void update(){
////        Artist art4= new Artist("Angelina Jolie");
////        art4.setId(7);
////        artRepo.save(art4);
////        Artist art_nou= new Artist("Zira");
////        art_nou.setId(8);
////        artRepo.update(7,art_nou);
////        assertEquals(art4.getNume(),"Zira");
//
//    }
//}