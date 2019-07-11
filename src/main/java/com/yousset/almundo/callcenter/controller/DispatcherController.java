/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yousset.almundo.callcenter.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yousset.almundo.callcenter.model.Call;
import com.yousset.almundo.callcenter.service.DispatcherService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Yousset
 */
@Controller
public class DispatcherController {

    @Autowired
    private DispatcherService service;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity welcome() {
        return ResponseEntity.ok("Welcome to Almundo Call Center!!!");
    }

    @RequestMapping(value = "/call/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity checkCall(@PathVariable String id) {
        Call call = service.getCall(id);
        if (call == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(call);
    }

    @RequestMapping(value = "/call", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity call(@RequestBody String payloadBody) {
        try {
            Call call = new ObjectMapper().readValue(payloadBody, new TypeReference<Call>() {
            });
            service.dispatchCall(call);
            return ResponseEntity.ok(call);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
