package com.example.chatbot.controller;

import com.example.chatbot.model.ChatRequest;
import com.example.chatbot.model.LogbookMetaInfo;
import com.example.chatbot.model.LogbookTransaction;
import com.example.chatbot.repository.LogbookMetaInfoRepository;
import com.example.chatbot.repository.LogbookTransactionRepository;
import com.example.chatbot.service.ChatbotService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

	@Autowired
	private LogbookMetaInfoRepository metaRepo;
	
	@Autowired
	private LogbookTransactionRepository logRepo;
	
    @Autowired
    private ChatbotService chatbotService;

    @PostMapping
    public String chat(@RequestBody ChatRequest chatRequest) {
    	System.out.println("chatRequest : "+chatRequest.toString());
        return chatbotService.chatWithContext(chatRequest.getLogbookId(), chatRequest.getMessages());
    }
    
    @PostMapping("/logbook/data")
	public ResponseEntity<?> saveLogbookData(@RequestBody LogbookTransaction data){
		LogbookTransaction savedData = logRepo.save(data);
		return new ResponseEntity<>(savedData,HttpStatus.OK);
	}
    @PostMapping("/metainfo")
	public ResponseEntity<?> saveLogbookMetaInfo(@RequestBody LogbookMetaInfo metadata){
		LogbookMetaInfo savedData = metaRepo.save(metadata);
		System.out.println("");
		return new ResponseEntity<>(savedData,HttpStatus.OK);
	}
}