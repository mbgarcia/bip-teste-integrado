package com.example.backend.controller;

import com.example.backend.exception.BusinessException;
import com.example.backend.service.BeneficioService;
import com.example.backend.to.BeneficioTo;
import com.example.backend.to.TransferPayload;
import com.example.backend.to.TransferResultTo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/beneficios")
public class BeneficioController {
    @Autowired
    BeneficioService service;

    @GetMapping("/")
    public List<BeneficioTo> list() {
        return service.allBenefits();
    }

    @PostMapping("/transfer/")
    @ResponseStatus(HttpStatus.OK)
    public TransferResultTo transfer(@Valid @RequestBody TransferPayload payload) throws BusinessException {
        return service.transfer(payload);
    }

    @PostMapping("/novo/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void novo(@Valid @RequestBody BeneficioTo novo) {
        service.novaConta(novo);
    }
}
