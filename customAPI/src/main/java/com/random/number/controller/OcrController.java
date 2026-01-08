package com.random.number.controller;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import okhttp3.RequestBody;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class OcrController {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/ocr")
    public ResponseEntity<?> extractHandwriting(@RequestParam("file") MultipartFile file) {
        try {
            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Construct JSON payload (note the "url" object)
            String json = """
            {
              "model": "gpt-4o-mini",
              "messages": [
                {
                  "role": "user",
                  "content": [
                    {
                      "type": "text",
                      "text": "You are an OCR system. Carefully read this handwriting image and extract all the text accurately and if there an image then guess what picture it is and provide short answer only and care of the grammar and meaningful sentences or words. Return only the plain text."
                    },
                    {
                      "type": "image_url",
                      "image_url": {
                        "url": "data:image/jpeg;base64,%s"
                      }
                    }
                  ]
                }
              ]
            }
            """.formatted(base64Image);

            RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/chat/completions")
                    .addHeader("Authorization", "Bearer " + openAiApiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return ResponseEntity
                            .status(response.code())
                            .body(response.body() != null ? response.body().string() : "Error: No response body");
                }

                String responseBody = response.body().string();
                JsonNode root = mapper.readTree(responseBody);
                String text = root.path("choices").get(0).path("message").path("content").asText();

                return ResponseEntity.ok()
                        .body(mapper.createObjectNode().put("text", text));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
