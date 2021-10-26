package com.torryharris;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MyController {
    @GetMapping("index")
    public String index(){
        return "index";
    }
    @GetMapping("reservation")
    public String reservation(){
        return "reservation";
    }
}
