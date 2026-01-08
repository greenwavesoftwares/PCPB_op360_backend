package com.example.chatbot.repository;

import com.example.chatbot.model.LogbookMetaInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogbookMetaInfoRepository extends MongoRepository<LogbookMetaInfo, String> {
    LogbookMetaInfo findTopByLogbookIdOrderByVersionNumberDesc(int logbookId);
}