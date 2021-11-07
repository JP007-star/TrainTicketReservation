package com.torryharris.model;

import java.sql.*;
import java.util.ArrayList;

public class TrainDAO {
    String driverName;
    String db_URL;
    String username;
    String password;
    Connection connection;
    Statement statement;
    Train train;
    ArrayList<Train> trainArrayList;

    //This function test connection with mysql DB
    public void dataBaseConnection() throws ClassNotFoundException, SQLException {
        db_URL = "jdbc:mysql://localhost:3306/trainticketreservationsystem";
        username = "root";
        password = "Prasad@66";
        driverName = "com.mysql.cj.jdbc.Driver";
        Class.forName(driverName);
        connection = DriverManager.getConnection(db_URL, username, password);
        statement = connection.createStatement();
    }

    //This function is used to find train by ID
    public Train findTrain(int trainNO)  throws NullPointerException  {
        try {
            dataBaseConnection();
            String query = "select * from Trains where TRAIN_NO=" + trainNO;
            ResultSet result = statement.executeQuery(query);
            result.next();
            String trainName = result.getString("TRAIN_NAME");
            String source = result.getString("SOURCE");
            String destination = result.getString("DESTINATION");
            double trainPrice = result.getFloat("TICKET_PRICE");
            train = new Train(trainNO, trainName, source, destination, trainPrice);
        }
        catch (SQLException | ClassNotFoundException  exception){

        }
        if(train==null){
           return null;
        }
        return train;
    }

    //This function is used to fetch all train from database
    public ArrayList<Train> fetchTrains()  throws NullPointerException  {
        try {
            dataBaseConnection();
            String query = "select * from Trains";
            ResultSet result = statement.executeQuery(query);
            trainArrayList=new ArrayList<>();
            while (result.next()) {
                int trainNo = result.getInt("TRAIN_NO");
                String trainName = result.getString("TRAIN_NAME");
                String source = result.getString("SOURCE");
                String destination = result.getString("DESTINATION");
                double trainPrice = result.getFloat("TICKET_PRICE");
                train = new Train(trainNo, trainName, source, destination, trainPrice);
                trainArrayList.add(train);
            }

        }
        catch (SQLException | ClassNotFoundException  exception){

        }

        if(trainArrayList==null){
            return null;
        }
        return trainArrayList;
    }
}

