package com.example.backend.service;

import com.example.backend.exception.BusinessException;
import com.example.backend.to.BeneficioTo;
import com.example.backend.to.TransferPayload;
import com.example.backend.to.TransferResultTo;
import com.example.ejb.exception.InvalidTransferException;
import com.example.ejb.model.Beneficio;
import com.example.ejb.service.BeneficioEjbService;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficioService {
    @Autowired
    BeneficioEjbService ejbService;

    public TransferResultTo transfer(TransferPayload payload) throws BusinessException {
        try{
            ejbService.transfer(payload.getFrom(), payload.getTo(), payload.getValue());
            TransferResultTo resultTo = new TransferResultTo();
            resultTo.setStatus("OK");
            resultTo.setMessagem("Transferencia realizada com sucesso");
            return resultTo;
        } catch (InvalidTransferException e) {
            throw new BusinessException(e.getCode());
        }
    }

    public List<BeneficioTo> allBenefits() {
        List<Beneficio> all = ejbService.listAllBeneficios();

        return all.stream().map(e -> new BeneficioTo(e.getId(), e.getNome(), e.getDescricao(), e.getValor(), e.getAtivo()))
                .toList();
    }

    public void novaConta(BeneficioTo to) {
        Beneficio novo = new Beneficio();
        novo.setAtivo(to.getAtivo());
        novo.setDescricao(to.getDescricao());
        novo.setNome(to.getNome());
        novo.setValor(to.getValor());

        ejbService.criarOuAtualizar(novo);
    }
}
