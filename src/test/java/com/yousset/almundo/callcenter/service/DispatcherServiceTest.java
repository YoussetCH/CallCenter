/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yousset.almundo.callcenter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yousset.almundo.callcenter.model.Call;
import com.yousset.almundo.callcenter.model.Employee;
import com.yousset.almundo.callcenter.model.EmployeeType;
import com.yousset.almundo.callcenter.model.People;
import com.yousset.almundo.callcenter.model.StatusCallType;
import com.yousset.almundo.callcenter.model.StatusType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 * @author Yousset
 */
public class DispatcherServiceTest {

    private DispatcherService service;

    @Before
    public void initialize() {
        service = new DispatcherService();
    }

    @Test
    public void whenAddCallAlreadyRegisterTheShouldBeReturnFalse() throws JsonProcessingException {
        Call call = new Call(new People("Prueba"), "Prueba");
        call.setId("213");
        service.calls.put(call.getId(), call);
        Assert.assertFalse(service.dispatchCall(call));
    }

    @Test
    public void whenAddCallAndNotRegisterTheShouldBeReturnTrue() throws JsonProcessingException {
        Call call = new Call(new People("Prueba"), "Prueba");
        service.setTaskExecutor(threadPoolTaskExecutor());
        Assert.assertTrue(service.dispatchCall(call));
    }

    @Test
    public void whenProcess3CallsConcurrent() throws JsonProcessingException, InterruptedException {
        service.setTaskExecutor(threadPoolTaskExecutor());
        startEmployees();
        addCalls(3);
        //Espeara el tiempo de maxima duracion de la llamada
        Thread.sleep(Call.MAX_DURATION * 1000L + 500L);
        Assert.assertEquals(3L, service.calls.values().parallelStream()
                .filter((Call c) -> c.getStatus() == StatusCallType.ENDED).count());
    }

    @Test
    public void whenProcessMultiCallsConcurrentAndAddEmployees() throws JsonProcessingException, InterruptedException {
        service.setTaskExecutor(threadPoolTaskExecutor());
        startEmployees();
        addCalls(30);
        startEmployees();
        int total = (service.getTotalEmployees() / ThreadConfig.MAX_NUMBER_SIM_CALLS) + 1;
        //Esperar el tiempo necesario para comprobar que todo corrio, cantidad de empleados / maximos hilos + 1
        Thread.sleep(total * Call.MAX_DURATION * 1000L + 500L);
        Assert.assertEquals(service.calls.size(), service.calls.values().parallelStream()
                .filter((Call c) -> c.getStatus() == StatusCallType.ENDED).count());
    }

    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(ThreadConfig.MAX_NUMBER_SIM_CALLS);
        executor.setMaxPoolSize(ThreadConfig.MAX_NUMBER_SIM_CALLS * 2);
        executor.setThreadNamePrefix("default_task_executor_thread");
        executor.initialize();

        return executor;
    }

    public void startEmployees() {
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
    }

    public void addCalls(int size) throws JsonProcessingException {
        for (int i = 0; i < size; i++) {
            service.dispatchCall(new Call(new People("Persona " + (i + 1)), "Llamada " + (i + 1)));
        }
    }
}
