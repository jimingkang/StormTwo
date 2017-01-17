package com.ibf.base.spout;

import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class SourceSpout implements IRichSpout{

	/**
	 * 数据源Spout
	 */
	private static final long serialVersionUID = 1L;

	Queue<String> queue = new ConcurrentLinkedQueue<String>();
	
	SpoutOutputCollector collector = null;
	
	@Override
	public void nextTuple() {
		if (queue.size() >= 0) {
			String str = queue.poll();//从队列里消费数据
			collector.emit(new Values(str));
//			String arr[] = str.split("\t") ;
//			arr[2] = arr[2].substring(0, 10);
//			collector.emit(new Values(arr[0],arr[1],arr[2],arr[3]));
		}
		try {
			Thread.sleep(300) ;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		try {
			this.collector = collector;
			
			Random random = new Random();
			String[] hosts = { "www.taobao.com" };
			String[] session_id = { "ABYH6Y4V4SCVXTG6DPB4VH9U123", "XXYH6YCGFJYERTT834R52FDXV9U34", "BBYH61456FGHHJ7JL89RG5VV9UYU7",
					"CYYH6Y2345GHI899OFG4V9U567", "VVVYH6Y4V4SFXZ56JIPDPB4V678" };
			String[] time = { "2014-01-07 08:40:50", "2014-01-07 08:40:51", "2014-01-07 08:40:52", "2014-01-07 08:40:53", 
					"2014-01-07 09:40:49", "2014-01-07 10:40:49", "2014-01-07 11:40:49", "2014-01-07 12:40:49" };
			String[] province_id = { "1","2","3","4","5","6" };
			
			
			for (int i = 0; i < 10000; i++) {
				//url            sessionid        time         province_id
				queue.add(hosts[0]+"\t"+session_id[random.nextInt(5)]+"\t"+time[random.nextInt(8)]+"\t"+province_id[random.nextInt(5)]);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
//		declarer.declare(new Fields("log"));
		declarer.declare(new Fields("log"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void ack(Object msgId) {
		// TODO Auto-generated method stub
		System.out.println("spout ack:"+msgId.toString());
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fail(Object msgId) {
		// TODO Auto-generated method stub
		System.out.println("spout fail:"+msgId.toString());
	}

}
