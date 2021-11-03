package com.torryharris.model;


import java.io.*;
import java.util.ArrayList;

public class Ticket {
    int counter;
    String pnr;
    String travelDate;
    Train train;
    Ticket ticket;
    Passenger passenger;
    double price =0;
    double totalPrice;

    public Ticket(Passenger passenger, Train train) {
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "pnr='" + pnr + '\'' +
                ", travelDate='" + travelDate + '\'' +
                ", train=" + train +
                '}';
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Ticket(String travelDate, com.torryharris.model.Train train) {
        this.travelDate = travelDate;
        this.train = train;
    }

    public Ticket() {

    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public String generatePNR(){
        Ticket ticket=new Ticket(travelDate,train);
        char source=train.getSource().charAt(0);
        char destination=train.getDestination().charAt(0);
        String travelDate= ticket.getTravelDate();
        String[] dt=ticket.getTravelDate().split("-",3);
        String pnrString=source+""+destination+"_"+travelDate+"_"+ updateCounter();
        return pnrString;
    }
    public void addPassenger(String name,int age,char gender,Train train){
        Ticket ticket=new Ticket();
        Passenger passenger = new Passenger(name, age, gender);
        ArrayList<Passenger> passengerArrayList=new ArrayList<>();
        passengerArrayList.add(passenger);
        ticket.calcPassengerFare(passenger,train);
        ticket.calcTotalTicketPrice();
        ticket.generateTicket(passenger);
    }
    private double calcPassengerFare(Passenger passenger,Train train){

       if(passenger.getAge()<=12)
        {
            price = train.getTicketPrice()/2;
            return price;

        }
        else if(passenger.getAge()>=60)
        {
            price = train.getTicketPrice()*(60/100);
            return price;

        }
        else if(passenger.getGender()=='F')
        {
            price = train.getTicketPrice()-(train.getTicketPrice()*(25/100));
            return price;
        }
        else
        {
            price = train.getTicketPrice();
            return price;
        }

    }
    private double calcTotalTicketPrice(){
        totalPrice += price;
        System.out.println(totalPrice);
        return price;
    }
    private  StringBuilder generateTicket(Passenger passenger){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(passenger);
        System.out.println(stringBuilder);
        return stringBuilder;
    }
    public static int updateCounter()
    {
        String counterFileName="counter.txt";
        int counter=99;
        File counterFile=new File(counterFileName);
        if(counterFile.isFile())
        {
            try (BufferedReader reader = new BufferedReader(new FileReader(counterFileName)))
            {
                counter=Integer.parseInt(reader.readLine());
            }
            catch(IOException e)
            {
                e.printStackTrace();
                return 0;
            }
        }
        try(FileWriter writer = new FileWriter(counterFileName))
        {
            writer.write(String.valueOf(++counter));
        } catch(IOException e){
            e.printStackTrace();
            return 0;
        }
        return counter;
    }

}
