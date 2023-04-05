package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String employeeIdUrl;
    private String compensationUrl;
    private String compensationIdUrl;

    private static final String JOHN_LENNON_EMPLOYEE_ID = "16a596ae-edd3-4847-99fe-c4518e82c86f";

    @Autowired
    private CompensationService compensationService;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        compensationUrl = "http://localhost:" + port + "/employee/{id}/compensation";
        compensationIdUrl = "http://localhost:" + port + "/employee/{id}/compensation";
    }

    @Test
    public void testCreateRead() {

        // fetch existing employee
        Employee johnLennonEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, JOHN_LENNON_EMPLOYEE_ID).getBody();

        Compensation testCompensation = new Compensation();
        testCompensation.setEmployee(johnLennonEmployee);
        testCompensation.setSalary(BigDecimal.valueOf(150000));
        testCompensation.setEffectiveDate(LocalDate.parse("2023-04-17"));

        // Create checks
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class, johnLennonEmployee.getEmployeeId()).getBody();

        assertNotNull(createdCompensation);
        assertNotNull(createdCompensation.getEmployeeId());
        assertCompensationEquivalence(testCompensation, createdCompensation);


        // Read checks
        Compensation johnLennonCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, JOHN_LENNON_EMPLOYEE_ID).getBody();

        assertNotNull(createdCompensation);
        assertNotNull(createdCompensation.getEmployeeId());
        assertCompensationEquivalence(testCompensation, createdCompensation);
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEmployeeEquivalence(expected.getEmployee(), actual.getEmployee());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
