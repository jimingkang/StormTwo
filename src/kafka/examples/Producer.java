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

import java.util.Properties;
import java.util.Random;

import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.productor.KafkaProperties;

public class Producer extends Thread {
	private final kafka.javaapi.producer.Producer<Integer, String> producer;
	private final String topic;
	private final Properties props = new Properties();

	public Producer(String topic) {
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("metadata.broker.list","192.168.1.116:9092");
		// Use random partitioner. Don't need the key type. Just set it to
		// Integer.
		// The message is of type String.
		producer = new kafka.javaapi.producer.Producer<Integer, String>(
				new ProducerConfig(props));
		this.topic = topic;
	}

	public void run() {
//		for (int i = 0; i < 2000; i++) {
//			String messageStr = new String("Message_" + i);
//			System.out.println("product:"+messageStr);
//			producer.send(new KeyedMessage<Integer, String>(topic, messageStr));
//		}
		
		Random random = new Random();
		String[] hosts = { "www.taobao.com" };
		String[] session_id = { "ABYH6Y4V4SCVXTG6DPB4VH9U123", "XXYH6YCGFJYERTT834R52FDXV9U34", "BBYH61456FGHHJ7JL89RG5VV9UYU7",
				"CYYH6Y2345GHI899OFG4V9U567", "VVVYH6Y4V4SFXZ56JIPDPB4V678" };
		String[] time = { "2017-01-07 08:40:50", "2017-01-07 08:40:51", "2017-01-07 08:40:52", "2017-01-07 08:40:53", 
				"2017-01-07 09:40:49", "2017-01-07 10:40:49", "2017-01-07 11:40:49", "2017-01-07 12:40:49" };
		String[] province_id = { "1","2","3","4","5","6" };
		
		
		for (int i = 0; i < 500; i++) {			
			producer.send(new KeyedMessage<Integer, String>(topic, hosts[0]+"\t"+session_id[random.nextInt(5)]+"\t"+time[random.nextInt(8)]+"\t"+province_id[random.nextInt(5)]));
		   
			System.out.println("produce msg i="+i);
		}

	}

	public static void main(String[] args) {
		Producer producerThread = new Producer("log");
		producerThread.start();
	}
}
