package com.torryharris;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
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
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController {
    private static final String DATE_FORMAT = "yyyy-MM-dd" ;
    public ArrayList<Passenger> fieldArrayList=new ArrayList<>();
    public TreeMap<Passenger,Double> passengers=new TreeMap<>();
    String pnr;
    Train train;
    String travelDateFormatted;
    double totalPrice=0.0;

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    // This Controller function is for loading the index page
    @GetMapping("/trains")
    public String index(Model model){
        TrainDAO trainDAO=new TrainDAO();
        ArrayList<Train> trainArrayList= trainDAO.fetchTrains();
        model.addAttribute("trainList",trainArrayList);
        return "trains";
    }


    // This Controller function is for loading the reservation page
    @GetMapping("/index")
    public String reservation(Model model){
        TrainDAO trainDAO=new TrainDAO();
        ArrayList<String> source=trainDAO.fetchSource();
        ArrayList<String> destination=trainDAO.fetchDestination();
        model.addAttribute("sources",source);
        model.addAttribute("destinations",destination);
        return "index";
    }


    // This Controller function is for loading ticket confirmation page and calculate ticket
    @PostMapping("bookTicket")
    public String bookTicket(HttpServletRequest request, Model model) throws ParseException {
        fieldArrayList.clear();
        passengers.clear();
        //double totalPrice=0.0;
        int trainNo=Integer.parseInt(request.getParameter("trainNo"));
        String travelDate=request.getParameter("travelDate");
        TrainDAO trainDAO=new TrainDAO();
        train=trainDAO.findTrain(trainNo);
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(travelDate);
        //String travelDateFormatted = new SimpleDateFormat("dd-MM-yyyy").format(date);
        travelDateFormatted = new SimpleDateFormat("dd-MM-yyyy").format(date);
        Ticket ticket=new Ticket(travelDateFormatted,train);
        pnr=ticket.generatePNR();
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
    @PostMapping("addTrain")
    public String addTrain(HttpServletRequest request, Model model) throws ParseException {
        ArrayList<Train> trainArrayList=new ArrayList<>();
        if (request.getParameter("noOfTrains") != null) {
            int noOfPassengers = Integer.parseInt(request.getParameter("noOfTrains"));
            for (int i = 1; i <= noOfPassengers; i++) {
                int trainNo = Integer.parseInt(request.getParameter("trainNo" + String.valueOf(i)));
                String trainName= request.getParameter("trainName" + String.valueOf(i));
                String source= request.getParameter("source" + String.valueOf(i));
                String destination= request.getParameter("destination" + String.valueOf(i));
                double price = Integer.parseInt(request.getParameter("price" + String.valueOf(i)));

                Train train=new Train(trainNo,trainName,source,destination,price);
                trainArrayList.add(train);
                System.out.println(trainArrayList);
            }
            TrainDAO trainDAO=new TrainDAO();
            trainDAO.addTrain(trainArrayList);
        }
        return "redirect:/trains";
    }
    @PostMapping("deleteTrain")
    public String deleteTrain(HttpServletRequest request, Model model) throws SQLException, ClassNotFoundException {
        String trainNo=request.getParameter("trainNo");
        System.out.println(trainNo);
        TrainDAO trainDAO=new TrainDAO();
        trainDAO.deleteTrain(trainNo);
        return "redirect:/trains";
    }
    //This controller function is for generating ticket as PDF
    @GetMapping("downloadTicket")
    public void  downloadTicket(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey= "Content-Disposition";
        String headerValue ="attachment; filename="+pnr+".pdf";
        response.setHeader(headerKey,headerValue);
        Document document=new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());
        document.open();

        PdfPTable table1=new PdfPTable(2);
        table1.setWidthPercentage(100);
        table1.getDefaultCell().setBorderColor(Color.WHITE);
        table1.addCell("PNR");
        table1.addCell(":"+pnr);
        table1.addCell("Train no");
        table1.addCell(String.valueOf(":"+train.getTrainNo()));
        table1.addCell("Train Name");
        table1.addCell(":"+train.getTrainName());
        table1.addCell("From");
        table1.addCell(":"+train.getSource());
        table1.addCell("To");
        table1.addCell(":"+train.getDestination());
        table1.addCell("Travel Date");
        table1.addCell(":"+travelDateFormatted);
        table1.setSpacingAfter(10);
        document.add(table1);

        document.add(new Paragraph("Passengers:"));

        PdfPTable table2=new PdfPTable(4);
        table2.setWidthPercentage(100);
        table2.setSpacingBefore(5);
        table2.getDefaultCell().setBorderColor(Color.BLACK);
        table2.addCell("Name");
        table2.addCell("Age");
        table2.addCell("Gender");
        table2.addCell("Fare");
        for(Passenger passengerFields: fieldArrayList)
        {
            table2.addCell(passengerFields.getName());
            table2.addCell(String.valueOf(passengerFields.getAge()));
            table2.addCell(String.valueOf(passengerFields.getGender()));
            table2.addCell(String.valueOf(passengerFields.getPrice()));
        }
        table2.setSpacingAfter(10);
        document.add(table2);
        document.add(new Paragraph("Total Price:"+totalPrice));
        document.close();
    }

    // This Controller function is used to check the availability of train
    @PostMapping("checkTrain")
    public ResponseEntity<?> checkTrain(HttpServletRequest request){
        int trainNo=Integer.parseInt(request.getParameter("trainNo"));
        System.out.println(trainNo);
        TrainDAO trainDAO=new TrainDAO();
        Train train=trainDAO.findTrain(trainNo);
        Train result=new Train();
        System.out.println(trainDAO.findTrain(trainNo));
        if(train==null) {
            result=null;
        }
        else {
            result = train;
        }
        return ResponseEntity.ok(result);
    }

    // This Controller function is used to check the availability of train
    @PostMapping("checkSourceDestination")
    public ResponseEntity<?> checkSourceDestination(HttpServletRequest request){
        String source=request.getParameter("source");
        String destination=request.getParameter("destination");
        System.out.println(source);
        TrainDAO trainDAO=new TrainDAO();
        Train train=trainDAO.findTrainBySource(source,destination);
        Train result=new Train();
        if(train==null) {
            result=null;
        }
        else {
            result = train;
        }
        return ResponseEntity.ok(result);
    }

    //This function is used check is date is valid
    @PostMapping("checkTravelDate")
    public ResponseEntity<?> checkTravelDate(HttpServletRequest request){
        String travelDate=request.getParameter("travelDate");
        System.out.println(travelDate);
        String travelDateStatus=isDateValid(travelDate);
        return ResponseEntity.ok(travelDateStatus);
    }

    //This function is used check the valid date
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
