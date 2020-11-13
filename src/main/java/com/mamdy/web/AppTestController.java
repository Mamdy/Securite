package com.mamdy.web;

import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class AppTestController {

    @GetMapping("/test")
    @ResponseBody
    public String isUp(){
        return "isUp";
    }
}
