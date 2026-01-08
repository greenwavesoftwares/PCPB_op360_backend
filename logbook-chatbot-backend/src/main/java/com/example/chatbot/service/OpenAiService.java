package com.example.chatbot.service;

import com.example.chatbot.model.ChatRequest;
import com.google.gson.*;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OpenAiService {

    @Value("${openai.api.key}")
    private String apiKey;

    public String getAnswerWithHistory(String logSummary, List<ChatRequest.Message> messages) {
    	OkHttpClient client = new OkHttpClient.Builder()
    		    .connectTimeout(60, TimeUnit.SECONDS)   // handshake
    		    .readTimeout(120, TimeUnit.SECONDS)     // time to receive response
    		    .writeTimeout(120, TimeUnit.SECONDS)    // time to send body
    		    .build();
        Gson gson = new Gson();
        JsonArray conversation = new JsonArray();

        JsonObject system = new JsonObject();
        system.addProperty("role", "system");
        system.addProperty("content", "You are a helpful assistant that answers user questions using provided logbook summaries and conversation context.");
        conversation.add(system);

        JsonObject summary = new JsonObject();
        summary.addProperty("role", "user");
        summary.addProperty("content", "Here is the latest form data:\n" + logSummary);
        conversation.add(summary);

        for (ChatRequest.Message msg : messages) {
            JsonObject obj = new JsonObject();
            obj.addProperty("role", msg.getRole());
            obj.addProperty("content", msg.getContent());
            conversation.add(obj);
        }

        JsonObject body = new JsonObject();
        body.addProperty("model", "gpt-4o-mini");
        body.add("messages", conversation);
        body.addProperty("temperature", 0.4);
      
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(RequestBody.create(gson.toJson(body), MediaType.get("application/json")))
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String json = response.body().string();
            JsonObject parsed = JsonParser.parseString(json).getAsJsonObject();
            return parsed.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            return "⚠️ OpenAI Error: " + e.getMessage();
        }
    }
}