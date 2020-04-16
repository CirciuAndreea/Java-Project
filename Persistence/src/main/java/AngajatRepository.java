import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AngajatRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger(AngajatRepository.class);

    private int maxID = 0;

    private int getMaxID() {
        List<Angajat> all = findAll();
        for (Angajat b : all) {
            if (b.getId() > maxID)
                maxID = b.getId();
        }
        return maxID + 1;
    }
    public AngajatRepository(Properties props) {
        dbUtils=new JdbcUtils(props);
        maxID=getMaxID();
    }

    public int save(Angajat ex) {
        logger.traceEntry("saving employee with id {}", ex.getId());
        Connection con = dbUtils.getConnection();
        try {
            PreparedStatement preparedStatement = con.prepareStatement("insert or ignore into angajat(id, username,password) values (?,?,?)");
            preparedStatement.setInt(1, ex.getId());
            preparedStatement.setString(2, ex.getUsername());
            preparedStatement.setString(3, ex.getPassword());
            int result = preparedStatement.executeUpdate();
            return result;
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        logger.traceExit();
        return -1;
    }

    public int delete(Integer ID) {
        logger.traceEntry("deleting employee with {}", ID);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from angajat where ID=?")) {
            preStmt.setInt(1, ID);
            int result = preStmt.executeUpdate();
            return result;
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);

        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        logger.traceExit();
        return -1;

    }

    public int update(Integer ID, Angajat entity) {
        logger.traceEntry("Update a person with id {}", ID);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update angajat set ID=?,username=?,password=? where ID=?")) {
            preStmt.setInt(1, entity.getId());
            preStmt.setString(2, entity.getUsername());
            preStmt.setString(3, entity.getPassword());
            preStmt.setInt(4, ID);
            int result = preStmt.executeUpdate();
            return result;
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public Angajat findOne(int ID) {
        logger.traceEntry("finding employee with id {}", ID);
        Connection con = dbUtils.getConnection();
        Angajat ex = null;
        try {
            PreparedStatement preStmt = con.prepareStatement("select * from angajat where ID=?");
            preStmt.setInt(1, ID);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("username");
                String passwd = rs.getString("password");
                ex = new Angajat(ID,name, passwd);
                logger.traceExit();
                return ex;
            }

        } catch (SQLException e) {
            logger.error(ex);
            System.out.println("Error DB" + ex);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        logger.traceExit("No employee found with this id {}", ID);
        return null;
    }


    public List<Angajat> findAll() {
        Connection con = dbUtils.getConnection();
        List<Angajat> excursii = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from angajat")) {
            try (ResultSet rs = preStmt.executeQuery()) {
                while (rs.next()) {
                    int ID = rs.getInt("ID");
                    String name = rs.getString("username");
                    String passwd = rs.getString("password");
                    Angajat ex = new Angajat(ID,name, passwd);
                    excursii.add(ex);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        logger.traceExit(excursii);
        return excursii;
    }

    public Angajat FindByUsernameAndPassword(String user,String passwd){
        logger.traceEntry("finding employee with username {}",user);
        Connection con=dbUtils.getConnection();
        Angajat ex=null;
        try{
            PreparedStatement preStm= con.prepareStatement("select * from angajat where username=? and password=?");
            preStm.setString(1,passwd);
            preStm.setString(2,user);
            ResultSet rs=preStm.executeQuery();
            while(rs.next()){
                int ID=rs.getInt("ID");
                String name=rs.getString("username");
                String passwdd=rs.getString("password");
                ex= new Angajat(ID,name,passwdd);
                logger.traceExit();
                return ex;
            }
        }catch (SQLException e){
            logger.error(ex);
            System.out.println("Error DB" + ex);
        }
        finally{
            try{
                con.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        logger.traceExit("No employee found with this username {}",user);
        return null;
    }
}