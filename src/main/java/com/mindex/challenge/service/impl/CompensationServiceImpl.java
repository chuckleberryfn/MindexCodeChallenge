package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Compensation createCompensation(String id, Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        compensation.setEmployeeId(id);
        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public Compensation readCompensation(String id) {
        LOG.debug("Reading compensation with id [{}]", id);

        Compensation compensation = compensationRepository.findCompensationByEmployeeId(id);

        if (compensation == null) {
            throw new RuntimeException(String.format("Invalid employeeId: %s - Compensation record does not exist.", id));
        }

        return compensation;
    }

}
