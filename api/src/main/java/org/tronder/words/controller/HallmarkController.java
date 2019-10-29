package org.tronder.words.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tronder.words.service.HallmarkService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hallmark")
public class HallmarkController {

    private final HallmarkService hallmarkService;

    @Autowired
    public HallmarkController(HallmarkService hallmarkService) {
        this.hallmarkService = hallmarkService;
    }

    @GetMapping("")
    public List<String> getHallmarks() {
        return hallmarkService
                .getHallmarks()
                .stream()
                .map(hallmark -> hallmark.getHallmark())
                .collect(Collectors.toList());
    }

}
