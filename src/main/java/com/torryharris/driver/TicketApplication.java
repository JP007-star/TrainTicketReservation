package com.torryharris.driver;

import com.torryharris.model.Passenger;
import com.torryharris.model.Ticket;
import com.torryharris.model.Train;
import com.torryharris.model.TrainDAO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
//This Console Based Application
public class TicketApplication {
    final static String DATE_FORMAT = "dd-MM-yyyy";
    public static void main(String[] args) {
        TrainDAO trainDAO=new TrainDAO();
        ArrayList<Passenger> passengerArrayList=new ArrayList<>();
        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter the Train Number:");
        int trainNo= Integer.parseInt(scanner.nextLine());
        Train train=trainDAO.findTrain(trainNo);
        //System.out.println(trainDAO.fetchTrains());
        if(trainDAO.findTrain(trainNo)!=null) {
            System.out.println("Enter the Travel Date:");
            String travelDate = scanner.nextLine();
            Ticket ticket=new Ticket(travelDate,train);
            isDateValid(travelDate);
            System.out.println(isDateValid(travelDate));
           // System.out.println(ticket.generatePNR());
            if(isDateValid(travelDate)!="TravelDate is before current date!...") {
                ticket.addPassenger();
                ticket.writeTicket();
            }

        }
        else{
            System.out.println("Train Not Found! ... Retry with correct Train No");
        }
    }


    public static String isDateValid(String date)
    {
        try {
            DateFormat travelDate = new SimpleDateFormat(DATE_FORMAT);
            travelDate.setLenient(false);
            travelDate.parse(date);
            DateFormat currentDate = new SimpleDateFormat(DATE_FORMAT);
            Date todayDate = new Date();
            if(travelDate.parse(date).after(todayDate)){
                  return "";
            }
            else
            return "TravelDate is before current date!...";
        } catch (ParseException e) {
            return "TravelDate is before current date!...";
        }
    }
}
