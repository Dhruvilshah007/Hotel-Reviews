package com.lcwd.hotel.controllers;


import com.lcwd.hotel.entities.Hotel;
import com.lcwd.hotel.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {
    @Autowired
    private HotelService hotelService;
    //create
    // Hotel creation can be done by only user with ADMIN role

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel){
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.create(hotel));
    }

    //get hotel info by id
    //No one will be able to R/Q data directly, as it can be only called via Microservices internally as per scope

    @PreAuthorize("hasAuthority('SCOPE_INTERNAL')")
    @GetMapping("{hotelId}")
    public ResponseEntity<Hotel> get(@PathVariable String hotelId){
        return ResponseEntity.ok(hotelService.get(hotelId));

    }


    //all hotel get
    //No one will be able to R/Q data directly, as it can be only called via Microservices internally as per scope and User with Admin rights

    @PreAuthorize("hasAuthority('SCOPE_INTERNAL') || hasAuthority('Admin')")
    @GetMapping
    public ResponseEntity<List<Hotel>> getAll(){
        return ResponseEntity.ok(hotelService.getAll());

    }
}
