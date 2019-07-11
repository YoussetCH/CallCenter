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
import com.yousset.almundo.callcenter.model.StatusCallType;
import com.yousset.almundo.callcenter.model.StatusType;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Yousset
 */
public class CallTask implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(CallTask.class);

    private static final EmployeeType[] ORDERS_EMPLOYEES = {EmployeeType.OPERADOR, EmployeeType.SUPERVISOR, EmployeeType.DIRECTOR};
    public static final long WAIT_TIME = 1000L;
    public static final int RETRIES = 60;
    private final ConcurrentMap<EmployeeType, ConcurrentLinkedQueue<Employee>> employees;
    private final Call call;
    private int count;

    public CallTask(ConcurrentMap<EmployeeType, ConcurrentLinkedQueue<Employee>> employees, Call call) {
        this.employees = employees;
        this.call = call;
        this.count = 0;
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    /**
     * Corre el hilo de la llamada
     */
    public void run() {
        try {
            //Maxima cantidad de reintentos por esperar un empleado disponible
            while (count < RETRIES) {
                count++;
                //Obteniendo empleado para la llamda
                Employee employee = getNextAvailableEmployee();
                if (employee != null) {
                    //Atendiendo la llamda
                    employee.setStatus(StatusType.IN_CALL);
                    LOGGER.info("Tomando llamada id: " + call.getId()
                            + " atendida por: [" + employee.getType() + "] " + employee.getName());
                    call.setOperator(employee);
                    call.setStartCall(new Date());
                    call.setStatus(StatusCallType.CALLING);
                    //Tiempo de duracion de la llamada
                    Thread.sleep(1000 * call.getDuration());

                    call.setEndCall(new Date());
                    call.setStatus(StatusCallType.ENDED);

                    //Coloca al empleado como disponible
                    employee.setStatus(StatusType.AVAILABLE);
                    LOGGER.info("Finalizando llamada: " + new ObjectMapper().writeValueAsString(call));
                    return;
                } else {
                    //Tiempo de espera para realizar otro intendo
                    LOGGER.info("Llamada id: " + call.getId() + " en espera");
                    Thread.sleep(WAIT_TIME);
                }
            }
            LOGGER.info("Llamada id: " + call.getId() + ", no atendida");
        } catch (InterruptedException e) {
            LOGGER.error("Error en la llamada ID: " + call.getId(), e);
        } catch (JsonProcessingException ex) {
            LOGGER.error("Error write: " + call.getId(), ex);

        } finally {
            call.setStatus(StatusCallType.ENDED);
        }
    }

    /**
     * Obtiene el proximo empleado disponible para atender segun prioridad de
     * cargo
     *
     * @return
     */
    protected Employee getNextAvailableEmployee() {
        for (EmployeeType order : ORDERS_EMPLOYEES) {
            Employee employee = getNextAvailableEmployee(order);
            if (employee != null) {
                return employee;
            }
        }
        return null;
    }

    /**
     * Obtiene el proximo empleado disponible para atender segun el tipo de
     * empleado
     *
     * @param employeeType
     * @return
     */
    private Employee getNextAvailableEmployee(EmployeeType employeeType) {
        if (!employees.get(employeeType).isEmpty()) {
            return employees.get(employeeType).parallelStream()
                    .filter((Employee t) -> t.getStatus() == StatusType.AVAILABLE).findAny().orElse(null);
        }
        return null;
    }

}
