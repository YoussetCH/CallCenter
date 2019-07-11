/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yousset.almundo.callcenter.model;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author Yousset
 */
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Call {

    public static final int MIN_DURATION = 5;
    public static final int MAX_DURATION = 10;
    private String id;
    private final int duration;
    private People people;
    private String description;
    private StatusCallType status;
    private final Date registerCall;
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    private Date startCall;
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    private Date endCall;
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    private Employee operator;

    public Call() {
        id = String.valueOf(System.nanoTime() + new Random().nextInt(100));
        duration = new Random().nextInt(MAX_DURATION - MIN_DURATION + 1) + MIN_DURATION;
        status = StatusCallType.WAITING;
        registerCall = new Date();
    }

    public Call(People people, String description) {
        this();
        this.people = people;
        this.description = description;
    }

    public Date getRegisterCall() {
        return registerCall;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }

    public int getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusCallType getStatus() {
        return status;
    }

    public void setStatus(StatusCallType status) {
        this.status = status;
    }

    public Date getStartCall() {
        return startCall;
    }

    public void setStartCall(Date startCall) {
        this.startCall = startCall;
    }

    public Date getEndCall() {
        return endCall;
    }

    public void setEndCall(Date endCall) {
        this.endCall = endCall;
    }

    public Employee getOperator() {
        return operator;
    }

    public void setOperator(Employee operator) {
        this.operator = operator;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Call) {
            return id != null && id.equals(((Call) obj).id);
        }
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.id);
        return hash;
    }

}
