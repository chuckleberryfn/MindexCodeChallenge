package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure readReportingStructure(String id) {
        LOG.debug("Calculating employee reporting structure for employee with id [{}]", id);

        // I would handle error handling a bit differently but this is fine for the challenge/given the existing read method I have here
        // main concern is read could throw exception, which is why I kept it out of the try block - can discuss on call how I typically do exception handling
        Employee employee = read(id);

        ReportingStructure reportingStructure = new ReportingStructure();
        try {
            // three different solutions for review

            int numberOfReports = DirectReportsCounter.calculateDirectReportsDFS(employee);
            //int numberOfReports = DirectReportsCounter.calculateDirectReportsBFS(employee);
            //int numberOfReports = DirectReportsCounter.calculateDirectReportsRecursive(employee);

            reportingStructure.setEmployee(employee);
            reportingStructure.setNumberOfReports(numberOfReports);
        } catch (Exception e) {
            LOG.error("Error counting direct reports of employee", e);
            throw new RuntimeException("Error counting direct reports of employee");
        }

        return reportingStructure;
    }


    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }
}