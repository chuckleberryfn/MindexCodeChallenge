package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class DirectReportsCounter {

    // choose to pull this logic out for modularity
    // these methods can be static, no instance state required, etc

    // solution 3. iterative depth first search - uses stack
    public static int calculateDirectReportsDFS(Employee employee) {
        int countOfReports = 0;
        Stack<Employee> directReportsStack = new Stack<>();
        directReportsStack.push(employee);
        while(!directReportsStack.isEmpty()) {
            Employee currEmployee = directReportsStack.pop();
            countOfReports++;
            if(currEmployee.getDirectReports() != null && currEmployee.getDirectReports().size() != 0) {

                // I did the backwards iteration here so I traverse down the left side of the tree first, since Stack used
                for(int i = currEmployee.getDirectReports().size() - 1; i >= 0; i--) {
                    directReportsStack.push(currEmployee.getDirectReports().get(i));
                }
            }
        }
        // substract the root employee from the count, they do not report to themselves
        // could have handle this in a number of different ways, e.g. only put directReports of root employee in queue to start
        // didn't put too much thought into it since just assessment
        return countOfReports - 1;
    }

    // solution 2. iterative breadth first search - uses queue
    public static int calculateDirectReportsBFS(Employee employee) {
        int countOfReports = 0;
        Queue<Employee> directReportsQueue = new LinkedList<>();
        directReportsQueue.add(employee);
        while(!directReportsQueue.isEmpty()) {
            Employee currEmployee = directReportsQueue.poll();
            countOfReports++;
            if(currEmployee.getDirectReports() != null && currEmployee.getDirectReports().size() != 0) {
                for(Employee directReport : currEmployee.getDirectReports()) {
                    directReportsQueue.add(directReport);
                }
            }
        }
        // substract the root employee from the count, they do not report to themselves
        // could have handle this in a number of different ways, e.g. only put directReports of root employee in queue to start
        // didn't put too much thought into it since just assessment
        return countOfReports - 1;
    }

    // solution 1. recursive, this works fine but technically a little slower because increases size of callstack immensely.
    // addmitedly, may not matter for most companies so this solution could be fine, but if you wanted to process all of say
    // Walmart which had 2.3 Million employees, you may want slightly faster solution.
    // Or caching mechanism of this endpoint could be discussed, ehcache or custom solution or similar
    public static int calculateDirectReportsRecursive (Employee employee) {
        return calculateDirectReportsRecursive(employee, true);
    }

    private static int calculateDirectReportsRecursive (Employee employee, boolean isRoot) {
        // terminal case
        if(employee.getDirectReports() == null || employee.getDirectReports().size() == 0) {
            return 1;
        }
        int countOfReports = isRoot ? 0 : 1;
        for(Employee directReport : employee.getDirectReports()) {
            countOfReports += DirectReportsCounter.calculateDirectReportsRecursive(directReport, false);
        }
        return countOfReports;
    }

}
