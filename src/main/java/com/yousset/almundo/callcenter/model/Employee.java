/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yousset.almundo.callcenter.model;

/**
 *
 * @author Yousset
 */
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Employee extends People {

    private StatusType status;
    private EmployeeType type;

    public Employee() {
        this(null, StatusType.AVAILABLE, EmployeeType.OPERADOR);
    }

    public Employee(String name, EmployeeType type) {
        this(name, StatusType.AVAILABLE, type);
    }

    public Employee(String name, StatusType status, EmployeeType type) {
        super.setName(name);
        this.status = status;
        this.type = type;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public EmployeeType getType() {
        return type;
    }

    public void setType(EmployeeType type) {
        this.type = type;
    }
}
