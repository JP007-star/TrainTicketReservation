package com.torryharris;

import com.torryharris.model.Ticket;
import com.torryharris.model.Train;
import com.torryharris.model.TrainDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class MainController {

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
    public String bookTicket(
            @RequestParam(value = "trainNo", required = false) int trainNo,
            @RequestParam(value = "travelDate", required = false) String travelDate,
            Model model) throws ParseException {
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
       //https://codereview.stackexchange.com/questions/145900/spring-mvc-dynamically-adding-form-elements
    }

    @GetMapping("ticket")
    public String ticket(){
        return "ticket";
    }
}
