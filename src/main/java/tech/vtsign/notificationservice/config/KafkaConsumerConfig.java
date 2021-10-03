package tech.vtsign.notificationservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import tech.vtsign.notificationservice.model.User;

import java.util.HashMap;
import java.util.Map;

//@Configuration
//public class KafkaConsumerConfig {
//    @Bean
//    public RecordMessageConverter converter() {
//        return new StringJsonMessageConverter();
//    }
//    @Bean
//    public Map<String, Object> consumerConfigs() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "vtsign.tech:9092");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
//        return props;
//    }
//
//    @Bean
//    public ConsumerFactory<String, User> consumerFactory() {
//        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
//        //, new StringDeserializer(),
//        //                new JsonDeserializer<>(User.class)
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, User> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, User> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//
//        return factory;
//    }
//
//    @Bean
//    public Receiver receiver() {
//        return new Receiver();
//    }
//}
//@Configuration
//public class KafkaConsumerConfig {
//    @Bean
//    public Map<String, Object> consumerConfigs() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "vtsign.tech:9092");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        props.put(JsonDeserializer.TRUSTED_PACKAGES, "tech.vtsign.notificationservice.model.*");
//        return props;
//    }
//
//
//    @Bean
//    public ConsumerFactory<String, User> consumerFactory() {
//
////        var deserializer = new JsonDeserializer<User>();
////        deserializer.addTrustedPackages("tech.vtsign.notificationservice.model.*");
//        return new DefaultKafkaConsumerFactory<>(
//                consumerConfigs(),
//                new StringDeserializer(),
//                new JsonDeserializer<User>());
//    }
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, User> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, User> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
//
//}

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, User> consumerFactory(){
        JsonDeserializer<User> deserializer = new JsonDeserializer<>(User.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> configs = new HashMap<>();

        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "vtsign.tech:9092");
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, User> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, User> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;

    }
}