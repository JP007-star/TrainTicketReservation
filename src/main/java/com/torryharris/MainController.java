package com.torryharris;

import com.torryharris.model.Passenger;
import com.torryharris.model.Ticket;
import com.torryharris.model.Train;
import com.torryharris.model.TrainDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController {
    public ArrayList<Passenger> fieldArrayList=new ArrayList<>();
    public ArrayList<Double> priceArray=new ArrayList<>();
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
    public String bookTicket(HttpServletRequest hsr, Model model) throws ParseException {
        fieldArrayList.clear();
        int trainNo=Integer.parseInt(hsr.getParameter("trainNo"));
        String travelDate=hsr.getParameter("travelDate");
        TrainDAO trainDAO=new TrainDAO();
        Train train=trainDAO.findTrain(trainNo);
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(travelDate);
        String travelDateFormatted = new SimpleDateFormat("dd-MM-yyyy").format(date);
        Ticket ticket=new Ticket(travelDateFormatted,train);
        String pnr=ticket.generatePNR();
        if (hsr.getParameter("noOfPassengers") != null) {
            int noOfPassengers = Integer.parseInt(hsr.getParameter("noOfPassengers"));
            for (int i = 1; i <= noOfPassengers; i++) {
                String name = hsr.getParameter("name" + String.valueOf(i));
                String ageString = hsr.getParameter("age" + String.valueOf(i));
                int age=Integer.parseInt(ageString);
                String genderString = hsr.getParameter("gender" + String.valueOf(i));
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
            priceArray.add(price);
        }
        double totalPrice= calcTotalTicketPrice();
        model.addAttribute("trainNo", trainNo);
        model.addAttribute("train", train);
        model.addAttribute("travelDate", travelDateFormatted);
        model.addAttribute("passengers", fieldArrayList);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("pnr", pnr);
       return "ticket";
    }

    @GetMapping("ticket")
    public String ticket(){
        return "ticket";
    }
    public double calcTotalTicketPrice(){
        double totalPrice = 0.0;
        for (double p : passengers.values()) {
            totalPrice += p;
        }
        return totalPrice;
    }
}
