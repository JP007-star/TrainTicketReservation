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
    public String addTrain(ArrayList<Train> trainArray) throws NullPointerException {
        try {
            dataBaseConnection();
            ArrayList<Train> trainArrayList =trainArray;
            for (Train train:trainArrayList) {
                String insertQuery = "insert into trains values(" + train.getTrainNo() + ",'" + train.getTrainName() + "','" + train.getSource() + "','" + train.getDestination()+"'," + train.getTicketPrice() + ");";
                System.out.println(insertQuery);
                statement.execute(insertQuery);
            }

        }
        catch (SQLException | ClassNotFoundException  exception){

        }


        return "success";

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

    public ArrayList<String> fetchSource() {
        ArrayList<String> source=null;
        try {
            dataBaseConnection();
            String query = "select source from Trains";
            ResultSet result = statement.executeQuery(query);
            source=new ArrayList<>();
            while (result.next()) {
                String sourceValue = result.getString("SOURCE");
                source.add(sourceValue);
            }

        }
        catch (SQLException | ClassNotFoundException  exception){

        }

        if(source==null){
            return null;
        }
        return source;
    }

    public ArrayList<String> fetchDestination() {
        ArrayList<String> destination=null;
        try {
            dataBaseConnection();
            String query = "select destination from Trains";
            ResultSet result = statement.executeQuery(query);
            destination=new ArrayList<>();
            while (result.next()) {
                String destinationValue = result.getString("DESTINATION");
                destination.add(destinationValue);
            }

        }
        catch (SQLException | ClassNotFoundException  exception){

        }

        if(destination==null){
            return null;
        }
        return destination;
    }

    public Train findTrainBySource(String source, String destination) {
        try {
            dataBaseConnection();
            String query = "select * from Trains where source='" +source+"' && destination='"+destination+"'";
            ResultSet result = statement.executeQuery(query);
            result.next();
            int trainNO=result.getInt("TRAIN_NO");
            String trainName = result.getString("TRAIN_NAME");
            String sourceResult = result.getString("SOURCE");
            String destinationResult = result.getString("DESTINATION");
            double trainPrice = result.getFloat("TICKET_PRICE");
            train = new Train(trainNO, trainName, sourceResult, destinationResult, trainPrice);
        }
        catch (SQLException | ClassNotFoundException  exception){

        }
        if(train==null){
            return null;
        }
        return train;
    }

    public Train deleteTrain(String trainNo) throws SQLException, ClassNotFoundException {
        dataBaseConnection();
        String query = "delete from Trains where TRAIN_NO="+trainNo;
        System.out.println(query);
        statement.execute(query);
        return null;
    }

    public void updateTrain(Train train) throws SQLException, ClassNotFoundException {
        dataBaseConnection();
        String query = "update Trains set TRAIN_NAME='"+train.trainName+"',SOURCE='"+train.source+"',DESTINATION='"+train.destination+"'," +
                "TICKET_PRICE="+train.ticketPrice+" where TRAIN_NO="+train.trainNo+";";
        System.out.println(query);
        statement.execute(query);

    }
}

