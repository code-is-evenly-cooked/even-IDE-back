package com.evenly.evenide.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Bean
    public ChatClient chatClient() {

        OpenAiApi openAiApi = OpenAiApi.builder()
                .apiKey(apiKey)
                .build();

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model("gpt-4o-mini")
                .build();

        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(options)
                .build();

        return ChatClient.builder(chatModel)
                .defaultSystem("넌 친근하고 재밌는 튜터야. 항상 친구에게 설명하듯 말해줘. " +
                        "쓸모 없는 말은 삼가고 대답은 최대한 간결하고 짧게 부탁할게.")
                .build();
    }
}
