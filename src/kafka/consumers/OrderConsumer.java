package kafka.consumers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.productor.KafkaProperties;
import kafka.utils.IteratorTemplate;

public class OrderConsumer extends Thread {
	private final ConsumerConnector consumer;
	private final String topic;

	private static Queue<String> queue = new ConcurrentLinkedQueue<String>() ;
	
	public OrderConsumer(String topic) {
		consumer = kafka.consumer.Consumer
				.createJavaConsumerConnector(createConsumerConfig());
		this.topic = topic;
	}

	private static ConsumerConfig createConsumerConfig() {
		Properties props = new Properties();
		props.put("zookeeper.connect", KafkaProperties.zkConnect);
		props.put("group.id", KafkaProperties.groupId+"1234");//每个topo设置唯一groupid
		props.put("zookeeper.session.timeout.ms", "4000");
		props.put("zookeeper.sync.time.ms", "2000");
		props.put("auto.commit.interval.ms", "10000");//
		props.put("auto.offset.reset","smallest");//

		return new ConsumerConfig(props);

	}
	String aaString = null;
	public void run() {
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(1));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
		KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		int n=0;
		
	while (it.hasNext()){
			n++;
			//逻辑处理
			String msg = new String(it.next().message()) ;
			System.out.println("consumer:"+msg+"   n:"+n);
			queue.add(msg) ;
			
//			aaString = new String(it.next().message()) ;
		}
			
	}

	public Queue<String> getQueue()
	{
		return queue ;
	}
	public String getString()
	{
		return aaString ;
	}
	public static void main(String[] args) {
		OrderConsumer consumerThread = new OrderConsumer(KafkaProperties.topic);
		consumerThread.start();
	}
}
