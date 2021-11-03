package com.torryharris.model;


import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class Ticket {
    int counter;
    String pnr;
    String travelDate;
    Train train;
    TreeMap<Passenger,Double> passengers = new TreeMap<>();
    ArrayList<Passenger> passengerArrayList = new ArrayList<>();
    double price;

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



    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Ticket(String travelDate,Train train) {
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



    public String generatePNR(){
        //Ticket ticket=new Ticket(travelDate,train);
        char source=train.getSource().charAt(0);
        char destination=train.getDestination().charAt(0);
        String travelDate=getTravelDate();
        String[] dt=getTravelDate().split("-",3);
        String pnrstring = source+""+destination+"_"+travelDate+"_"+ updateCounter();

        setPnr(pnrstring);
        System.out.println(pnr);
        return pnrstring;
    }
    public void addPassenger(String name,int age,char gender){
        Passenger passenger = new Passenger(name, age, gender);
        passengerArrayList.add(passenger);
        calcPassengerFare(passenger);
        passengers.put(passenger,price);
    }


    private double calcPassengerFare(Passenger passenger){

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
        double totalPrice = 0.0;
        for (double p : passengers.values()) {
            totalPrice += p;
        }
        return totalPrice;
    }
    private StringBuilder generateTicket() {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getPnr() + ","
                    + train.getTrainNo() + ","
                    + train.getTrainName() + ","
                    + train.getSource() + ","
                    + train.getDestination() + ","
                    + travelDate + ","
                    + calcTotalTicketPrice());

            return stringBuilder;
        }
        public void writeTicket() {

            File file = new File(generatePNR() + ".txt");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (FileWriter fileWriter = new FileWriter(file, true);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

                StringBuilder stringBuilder = generateTicket();


                bufferedWriter.newLine();
                bufferedWriter.newLine();
                bufferedWriter.write("PNR: " + stringBuilder.toString().split(",")[0]);
                bufferedWriter.newLine();
                bufferedWriter.write("Train no: " + stringBuilder.toString().split(",")[1]);
                bufferedWriter.newLine();
                bufferedWriter.write("Train name: " + stringBuilder.toString().split(",")[2]);
                bufferedWriter.newLine();
                bufferedWriter.write("From: " + stringBuilder.toString().split(",")[3]);
                bufferedWriter.newLine();
                bufferedWriter.write("To: " + stringBuilder.toString().split(",")[4]);
                bufferedWriter.newLine();
                bufferedWriter.write("Travel Date: " + stringBuilder.toString().split(",")[5]);
                bufferedWriter.newLine();

                bufferedWriter.write("Passengers: ");
                bufferedWriter.newLine();

                bufferedWriter.write("Name    Age    Gender    Fare");
                bufferedWriter.newLine();
                System.out.println(passengers);
                System.out.println(passengerArrayList);

                for (Passenger p : passengers.keySet()) {

                    bufferedWriter.write(p.getName() + "      ");
                    bufferedWriter.write(String.valueOf(p.getAge()) + "       ");
                    bufferedWriter.write(p.getGender() + "        ");
                    bufferedWriter.write(String.valueOf(passengers.get(p)) + "        ");
                    bufferedWriter.newLine();
                }

                bufferedWriter.write("Total Price:  " + calcTotalTicketPrice());

            } catch (IOException e) {
                e.printStackTrace();
            }


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
