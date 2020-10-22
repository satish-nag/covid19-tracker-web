package com.covid19tracker.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tracker")
public class TrackerController {

    @PostMapping("/trackStatus")
    public void trackStatus(){

    }

}
