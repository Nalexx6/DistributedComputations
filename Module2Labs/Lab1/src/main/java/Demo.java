import jdbc.AirCompanyDao;
import jdbc.DBManager;
import models.AirCompany;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.ibatis.jdbc.*;

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
        System.out.println(AirCompanyDao.insert(new AirCompany("2", "MAU")));
        System.out.println(AirCompanyDao.findByName("MAU").getName());
    }
}
