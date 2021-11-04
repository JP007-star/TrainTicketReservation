package com.torryharris;

import com.torryharris.model.Passenger;
import com.torryharris.model.Ticket;
import com.torryharris.model.Train;
import com.torryharris.model.TrainDAO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController {
    private static final String DATE_FORMAT = "yyyy-MM-dd" ;
    public ArrayList<Passenger> fieldArrayList=new ArrayList<>();
    public TreeMap<Passenger,Double> passengers=new TreeMap<>();

    @GetMapping("index")
    public String index(Model model){
        TrainDAO trainDAO=new TrainDAO();
        ArrayList<Train> trainArrayList= trainDAO.fetchTrains();
        model.addAttribute("trainList",trainArrayList);
        return "index";
    }

    @GetMapping("reservation")
    public String reservation(){
        return "reservation";
    }

    @PostMapping("bookTicket")
    public String bookTicket(HttpServletRequest request, Model model) throws ParseException {
        fieldArrayList.clear();
        double totalPrice=0.0;
        int trainNo=Integer.parseInt(request.getParameter("trainNo"));
        String travelDate=request.getParameter("travelDate");
        TrainDAO trainDAO=new TrainDAO();
        Train train=trainDAO.findTrain(trainNo);
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(travelDate);
        String travelDateFormatted = new SimpleDateFormat("dd-MM-yyyy").format(date);
        Ticket ticket=new Ticket(travelDateFormatted,train);
        String pnr=ticket.generatePNR();
        if (request.getParameter("noOfPassengers") != null) {
            int noOfPassengers = Integer.parseInt(request.getParameter("noOfPassengers"));
            for (int i = 1; i <= noOfPassengers; i++) {
                String name = request.getParameter("name" + String.valueOf(i));
                String ageString = request.getParameter("age" + String.valueOf(i));
                int age=Integer.parseInt(ageString);
                String genderString = request.getParameter("gender" + String.valueOf(i));
                char gender=genderString.charAt(0);
                Passenger passenger = new Passenger(name,age,gender);
                double price=ticket.calcPassengerFare(passenger);
                Passenger finalPassenger = new Passenger(name,age,gender,price);
                fieldArrayList.add(finalPassenger);
            }
        }
        System.out.println(fieldArrayList);
        for(Passenger passengerFields: fieldArrayList) {
            double price=ticket.calcPassengerFare(passengerFields);
            passengers.put(passengerFields,price);
        }
        totalPrice= ticket.calcTotalTicketPrice(passengers);
        model.addAttribute("trainNo", trainNo);
        model.addAttribute("train", train);
        model.addAttribute("travelDate", travelDateFormatted);
        model.addAttribute("passengers", fieldArrayList);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("pnr", pnr);
       return "ticket";
    }

    @PostMapping("downloadTicket")
    public void  downloadTicket(){
        Ticket ticket=new Ticket();
        ticket.addPassenger();
        ticket.writeTicket();
    } 
    @PostMapping("checkTrain")
    public ResponseEntity<?> checkTrain(HttpServletRequest request){
        int trainNo=Integer.parseInt(request.getParameter("trainNo"));
        System.out.println(trainNo);
        TrainDAO trainDAO=new TrainDAO();
        Train train=trainDAO.findTrain(trainNo);
        String result="";
        System.out.println(trainDAO.findTrain(trainNo));
        if(train==null) {
            result="The Train is not available";
        }
        else {
            result = "success";
        }
        return ResponseEntity.ok(result);
    }
    @PostMapping("checkTravelDate")
    public ResponseEntity<?> checkTravelDate(HttpServletRequest request){
        String travelDate=request.getParameter("travelDate");
        System.out.println(travelDate);
        String travelDateStatus=isDateValid(travelDate);
        return ResponseEntity.ok(travelDateStatus);
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
                return "success";
            }
            else
                return "TravelDate is before current date!...";
        } catch (ParseException e) {
            return "...";
        }
    }
}
