package main.consumer;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaDataConsumer {

	public static void main(String[] args) {
		String brokerServers = "localhost:9092,localhost:9093,localhost:9094";
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerServers);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer_instance_java");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

		System.out.println("begin consumer: " + new Timestamp(new Date().getTime()));
		consumer.subscribe(Collections.singleton("test_cli"));

		while (true) {
			ConsumerRecords<String, String> dataFromKafka = consumer.poll(Duration.ofMinutes(5));

			if (dataFromKafka.count() == 0) {
				break;
			}

			dataFromKafka.forEach(data -> {
				System.out.println("Data from kafka: " + data.value());
			});
		}

		System.out.println("close consumer: " + new Timestamp(new Date().getTime()));
		consumer.close();
	}

}
