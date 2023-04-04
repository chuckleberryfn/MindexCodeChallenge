package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompensationController {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;

    // Task 2. Compensation - POST HTTP method chosen to create new compensation record
    // same pathing convention as used in task 1 chosen, with specific employee as root and noun of compensation as path
    // FYI I did debate keeping employee id in the path, but I thought it was best, can discuss
    @PostMapping("/employee/{id}/compensation")
    public Compensation createCompensation(@PathVariable String id, @RequestBody Compensation compensation) {
        LOG.debug("Received create employee compensation request for [{}]", id);

        return compensationService.createCompensation(id, compensation);
    }

    // Task 2. Compensation - GET HTTP method chosen to fetch employee compensation record
    // same pathing convention as used in task 1 chosen, with specific employee as root and noun of compensation as path
    @GetMapping("/employee/{id}/compensation")
    public Compensation createCompensation(@PathVariable String id) {
        LOG.debug("Received read employee compensation request for [{}]", id);

        return compensationService.readCompensation(id);
    }

}
