package com.sbm.modules.consent.controller;

import com.sbm.modules.consent.dto.SegmentDto;
import com.sbm.modules.consent.service.segment.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/segment")
public class SegmentController {

    @Autowired
    private SegmentService segmentService;

    @GetMapping("/all")
    public ResponseEntity<List<SegmentDto>> getAllSegments() {

        List<SegmentDto> segments = segmentService.getAllSegments();
        return new ResponseEntity<>(segments, HttpStatus.OK);
    }
}
