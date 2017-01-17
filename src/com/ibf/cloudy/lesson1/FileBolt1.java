package com.ibf.cloudy.lesson1;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class FileBolt1 implements IRichBolt{

	OutputCollector Collector = null;
	@Override
	public void cleanup() {
		// 销毁函数
		
	}

	int lines = 0;
	@Override
	public void execute(Tuple tuple) {
		// 执行计算，死循环
		try {
			String TName = Thread.currentThread().getName() ;
//			String log = tuple.getStringByField("log");
			String guid = tuple.getString(0);
			String url = tuple.getString(1);
			
			lines ++ ;
			System.out.println(TName+"----"+lines+" ---bolt1:log="+guid);
			this.Collector.ack(tuple);  //显性声明
			
		} catch (Exception e) {
			// TODO: handle exception
			this.Collector.fail(tuple);
		}
		
		
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector Collector) {
		// 初始化函数，对应Spout里的open()
		this.Collector = Collector ;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// 定义输出字段的名字
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// 设置属性参数
		return null;
	}

	
}
