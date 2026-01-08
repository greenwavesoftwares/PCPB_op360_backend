package com.example.chatbot.service;

import com.example.chatbot.model.ChatRequest;
import com.example.chatbot.model.LogbookMetaInfo;
import com.example.chatbot.model.LogbookTransaction;
import com.example.chatbot.model.LogbookField;
import com.example.chatbot.repository.LogbookMetaInfoRepository;
import com.example.chatbot.repository.LogbookTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatbotService {

    @Autowired
    private LogbookMetaInfoRepository metaRepo;

    @Autowired
    private LogbookTransactionRepository txnRepo;

    @Autowired
    private OpenAiService openAiService;

    public String chatWithContext(Integer logbookId, List<ChatRequest.Message> messages) {
        LogbookMetaInfo meta = metaRepo.findTopByLogbookIdOrderByVersionNumberDesc(logbookId);
        List<LogbookTransaction> txns = txnRepo.findByLogbookId(logbookId);

        if (meta == null || txns.isEmpty()) {
            return "⚠️ No data found for this logbook.";
        }

        String summary = buildSummary(meta, txns);

        return openAiService.getAnswerWithHistory(summary, messages);
    }

    private String buildSummary(LogbookMetaInfo meta, List<LogbookTransaction> txns) {
        StringBuilder sb = new StringBuilder("Form Summary:\n");

        Map<String, String> labelMap = meta.getData();

        for (LogbookTransaction txn : txns) {
            sb.append("📅 ").append(txn.getTransactionTimestamp()).append("\n");
            Map<String, LogbookField> data = txn.getLogbookData();
            for (Map.Entry<String, String> entry : labelMap.entrySet()) {
                String key = entry.getKey();
                String label = entry.getValue();
                LogbookField field = data.get(key);
                if (field != null && field.getValue() != null && !field.getValue().isBlank()) {
                    sb.append("• ").append(label).append(": ").append(field.getValue()).append("\n");
                }
            }
            sb.append("\n");
        }
        sb.append("\n give precise answer only, explenations not required ."); 

        System.out.println("My String : "+sb.toString());
        return sb.toString();
    }
}