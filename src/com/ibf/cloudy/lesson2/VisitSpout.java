package com.ibf.cloudy.lesson2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class VisitSpout implements IRichSpout{

	//该类能把数据，传到下一级组件
	SpoutOutputCollector collector = null;
	BufferedReader br =  null;
	
	@Override
	public void ack(Object arg0) {
		// 当Bolt类里执行成功，则回调该函数
		
	}

	@Override
	public void activate() {
		// 激活Topo
		
	}

	@Override
	public void close() {
		// 关闭Topo
		
	}

	@Override
	public void deactivate() {
		// 非激活
		
	}

	@Override
	public void fail(Object arg0) {
		// 当Bolt类里执行失败，则回调该函数
		
	}

	@Override
	public void nextTuple() {
		// 从外部数据源拿数据
		// 死循环
		/**
		 * 读文件，文件作为我们的练习数据源
		 */
		try {
			// date, provinceid ,url
			
			String data = null;
		
			while((data = br.readLine())!=null)
			{
//			   System.out.println(data); 
			   String arr[] = data.split("\t");
			   if (arr.length>20) {
				 String date = arr[17].substring(0,10) ; // 2015-08-28
				 String provinceId = arr[23] ;
				 String url = arr[1];
				 
				 if(date!=null && provinceId!=null && url!=null)
				 {
					 collector.emit(new Values(date,provinceId,url));
				 }
			   }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void open(Map arg0, TopologyContext arg1, SpoutOutputCollector collector) {
		// 初始化函数
		this.collector = collector ;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream("E:/大数据O2O周末班11/Day16_Storm基础篇/2015082818")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// 定义输出数据的字段名
		declarer.declare(new Fields("date","provinceId","url"));
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// 配置环境变量
		return null;
	}
	

}
