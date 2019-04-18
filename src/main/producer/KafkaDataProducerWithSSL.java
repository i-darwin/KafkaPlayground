package main.producer;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SslConfigs;

public class KafkaDataProducerWithSSL {

	public static void main(String[] args) {
		// produce to kafka, SSL port is 9095, plaintext port is 9092
		String broker = "";
		boolean useSSL = true;

		if (useSSL) {
			broker = "10.1.92.76:9095,10.1.92.77:9095,10.1.92.78:9095";
		} else {
			broker = "10.1.92.76:9092,10.1.92.77:9092,10.1.92.78:9092";
		}

		//
		String serialiazer = "org.apache.kafka.common.serialization.StringSerializer";
		String topic = "test_cli";// "test_cli";

		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serialiazer);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, serialiazer);

		if (useSSL) {
			// config for SSL Encryption (TRUSTSTORE)
			props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
			props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
					"D:\\workspaceEclipse\\KeystoreAndCert\\client.truststore.jks");
			props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "manage");

			// config for SSL Authentication protocol (keystore)
			props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG,
					"D:\\workspaceEclipse\\KeystoreAndCert\\client.keystore.jks");
			props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "manage");
			props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "manage");
		}

		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

		System.out.println("begin: " + new Timestamp(new Date().getTime()));

		for (int i = 0; i <= 10; i++) {
			try {
				String message = "{\"recordId\":null,\"orderId\":\"FESSLA-000041827\",\"module\":\"Any Report\",\"duration\":20,\"currency\":null,\"otherActivityDescription\":null}";
				ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, message);
				//producer.send(record); //send without acknowledgement
				producer.send(record).get(); //wait until acknowledgement
				Thread.sleep(1000);
				System.out.println("record: " + i + " sent");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		System.out.println("END: " + new Timestamp(new Date().getTime()));

		producer.close();
		System.out.println("Close Transaction");

	}

}
