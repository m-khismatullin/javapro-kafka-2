package ru.km.kafka.conference.config;

//import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
//import org.springframework.kafka.core.KafkaAdmin;
//import org.springframework.util.StringUtils;

//import java.util.HashMap;
//import java.util.Map;

@Configuration
public class Config {
    //    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootstrapAddress;
    @Value("${topic}")
    private String topic;

//    @Bean
//    public KafkaAdmin admin() {
//        Map<String, Object> configs = new HashMap<>();
//        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
//        return new KafkaAdmin(configs);
//    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder
                .name(topic)
                .build();
    }
}
