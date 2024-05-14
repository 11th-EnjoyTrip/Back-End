package com.travelog.attraction.controller;

import com.travelog.attraction.dto.AttractionRequestDto;
import com.travelog.attraction.service.AttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/attraction")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AttractionController {
    @Autowired
    AttractionService attractionService;

    @GetMapping("")
    public ResponseEntity<?> getAttractionList(@RequestBody AttractionRequestDto attractionRequestDto, @PageableDefault(size=20) Pageable pageable) {
        try {
            return ResponseEntity.ok(attractionService.getAttractionList(attractionRequestDto, pageable).getContent()) ;
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAttractionDetail(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(attractionService.getAttractionDetail(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER ERROR");
        }
    }
}
