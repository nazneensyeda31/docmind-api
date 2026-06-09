package com.docmind.docmind_api.service;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClaudeAiService {
    @Value("${anthropic.api.key}")
    private String apiKey;
    private final RestClient restClient = RestClient.create();

    @Retry(name="claudeApi", fallbackMethod = "claudeFallback")
    @CircuitBreaker(name = "claudeApi", fallbackMethod = "claudeFallback")
    public String callClaude(String prompt){
        Map<String, Object> map = new HashMap<>();
        Map<String, String> innerMap = new HashMap<>();
        innerMap.put("role", "user");
        innerMap.put("content", prompt);
        map.put("model", "claude-sonnet-4-5");
        map.put("max_tokens", 1024);
        map.put("messages", List.of(innerMap));
        Map response = restClient.post()
                .uri("https://api.anthropic.com/v1/messages")
                .header("x-api-key", apiKey)
                .header("anthropic-version", "2023-06-01")
                .header("Content-Type", "application/json")
                .body(map)
                .retrieve()
                .body(Map.class);

        List content = (List) response.get("content");
        Map first = (Map) content.getFirst();
        return (String) first.get("text");

    }
    public String summarizeDocument(String extractedText){
        return callClaude("You are a document assistant. " +
                "Based on the following document content, \n" +
                "provide a brief summary in 3-5 sentences.\n\n" +
                "Document content:\n"+ extractedText);

    }
    private String claudeFallback(String prompt, Exception e){
        return "AI service is temporarily unavailable. Please try again later.";

    }

    public String answerQuestion(String extractedText, String question){
        return callClaude("You are a document assistant." +
                "Answer the following question based ONLY on the provided document content."
                + "If the answer is not in the document,say 'I cannot find this information in the document.'\n\n" +
                "Document content: "+ extractedText +

                "Question: "+ question);
    }
}
