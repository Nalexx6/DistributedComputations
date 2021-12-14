package jdbc;

import models.AirCompany;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AirCompanyDAO {
    public static AirCompany findById(long id) {
        try (Connection connection = DBManager.getConnection()) {
            String sql =
                    "SELECT * FROM air_company WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            AirCompany airCompany = null;
            if (resultSet.next()) {
                airCompany = new AirCompany();
                airCompany.setId(resultSet.getLong(1));
                airCompany.setName(resultSet.getString(2));
            }
            preparedStatement.close();
            return airCompany;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AirCompany findByName(String name) {
        try (Connection connection = DBManager.getConnection()) {
            String sql = "SELECT * FROM air_company WHERE name = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            AirCompany airCompany = null;
            if (resultSet.next()) {
                airCompany = new AirCompany();
                airCompany.setId(resultSet.getLong(1));
                airCompany.setName(resultSet.getString(2));
            }
            preparedStatement.close();
            return airCompany;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean update(AirCompany company) {
        try (Connection connection = DBManager.getConnection()) {
            String sql = "UPDATE air_company SET name = ? WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setLong(2, company.getId());
            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insert(AirCompany company) {
        try (Connection connection = DBManager.getConnection()) {
            String sql = "INSERT INTO air_company (name) VALUES (?)";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, company.getName());
            if(preparedStatement.executeUpdate() > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    company.setId(resultSet.getLong(1));
                } else
                    return false;
                preparedStatement.close();
                System.out.println(company.getId());
                return true;
            }
            DBManager.getInstance().commitAndClose(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(AirCompany company) {
        try (Connection connection = DBManager.getConnection()) {
            String sql = "DELETE FROM air_company WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, company.getId());
            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<AirCompany> findAll() {
        try (Connection connection = DBManager.getConnection()) {
            String sql = "SELECT * FROM air_company";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<AirCompany> list = new ArrayList<>();
            while (resultSet.next()) {
                AirCompany airCompany = new AirCompany();
                airCompany.setId(resultSet.getLong(1));
                airCompany.setName(resultSet.getString(2));
                list.add(airCompany);
            }
            preparedStatement.close();
            DBManager.getInstance().commitAndClose(connection);
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
