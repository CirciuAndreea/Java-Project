//package Repository;
//
//import Bilet;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class BiletRepositoryTest {
//    BiletRepository bR= new BiletRepository();
//
//    @Test
//    public void save() {
//        Bilet b= new Bilet(20,1,1);
//        b.setId(1);
//        bR.save(b);
//        Bilet gasit=bR.findOne(1);
//        assertEquals(gasit.getNrLocuriDorite(),20);
//    }
//
//    @Test
//    public void findOne() {
//        Bilet b= new Bilet(50,1,2);
//        b.setId(2);
//        bR.save(b);
//        Bilet gasit=bR.findOne(2);
//        assertEquals(gasit.getNrLocuriDorite(),50);
//    }
//
//    @Test
//    public void findAll() {
//        Iterable<Bilet> all=bR.findAll();
//        assertEquals(all.spliterator().getExactSizeIfKnown(),3);
//    }
//
//    @Test
//    public void delete() {
//        Bilet b= new Bilet(70,2,1);
//        b.setId(3);
//        bR.save(b);
//        Bilet b2= new Bilet(49,4,1);
//        b.setId(4);
//        bR.save(b);
//        bR.delete(4);
//        Bilet gasit=bR.findOne(4);
//        assertEquals(gasit,null);
//    }
//
//    @Test
//    public void update() {
//    }
//}