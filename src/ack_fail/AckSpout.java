package ack_fail;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class AckSpout implements IRichSpout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	FileInputStream fis;
	InputStreamReader isr;
	BufferedReader br;			
	private Random _rand;
    private ConcurrentHashMap<UUID, Values> _pending;//存储emit过的tuple
    private ConcurrentHashMap<UUID, Integer> fail_pending;//存储失败的tuple和其失败次数
	SpoutOutputCollector collector = null;
	
	String str = null;

	@Override
	public void nextTuple() {
		try {
			while ((str = this.br.readLine()) != null) {
				// 过滤动作
				String arr[] = str.split("\t") ;
				UUID msgId = UUID.randomUUID();
				// www.taobao.com	VVVYH6Y4V4SFXZ56JIPDPB4V678	2014-01-07 08:40:51 2
				Values val = new Values(arr[0],arr[1],arr[2],arr[3]);
		        this._pending.put(msgId, val);
		        collector.emit(val, msgId);
		        System.err.println("_pending.size()="+_pending.size());
		        
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub
		try {
			br.close();
			isr.close();
			fis.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	@Override
	//初始化函数
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		try {
			this.collector = collector;
			this.fis = new FileInputStream("track.log");
			this.isr = new InputStreamReader(fis, "UTF-8");
			this.br = new BufferedReader(isr);
			
			_rand = new Random();
	        _pending = new ConcurrentHashMap<UUID, Values>();
	        fail_pending = new ConcurrentHashMap<UUID, Integer>();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("url","session_id","date","province_id"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void ack(Object msgId) {
		// TODO Auto-generated method stub
		System.err.println("spout ack:"+msgId.toString());
		this._pending.remove(msgId);
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
		Integer fail_count = fail_pending.get(msgId);
		if (fail_count == null) {
			fail_count = 0;
		}
		fail_count ++ ;
		if (fail_count>=3) {
			//重试次数已满，不再进行重新emit
			fail_pending.remove(msgId);
		}else {
			//记录该tuple失败次数
			fail_pending.put((UUID)msgId, fail_count);
			//重发
			this.collector.emit(this._pending.get(msgId), msgId);
		}
	}

}
