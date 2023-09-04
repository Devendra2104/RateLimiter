package com.devo_bhai.rateLim.controller;

import com.devo_bhai.rateLim.dto.AreaV1;
import com.devo_bhai.rateLim.dto.RectangleDimension;
import com.devo_bhai.rateLim.dto.TriangleDimension;
import com.devo_bhai.rateLim.service.RateLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/v1/area", consumes = MediaType.APPLICATION_JSON_VALUE)
public class AreaCalController {

    @Autowired
    private RateLimitService rateLimitService;

    @PostMapping(value = "/rectangle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AreaV1> rectangle(@RequestBody RectangleDimension dimensions) throws Exception {
//        if(rateLimitService.allowRequest())
            return ResponseEntity.ok(new AreaV1("rectangle", dimensions.getLength() * dimensions.getWidth()));
//        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new AreaV1("Not good sir", 100.00));
    }

    @PostMapping(value = "/triangle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AreaV1> triangle(@RequestBody TriangleDimension dimensions) {

        return ResponseEntity.ok(new AreaV1("triangle", 0.5d * dimensions.getHeight() * dimensions.getBase()));
    }
}
