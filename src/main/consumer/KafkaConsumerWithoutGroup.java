package main.consumer;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConsumerWithoutGroup {

	public static void main(String[] args) {
		String bootstrapServer = "127.0.0.1:9092";

		// construct kafka connection properties
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		Consumer<String, String> consumer = new KafkaConsumer<String, String>(props);

		// #A subscription approach, let kafka manage your data polling
		// consumer.subscribe(Collections.singleton("test_cli"));
		// #A END

		// #B assign approach, manage your own data polling
		TopicPartition topicPartition = new TopicPartition("test_cli", 0);
		List<TopicPartition> assignmentList = new ArrayList<TopicPartition>();
		assignmentList.add(topicPartition);
		consumer.assign(assignmentList);
		// #B END

		// #BA below approach is to get the latest message (a.k.a last offset) only,
		// remark if want message
		// from beginning
		consumer.seekToEnd(assignmentList);
		long currentOffset = consumer.position(topicPartition);
		consumer.seek(topicPartition, currentOffset - 1);
		// #BA END

		System.out.println("BEGIN: " + new Timestamp(new Date().getTime()));
		while (true) {
			// wait 20s if no data
			ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(20));

			// if no data after 5 min, break out the loop to close consumer
			if (consumerRecords.count() == 0) {
				break;
			}

			// java 1.8 lambda expression, means to do below function for each records on
			// consumer record. Operates as var "record"
			consumerRecords.forEach(record -> {
				System.out.printf("Consumer Record:(%d, %s, %d, %d)\n", record.key(), record.value(),
						record.partition(), record.offset());
			});

		}

		System.out.println("close consumer: " + new Timestamp(new Date().getTime()));
		consumer.close();
	}

}
