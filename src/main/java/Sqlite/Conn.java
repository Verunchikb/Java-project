package Sqlite;

import Parameters.Grand;
import org.apache.commons.lang3.ArrayUtils;

import java.sql.*;
public class Conn {
    public static Statement statmt;
    public static ResultSet resSet;
    public Connection connect() {
        String url = "jdbc:sqlite:D:/sqllite/grands.db";
        Connection conn = null;
        try {conn = DriverManager.getConnection(url);}
        catch (SQLException e) {System.out.println(e.getMessage());}
        return conn;
    }

    public void create(){
        String sql = "CREATE TABLE grand(" +
                "company VARCHAR(255)," +
                "street VARCHAR(255)," +
                "money FLOAT," +
                "year INT," +
                "type VARCHAR(255)," +
                "places INT)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();}
        catch (SQLException e) {throw new RuntimeException(e);}
    }

    public void insert(Grand grand) {
        String sql = "INSERT INTO grand (company,street,money,year,type,places) VALUES(?,?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, grand.getCompany());
            pstmt.setString(2, grand.getStreet());
            pstmt.setString(3, grand.getMoney().replaceAll("\\$","").replaceAll(",",""));
            pstmt.setString(4, grand.getYear());
            pstmt.setString(5, grand.getType());
            pstmt.setString(6, grand.getPlaces());
            pstmt.executeUpdate();}
        catch (SQLException e) {System.out.println(e.getMessage());}
    }

    public void Delete(){
        String sql = "DROP TABLE grand;";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();}
        catch (SQLException e) {throw new RuntimeException(e);}
    }

    public void Task1() {
        String sql = "SELECT year,places FROM grand";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            statmt = conn.createStatement();
            resSet = statmt.executeQuery("SELECT year,places FROM grand");
            String[] years = {"2012","2013","2014","2015","2016"};
            int[] num = {0,0,0,0,0};
            int[] count = {0,0,0,0,0};
            while(resSet.next()){
                String year = resSet.getString("year");
                if(ArrayUtils.indexOf(years,year) != -1) {
                    num[ArrayUtils.indexOf(years, year)] += resSet.getInt("places");
                    count[ArrayUtils.indexOf(years, year)] += 1;
                }
            }
            for (int i = 0; i < 5; i++){
                System.out.println(years[i] + " " + (double)num[i] / count[i]);}
        }
        catch (SQLException e) {throw new RuntimeException(e);}
    }

    public void Task2() {
        String sql = "SELECT money,type FROM grand WHERE type = \"Salon/Barbershop\"";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            statmt = conn.createStatement();
            resSet = statmt.executeQuery("SELECT money,type FROM grand WHERE type = \"Salon/Barbershop\"");
            String name = resSet.getString("type");
            float num = 0.0F;
            int count = 0;
            while(resSet.next()){
                num += Float.valueOf(resSet.getString("money"));
                count += 1;
            }
            System.out.println(name + " " + num/count);}
        catch (SQLException e) {throw new RuntimeException(e);}
    }

    public void Task3() {
        String sql = "SELECT type,places,money FROM grand ORDER BY places DESC";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            statmt = conn.createStatement();
            resSet = statmt.executeQuery(sql);
            while(resSet.next()){
                if (resSet.getFloat("money") <= 55000.00){
                    System.out.println(resSet.getString("type") + " = " + resSet.getInt("places") + " мест");
                break;}
            }}
        catch (SQLException e) {throw new RuntimeException(e);}
    }
}

