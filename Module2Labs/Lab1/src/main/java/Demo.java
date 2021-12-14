import jdbc.AirCompanyDAO;
import jdbc.DBManager;
import models.AirCompany;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Demo {

    public static void main(String[] args){

        try {
            Connection con = DBManager.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("show grants;");
            while (rs.next()){
                System.out.println(rs.getString("Grants for root@localhost"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println(AirCompanyDAO.insert(new AirCompany(2, "MAU")));
        System.out.println(AirCompanyDAO.findByName("MAU").getName());
    }
}
