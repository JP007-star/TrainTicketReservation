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
    double price;
    double finalChildFare=0;
    double finalAgedFare=0;
    double finalFemaleFare=0;
    double finalMaleFare=0;

    public Ticket(Passenger passenger, Train train) {
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
        System.out.println(passengerArrayList);
        ticket.calcPassengerFare(passenger,train);
    }
    private double calcPassengerFare(Passenger passenger,Train train){

        if(passenger.getAge()<=12)
        {
            double childFare=train.getTicketPrice()/2;
            System.out.println(childFare);
            finalChildFare+=childFare;
        }
        else if(passenger.getAge()>=60)
        {
            double agedFare=train.getTicketPrice()*(60/100);
            finalAgedFare+=agedFare;
        }
        else if(passenger.getGender()=='F')
        {
            double femaleFare= train.getTicketPrice()-(train.getTicketPrice()*(25/100));
            finalFemaleFare+=femaleFare;
        }
        else
        {
            double maleFare = train.getTicketPrice();
            finalMaleFare+=maleFare;
        }
        double totalTicketPrice=finalAgedFare+finalMaleFare+finalChildFare+finalFemaleFare;
        System.out.println(totalTicketPrice);
        return (totalTicketPrice);
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
