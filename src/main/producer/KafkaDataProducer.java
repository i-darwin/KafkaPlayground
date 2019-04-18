package main.producer;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaDataProducer {

	public static void main(String[] args) {
		String broker = "localhost:9092,localhost:9093,localhost:9094";
		String serialiazer = "org.apache.kafka.common.serialization.StringSerializer";
		String topic = "test_cluster";

		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serialiazer);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, serialiazer);

		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

		System.out.println("begin: " + new Timestamp(new Date().getTime()));

		for (int i = 0; i <= 100; i++) {
			String message = "DEF number: " + i + " has been sent to kafka";
			ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, message);
			try {
				producer.send(record).get();
				Thread.sleep(1000);
				System.out.println(message);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} 
		}

		System.out.println("END: " + new Timestamp(new Date().getTime()));

		producer.close();
		System.out.println("Close Transaction");
	}

}
