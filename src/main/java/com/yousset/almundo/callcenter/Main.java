/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yousset.almundo.callcenter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yousset.almundo.callcenter.model.Call;
import com.yousset.almundo.callcenter.model.Employee;
import com.yousset.almundo.callcenter.model.EmployeeType;
import com.yousset.almundo.callcenter.model.People;
import com.yousset.almundo.callcenter.service.DispatcherService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author Yousset
 */
@SpringBootApplication
public class Main {

    public static void main(String[] args) throws JsonProcessingException {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        DispatcherService service = context.getBean(DispatcherService.class);
        service.addEmployee(new Employee("Operador 1", EmployeeType.OPERADOR));
        service.addEmployee(new Employee("Operador 2", EmployeeType.OPERADOR));
        service.addEmployee(new Employee("Operador 3", EmployeeType.OPERADOR));
        service.addEmployee(new Employee("Operador 4", EmployeeType.OPERADOR));
        service.addEmployee(new Employee("Operador 5", EmployeeType.OPERADOR));
        service.addEmployee(new Employee("Operador 6", EmployeeType.OPERADOR));
        service.addEmployee(new Employee("Supervisor 1", EmployeeType.SUPERVISOR));
        service.addEmployee(new Employee("Supervisor 2", EmployeeType.SUPERVISOR));
        service.addEmployee(new Employee("Supervisor 3", EmployeeType.SUPERVISOR));
        service.addEmployee(new Employee("Supervisor 4", EmployeeType.SUPERVISOR));
        service.addEmployee(new Employee("Director 1", EmployeeType.DIRECTOR));
        service.addEmployee(new Employee("Director 2", EmployeeType.DIRECTOR));

        for (int i = 0; i < 20; i++) {
            service.dispatchCall(new Call(new People("Persona " + (i + 1)), "Llamada " + (i + 1)));
        }
    }
}
