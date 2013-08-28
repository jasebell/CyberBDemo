package uk.co.dataissexy.bullydemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class MessageQueue {
	private static final String RPC_QUEUE_NAME = "my_message_queue";
	
	public static void main(String[] argv) {
		MessageClassifier mc = new MessageClassifier();
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(RPC_QUEUE_NAME, true, consumer);
			
			while (true) {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				System.out.println("[x] Received '" + message + "'");
				
				double commentRating = mc.rateBullyLevel(message);
				if(commentRating > 25 && commentRating < 50) {
					System.out.println("Store message as flagged.");
				} else if(commentRating > 50 && commentRating < 75) {
					System.out.println("Forward to community admin for logging.");
				} else if(commentRating > 75 && commentRating < 90) {
					System.out.println("Forward to community admin for logging and warn sender.");
				} else if(commentRating > 90) {
					System.out.println("Harmful levels of language, suspend account");
				} else {
					System.out.println("Comment okay");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Problem with feed queue.");
		} finally {
			
		}
	}
}
