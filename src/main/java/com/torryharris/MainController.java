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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class MainController {
    public ArrayList<Passenger> fieldArrayList=new ArrayList<>();
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
        int trainNo=Integer.parseInt(hsr.getParameter("trainNo"));
        String travelDate=hsr.getParameter("travelDate");
        if (hsr.getParameter("noOfPassengers") != null) {
            String noOfPassengers = hsr.getParameter("noOfPassengers");
            List<String> d = Arrays.asList(noOfPassengers);
            int count = d.size() + 1;
            for (int i = 1; i < count+1; i++) {
                Passenger passenger = new Passenger();
                String name = hsr.getParameter("name" + String.valueOf(i));
                String ageString = hsr.getParameter("age" + String.valueOf(i));
                int age=Integer.parseInt(ageString);
                String genderString = hsr.getParameter("gender" + String.valueOf(i));
                char gender=genderString.charAt(0);
                passenger.setName(name);
                passenger.setAge(age);
                passenger.setGender(gender);
                fieldArrayList.add(passenger);
            }
        }
        System.out.println(fieldArrayList);
        TrainDAO trainDAO=new TrainDAO();
        Train train=trainDAO.findTrain(trainNo);
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(travelDate);
        String travelDateFormatted = new SimpleDateFormat("dd-MM-yyyy").format(date);
        Ticket ticket=new Ticket(travelDateFormatted,train);
        String pnr=ticket.generatePNR();
        model.addAttribute("trainNo", trainNo);
        model.addAttribute("train", train);
        model.addAttribute("travelDate", travelDateFormatted);
        model.addAttribute("pnr", pnr);
       return "ticket";
    }

    @GetMapping("ticket")
    public String ticket(){
        return "ticket";
    }
}
