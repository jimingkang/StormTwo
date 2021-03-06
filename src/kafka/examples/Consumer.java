/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kafka.examples;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import backtype.storm.tuple.Values;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.productor.KafkaProperties;

public class Consumer extends Thread {
	private final ConsumerConnector consumer;
	private final String topic;

	public Consumer(String topic) {
		consumer = kafka.consumer.Consumer
				.createJavaConsumerConnector(createConsumerConfig());
		this.topic = topic;
	}

	private static ConsumerConfig createConsumerConfig() {
		Properties props = new Properties();
		props.put("zookeeper.connect", KafkaProperties.zkConnect);
		props.put("group.id", KafkaProperties.groupId);
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "10000");// offset 娑堣垂鐨勬爣璇嗕綅
//	props.put("auto.offset.reset","smallest");// --from-beginning


		return new ConsumerConfig(props);

	}
// push娑堣垂鏂瑰紡锛屾湇鍔＄鎺ㄩ�杩囨潵銆備富鍔ㄦ柟寮忔槸pull
	public void run() {
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(1));
//		topicCountMap.put(topic1, new Integer(2));
		
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer
				.createMessageStreams(topicCountMap);
		KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		int n=0;
		while (it.hasNext()){
			//閫昏緫澶勭悊
			n++;
			String msg = new String(it.next().message()) ;
			System.out.println("consumer:"+msg+"  "+n);
//			collector.emit(new Values(msg));
		}
			
	}

	public static void main(String[] args) {
		Consumer consumerThread = new Consumer("log");
		consumerThread.start();
	}
}
