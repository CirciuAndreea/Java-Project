import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BiletRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger(BiletRepository.class);

    private int maxId=0;
    private int getMaxId(){
        List<Bilet> all=findAll();
        for (Bilet b:all){
            if(b.getId()> maxId)
                maxId=b.getId();
        }
        return maxId+1;
    }
    public BiletRepository(Properties props) {
        dbUtils=new JdbcUtils(props);
        maxId=getMaxId();
    }

    public int save(Bilet bilet){
        logger.traceEntry("saving ticket with id {}", bilet.getId());
        Connection con=dbUtils.getConnection();
        try{
            PreparedStatement preparedStatement=con.prepareStatement("insert or ignore into bilet values (?,?,?,?)");
            preparedStatement.setInt(1,bilet.getId());
            preparedStatement.setInt(2,bilet.getNrLocuriDorite());
            preparedStatement.setString(3,bilet.getBuyerName());
            preparedStatement.setInt(4,bilet.getId_spectacol());
            int result=preparedStatement.executeUpdate();
            return result;
        }catch (SQLException e){
            logger.error(e);
            System.out.println("Error DB" + e);
        }
        finally{
            try{
                con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        logger.traceExit();
        return -1;
    }
    public Bilet findOne(int ID){
        logger.traceEntry("finding ticket with id{}",ID);
        Connection con=dbUtils.getConnection();
        Bilet bilet=null;
        try{
            PreparedStatement preStmt=con.prepareStatement("select * from bilet where ID=?");
            preStmt.setInt(1,ID);
            ResultSet rs=preStmt.executeQuery();
            while(rs.next()){
                int nrLocuriDorite=rs.getInt("nrLocuriDorite");
                String buyerName=rs.getString("buyerName");
                int id_spectacol=rs.getInt("id_spectacol");
                bilet=new Bilet(ID,nrLocuriDorite,buyerName,id_spectacol);
                logger.traceExit();
                return bilet;
            }
        }catch(SQLException e){
            logger.error(e);
            System.out.println("Error DB" + e);
        }
        finally{
            try{
                con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        logger.traceExit("No ticket found with this is{}", ID);
        return null;
    }

    public List<Bilet> findAll(){
        Connection con=dbUtils.getConnection();
        List<Bilet> lista=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from bilet")){
            try(ResultSet rs= preStmt.executeQuery()){
                while(rs.next()){
                    int id=rs.getInt("ID");
                    int nrLocuriDorite=rs.getInt("nrLocuriDorite");
                    String buyerName=rs.getString("buyerName");
                    int id_spectacol=rs.getInt("id_spectacol");

                    Bilet bilet=new Bilet(id,nrLocuriDorite,buyerName,id_spectacol);

                    lista.add(bilet);

                }
            }
        }catch(SQLException e){
            logger.error(e);
            System.out.println("Error BD" + e);
        }
        finally{
            try{
                con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        logger.traceExit(lista);
        return lista;
    }
    public int delete(Integer ID){
        logger.traceEntry("deleting ticket with {}",ID);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt= con.prepareStatement("delete from bilet where ID=?")){
            preStmt.setInt(1,ID);
            int result=preStmt.executeUpdate();
            return result;
        }catch(SQLException e){
            logger.error(e);
            System.out.println("Error DB" + e);
        }
        finally {
            try{
                con.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        logger.traceExit();
        return -1;
    }

    public int update(Integer ID,Bilet bil){
        logger.traceEntry("update a ticket with id{}",ID);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update bilet set ID=?,nrLocuriDorite?,buyerName=?,id_spectacol=? where ID=?")){
            preStmt.setInt(1,bil.getId());
            preStmt.setInt(2,bil.getNrLocuriDorite());
            preStmt.setString(3,bil.getBuyerName());
            preStmt.setInt(4,bil.getId_spectacol());
            preStmt.setInt(5,ID);
            int result=preStmt.executeUpdate();
            return result;
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB" + ex);
        }
        finally {
            try{
                con.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return -1;
    }

}
