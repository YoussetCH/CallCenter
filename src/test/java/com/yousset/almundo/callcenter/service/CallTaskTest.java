/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yousset.almundo.callcenter.service;

import com.yousset.almundo.callcenter.model.Call;
import com.yousset.almundo.callcenter.model.Employee;
import com.yousset.almundo.callcenter.model.EmployeeType;
import com.yousset.almundo.callcenter.model.StatusType;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Yousset
 */
public class CallTaskTest {

    private CallTask callTask;
    private ConcurrentMap<EmployeeType, ConcurrentLinkedQueue<Employee>> employees;
    
    @Before
    public void initialize(){
        employees = new ConcurrentHashMap<>();
        employees.put(EmployeeType.OPERADOR, new ConcurrentLinkedQueue<>());
        employees.put(EmployeeType.DIRECTOR, new ConcurrentLinkedQueue<>());
        employees.put(EmployeeType.SUPERVISOR, new ConcurrentLinkedQueue<>());
    }
    
    @Test
    public void whenGetAvailableEmployeeAndAllAvailablesThenShouldBeReturnOperator(){
        addEmployee(new Employee("Director 1", EmployeeType.DIRECTOR));
        addEmployee(new Employee("Supervisor 1", EmployeeType.SUPERVISOR));
        addEmployee(new Employee("Operador 5", EmployeeType.OPERADOR));
        
        callTask = new CallTask(employees, new Call());
        
        Assert.assertEquals(callTask.getNextAvailableEmployee().getType(), EmployeeType.OPERADOR);
    }
    
    @Test
    public void whenGetAvailableEmployeeAndOperatorsIsNotAvailabeThenShouldBeReturnNextLevel(){
        addEmployee(new Employee("Director 1", EmployeeType.DIRECTOR));
        addEmployee(new Employee("Supervisor 1", EmployeeType.SUPERVISOR));
        addEmployee(new Employee("Operador 5", StatusType.IN_CALL, EmployeeType.OPERADOR));
        
        callTask = new CallTask(employees, new Call());
        Assert.assertEquals(callTask.getNextAvailableEmployee().getType(), EmployeeType.SUPERVISOR);
    }
    
    @Test
    public void whenGetAvailableEmployeeAndAllOperatorsIsNotAvailabeThenShouldBeReturnNull(){
        addEmployee(new Employee("Director 1", StatusType.IN_CALL, EmployeeType.DIRECTOR));
        addEmployee(new Employee("Supervisor 1", StatusType.IN_CALL, EmployeeType.SUPERVISOR));
        addEmployee(new Employee("Operador 5", StatusType.IN_CALL, EmployeeType.OPERADOR));
        
        callTask = new CallTask(employees, new Call());
        
        Assert.assertNull(callTask.getNextAvailableEmployee());
    }
 
    public void addEmployee(Employee employee) {
        employees.get(employee.getType()).add(employee);
    }
}
