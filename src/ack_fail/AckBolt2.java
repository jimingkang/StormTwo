package ack_fail;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class AckBolt2 implements IRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	OutputCollector collector = null;
	TopologyContext context = null;
	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}
	int num = 0;
	String url = null;
	String session_id = null;
	String date = null;
	int province_id = 0;
	@Override
	public void execute(Tuple input) {
		// TODO Auto-generated method stub
//		input.getValueByField("log");
//		input.getValue(0);
//		System.err.println(input.getMessageId());
//		input.getMessageId() 获取 messageid
		try {
			date = input.getStringByField("date") ;
			province_id = Integer.parseInt(input.getStringByField("province_id")) ;
			
			if(session_id != null && session_id.length()>5)
			{
				System.err.println(Thread.currentThread().getId()+"   lines  :"+num +"   session_id:"+session_id);
				collector.ack(input);
			}
//			collector.emit(input, new Values());
//     		Thread.sleep(300);
		} catch (Exception e) {
			System.err.println("error data:"+input.getStringByField("province_id"));
			collector.fail(input);
			e.printStackTrace();
		}
		
	}
//初始化，对应spout的open函数
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		// TODO Auto-generated method 
		this.context = context ;
		this.collector = collector ;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

		declarer.declare(new Fields("")) ;
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
