import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SpectacolRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger(SpectacolRepository.class);

    private int maxID=0;
    private int getmaxID(){
        List<Spectacol> sp= findAll();
        for(Spectacol s: sp){
            if (s.getId()> maxID)
                maxID=s.getId();
        }
        return maxID + 1;
    }

    public SpectacolRepository(Properties props ) {
        dbUtils=new JdbcUtils(props);
        maxID=getmaxID();
    }

    public int save(Spectacol spectacol){
        logger.traceEntry("saving spectacol with id {}", spectacol.getId());
        Connection con= dbUtils.getConnection();
        try{
            PreparedStatement preStmt= con.prepareStatement("insert or ignore into spectacol values (?,?,?,?,?,?)");
            preStmt.setInt(1,spectacol.getId());
            preStmt.setString(2,spectacol.getArtistName());
            preStmt.setString(3,spectacol.getDataTimp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            preStmt.setString(4,spectacol.getLocatie());
            preStmt.setInt(5,spectacol.getNr_loc_vandute());
            preStmt.setInt(6,spectacol.getNr_loc_disponibile());
            int result= preStmt.executeUpdate();
            return result;
        }catch (SQLException e){
            logger.error(e);
            System.out.println("Error DB" + e);
        }
        finally {
            try{
                con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        logger.traceExit();
        return -1;
    }

    public int delete(Integer ID){
        logger.traceEntry("Deleting spectacol with id {}", ID);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt= con.prepareStatement("delete from spectacol where id=?")){
            preStmt.setInt(1,ID);
            int result= preStmt.executeUpdate();
            return result;
        }catch (SQLException e){
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

    public int update(Integer ID,Spectacol s){
        logger.traceEntry("Updating spectacol with id {}", ID);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt= con.prepareStatement("update spectacol set id=?,artistName=?,data=?,localitate=?,nr_loc_vandute=?,nr_loc_disponibile=? where id=?")) {
            preStmt.setInt(1, s.getId());
            preStmt.setString(2, s.getArtistName());
            preStmt.setString(3,s.getDataTimp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            preStmt.setString(4, s.getLocatie());
            preStmt.setInt(5, s.getNr_loc_vandute());
            preStmt.setInt(6, s.getNr_loc_disponibile());
            preStmt.setInt(7, ID);
            int result = preStmt.executeUpdate();
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
    public Spectacol findOne(int ID){
        logger.traceEntry("finding spectacol with id {}", ID);
        Connection con= dbUtils.getConnection();
        Spectacol sp= null;
        try{
            PreparedStatement preStmt= con.prepareStatement("select * from spectacol where id=?");
            preStmt.setInt(1,ID);
            ResultSet rs=preStmt.executeQuery();
            while(rs.next()){
                String a=rs.getString("artistName");
                //LocalDateTime data= LocalDateTime.parse(rs.getString("data"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime data= LocalDateTime.parse(rs.getString("data"),formatter);
                String localitate=rs.getString("localitate");
                int nr_loc_vandute= rs.getInt("nr_loc_vandute");
                int nr_loc_disponibile=rs.getInt("nr_loc_disponibile");
                sp=new Spectacol(ID,data,localitate,nr_loc_vandute,nr_loc_disponibile,a);
                logger.traceExit();
                return sp;
            }

        }catch (SQLException e){
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
        logger.traceExit("No spectacol found with this id {}", ID);
        return null;
    }
    public List<Spectacol> findAll(){
        Connection con=dbUtils.getConnection();
        List<Spectacol> lista=new ArrayList<>();
        try(PreparedStatement preStmt= con.prepareStatement("select * from spectacol")){
            try(ResultSet rs=preStmt.executeQuery()){
                while(rs.next()){
                    int ID=rs.getInt("id");
                    String a=rs.getString("artistName");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    LocalDateTime data= LocalDateTime.parse(rs.getString("data"),formatter);
                    //String inputModified = data.replace ( " " , "T" );
                    //LocalDateTime data= LocalDateTime.parse(rs.getString("data"));
                    String localitate=rs.getString("localitate");
                    int nr_loc_vandute=rs.getInt("nr_loc_vandute");
                    int nr_loc_disponibile=rs.getInt("nr_loc_disponibile");
                    Spectacol sp=new Spectacol(ID,data,localitate,nr_loc_vandute,nr_loc_disponibile,a);

                    lista.add(sp);
                }
            }
        }catch (SQLException e){
            logger.error(e);
            System.out.println("Error DB" +e);
        }
        finally {
            try{
                con.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        logger.traceExit();
        return lista;
    }

    }

