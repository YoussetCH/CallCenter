/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yousset.almundo.callcenter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yousset.almundo.callcenter.model.Call;
import com.yousset.almundo.callcenter.model.Employee;
import com.yousset.almundo.callcenter.model.EmployeeType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yousset
 */
@Service
public class DispatcherService {

    private static final Logger LOGGER = LogManager.getLogger(DispatcherService.class);

    private final ConcurrentMap<EmployeeType, ConcurrentLinkedQueue<Employee>> employees;
    protected final ConcurrentMap<String, Call> calls;
    @Autowired
    private TaskExecutor taskExecutor;

    public DispatcherService() {
        employees = new ConcurrentHashMap<>();
        employees.put(EmployeeType.OPERADOR, new ConcurrentLinkedQueue<>());
        employees.put(EmployeeType.DIRECTOR, new ConcurrentLinkedQueue<>());
        employees.put(EmployeeType.SUPERVISOR, new ConcurrentLinkedQueue<>());
        calls = new ConcurrentHashMap<>();
    }

    /**
     * Metodo que recibe la llamada y la agrega a la cola
     *
     * @param call
     * @return 
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public boolean dispatchCall(Call call) throws JsonProcessingException {
        //Valida que la llamada no se encuentre ya registrada
        if (calls.containsKey(call.getId())) {
            System.out.println("Llamada id: " + call.getId() + " Registrada");
            return false;
        } else {
            //Registra la llamada
            calls.put(call.getId(), call);
            LOGGER.info("Entrando llamada id: " + new ObjectMapper().writeValueAsString(call));
            taskExecutor.execute(new CallTask(employees, call));
        }
        return true;
    }

    /**
     * Agrega un empleado a la lista de empleados disponibles
     *
     * @param employee
     */
    public void addEmployee(Employee employee) {
        employees.get(employee.getType()).add(employee);
    }

    /**
     * Obtiene los datos de una llamada
     *
     * @param id
     * @return
     */
    public Call getCall(String id) {
        return calls.get(id);
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
    
    public int getTotalEmployees() {
        int result = 0;
        result = employees.entrySet().stream().map((entry) -> entry.getValue().size()).reduce(result, Integer::sum);
        return result;
    }
}
