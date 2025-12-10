package com.example.ejb.service;

import com.example.ejb.exception.InvalidTransferException;
import com.example.ejb.model.Beneficio;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Stateless
public class BeneficioEjbService {
    private static final String ERROR_CODE_INPUT_DATA = "error.transfer.input";
    private static final String ERROR_CODE_NEGATIVE_AMOUNT = "error.negative.amount";
    private static final String ERROR_CODE_INVALID_DESTINATION = "error.invalid.destination";

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void criarOuAtualizar(Beneficio e) {
        em.persist(e);
    }

    public List<Beneficio> listAllBeneficios(){
        TypedQuery<Beneficio> query = em.createQuery("select e from Beneficio e", Beneficio.class);

        return query.getResultList();
    }

    @Transactional()
    public void transfer(Long fromId, Long toId, BigDecimal amount) throws InvalidTransferException {
        if (fromId == null || toId == null || amount == null) {
            throw new InvalidTransferException(ERROR_CODE_INPUT_DATA);
        }

        if (amount.compareTo(BigDecimal.ZERO)<= 0) {
            throw new InvalidTransferException(ERROR_CODE_NEGATIVE_AMOUNT);
        }

        if (fromId.equals(toId)) {
            throw new InvalidTransferException(ERROR_CODE_INVALID_DESTINATION);
        }

        Beneficio from = em.find(Beneficio.class, fromId, LockModeType.OPTIMISTIC);
        Beneficio to   = em.find(Beneficio.class, toId, LockModeType.OPTIMISTIC);

        // BUG: sem validações, sem locking, pode gerar saldo negativo e lost update
        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        em.merge(from);
        em.merge(to);
    }
}
