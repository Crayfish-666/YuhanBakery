package com.cyh.yuhanbakery.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/customer/ai")
public class AiChatController {

    @Value("${volcengine.ark.api-key}")
    private String apiKey;

    @Value("${volcengine.ark.model-id}")
    private String modelId;

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/chat")
    public SseEmitter chat(@RequestBody Map<String, String> requestBody) {
        String userMessage = requestBody.get("message");
        // 120s 超时时间
        SseEmitter emitter = new SseEmitter(120000L);

        executor.execute(() -> {
            try {
                // 构建 JSON Payload，包含系统角色设定
                String payload = """
                {
                    "model": "%s",
                    "stream": true,
                    "tools": [
                        {
                            "type": "web_search",
                            "max_keyword": 3
                        }
                    ],
                    "input": [
                        {
                            "role": "system",
                            "content": [
                                {
                                    "type": "input_text", 
                                    "text": "你是一个名为‘宇晗烘焙’的智能糕点助手。请用温柔、专业的语气回答顾客的疑问，你可以主动介绍本店的热卖产品（如各类软欧包、定制生日蛋糕等）。如果不知道的问题请礼貌婉拒。你的回答要排版美观，尽量简短清晰。"
                                }
                            ]
                        },
                        {
                            "role": "user",
                            "content": [
                                {
                                    "type": "input_text", 
                                    "text": "%s"
                                }
                            ]
                        }
                    ]
                }
                """.formatted(modelId, userMessage.replace("\"", "\\\"").replace("\n", "\\n"));

                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create("https://ark.cn-beijing.volces.com/api/v3/responses"))
                        .header("Authorization", "Bearer " + apiKey)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(payload))
                        .build();

                HttpClient httpClient = HttpClient.newHttpClient();
                HttpResponse<InputStream> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("data: ")) {
                            String data = line.substring(6);
                            if ("[DONE]".equals(data.trim())) {
                                break;
                            }
                            try {
                                JsonNode root = objectMapper.readTree(data);
                                String type = root.path("type").asText();
                                
                                // 处理 Volcengine v3/responses 特定格式 (例如 DeepSeek 模型返回)
                                // data: {"type":"response.output_text.delta","delta":"你好"}
                                if ("response.output_text.delta".equals(type)) {
                                    String delta = root.path("delta").asText();
                                    if (delta != null && !delta.isEmpty()) {
                                        emitter.send(delta);
                                    }
                                } else {
                                    // 兼容标准的 OpenAI 格式（以防部分模型使用此格式）
                                    JsonNode choices = root.path("choices");
                                    if (choices.isArray() && !choices.isEmpty()) {
                                        JsonNode contentNode = choices.get(0).path("delta").path("content");
                                        if (contentNode.isMissingNode() || contentNode.isNull()) {
                                            contentNode = choices.get(0).path("message").path("content");
                                        }
                                        if (!contentNode.isMissingNode() && !contentNode.isNull()) {
                                            String contentText = "";
                                            if (contentNode.isArray() && !contentNode.isEmpty()) {
                                                JsonNode typeNode = contentNode.get(0).path("type");
                                                if (typeNode != null && ("input_text".equals(typeNode.asText()) || "text".equals(typeNode.asText()))) {
                                                    contentText = contentNode.get(0).path("text").asText();
                                                }
                                            } else {
                                                contentText = contentNode.asText();
                                            }
                                            
                                            if (contentText != null && !contentText.isEmpty()) {
                                                emitter.send(contentText);
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                log.error("解析火山引擎 SSE 响应部分数据失败: {}", line, e);
                            }
                        }
                    }
                }
                emitter.complete();
            } catch (Exception e) {
                log.error("调用大模型 API 异常", e);
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}
