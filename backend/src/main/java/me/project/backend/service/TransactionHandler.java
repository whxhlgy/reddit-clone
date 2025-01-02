package me.project.backend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class TransactionHandler {
    @Transactional(value = Transactional.TxType.REQUIRED)
    public <T> T runInTransaction(Supplier<T> supplier) {
        return supplier.get();
    }
}
