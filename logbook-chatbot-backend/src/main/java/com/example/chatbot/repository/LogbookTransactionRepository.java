package com.example.chatbot.repository;

import com.example.chatbot.model.LogbookTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LogbookTransactionRepository extends MongoRepository<LogbookTransaction, String> {
    List<LogbookTransaction> findByLogbookId(int logbookId);
}