package com.travelog.attraction.controller;

import com.travelog.attraction.dto.AttractionRequestDto;
import com.travelog.attraction.service.AttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/attraction")
public class AttractionController {
    @Autowired
    AttractionService attractionService;

    @GetMapping("")
    public ResponseEntity<?> getAttractionList(@RequestBody AttractionRequestDto attractionRequestDto, @PageableDefault(size=20) Pageable pageable) throws Exception{
        return ResponseEntity.ok(attractionService.getAttractionList(attractionRequestDto, pageable)) ;
    }

    @GetMapping("/test")
    public int getTest(){
        return 1;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAttractionDetail(@PathVariable("id") String id) throws Exception{
        return ResponseEntity.ok(attractionService.getAttractionDetail(Integer.parseInt(id)));
    }
}
