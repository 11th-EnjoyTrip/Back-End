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

    private final AttractionService attractionService;

    public AttractionController(AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAttractionList(@ModelAttribute AttractionRequestDto attractionRequestDto, @PageableDefault(size=20) Pageable pageable) {
        try {
            System.out.println(attractionRequestDto.getRegion());
            System.out.println(attractionRequestDto.getCategory());
            System.out.println(attractionRequestDto.getKeyword());
            System.out.println(pageable);
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
