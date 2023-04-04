package com.mindex.challenge.data;

public class ReportingStructure {

    // I usually like to use lombok or similar for POJO annotations for setter, getter, no and all args constructors, hashcode overrides, etc

    private Employee employee;

    private int numberOfReports;

    public ReportingStructure() {

    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }

    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }

}
