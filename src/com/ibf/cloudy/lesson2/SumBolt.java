package com.ibf.cloudy.lesson2;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class SumBolt extends BaseRichBolt{

	HashMap<String, Long> map = new HashMap<String, Long>();
	
	long t1 = System.currentTimeMillis();
	@Override
	public void execute(Tuple tuple) {
		// TODO Auto-generated method stub
		String date = tuple.getString(0) ; // 2015-08-28
		String provinceId = tuple.getString(1) ;
		String url = tuple.getString(2);
		
		String key = date+"_"+provinceId;
		System.err.println(key);
		Long count = map.get(key) ;
		if(count == null)
		{
			count = 0L;  //第一次给初始值
		}
		count ++ ;
		
		map.put(key, count);
		
		long t2 = System.currentTimeMillis();
//		if (t2-t1 >= 10000) 
//		{
			for(String k : map.keySet())
	       {
	        	  System.out.println(k+"====>"+map.get(k));
	       }
//		}
		
		
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub
		
	}

}
