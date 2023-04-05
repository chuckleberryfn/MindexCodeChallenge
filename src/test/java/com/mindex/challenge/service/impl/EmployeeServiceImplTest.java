package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;

    private String reportingStructureUrl;

    private static final String JOHN_LENNON_EMPLOYEE_ID = "16a596ae-edd3-4847-99fe-c4518e82c86f";

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportingStructureUrl = "http://localhost:" + port + "/employee/{id}/reporting-structure";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    @Test
    public void testReportingStructureExistingRecord() {
        // check new reporting structure
        ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, JOHN_LENNON_EMPLOYEE_ID).getBody();

        assertNotNull(reportingStructure);
        assertEquals(2, reportingStructure.getNumberOfReports());
    }

    @Test
    public void testReportingStructureLargerStructure() {
        Employee childEmployee = new Employee();
        childEmployee.setFirstName("Jane");
        childEmployee.setLastName("Doe ");
        childEmployee.setDepartment("Engineering");
        childEmployee.setPosition("Developer");

        // Create child employee
        Employee createdChildEmployee = restTemplate.postForEntity(employeeUrl, childEmployee, Employee.class).getBody();

        // fetch existing employee, which has two direct reports
        Employee existingJohnLennonEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, JOHN_LENNON_EMPLOYEE_ID).getBody();

        Employee rootEmployee = new Employee();
        rootEmployee.setFirstName("Chuck");
        rootEmployee.setLastName("Harris");
        rootEmployee.setDepartment("Engineering");
        rootEmployee.setPosition("Developer");
        rootEmployee.setDirectReports(new ArrayList<>());

        // add child and existing John Lennon employees to new root employee
        rootEmployee.getDirectReports().add(createdChildEmployee);
        rootEmployee.getDirectReports().add(existingJohnLennonEmployee);

        // Create root employee
        Employee createdRootEmployee = restTemplate.postForEntity(employeeUrl, rootEmployee, Employee.class).getBody();

        // check new reporting structure
        ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, createdRootEmployee.getEmployeeId()).getBody();

        assertNotNull(reportingStructure);
        assertEquals(4, reportingStructure.getNumberOfReports());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
