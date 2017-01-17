package com.ibf.cloudy.lesson1;

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

public class FileSpout implements IRichSpout{

	//该类能把数据，传到下一级组件
	SpoutOutputCollector collector = null;
	BufferedReader br =  null;
	
	@Override
	public void ack(Object arg0) {
		// 当Bolt类里执行成功，则回调该函数
		System.err.println("ack "+arg0.toString());
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
		System.err.println("fail "+arg0.toString());
		
		//进行重新发送该tuple，框架不会自动重发，需要我们自己程序控制。
		
	}

	@Override
	public void nextTuple() {
		// 从外部数据源拿数据
		// 死循环
		/**
		 * 读文件，文件作为我们的练习数据源
		 */
		try {
			
			String data = null;
		
			while((data = br.readLine())!=null)
			{
			   System.out.println(data); 
			   String arr[] = data.split("\t");
			   if (arr.length>1 && arr[0]!=null && arr[1]!=null) {
				                           //guid  id
				   collector.emit(new Values(arr[1],arr[0]));
				
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
			br = new BufferedReader(new InputStreamReader(new FileInputStream("E:/大数据O2O周末班11/Day16_Storm基础篇/data.txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// 定义输出数据的字段名
		declarer.declare(new Fields("guid","id"));
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// 配置环境变量
		return null;
	}
	

}
