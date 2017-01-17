package com.ibf.cloudy.lesson1;

import java.io.IOException;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class FileTopo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("spout", new FileSpout(), 1);  //并发度为1
		
		builder.setBolt("bolt1", new FileBolt1(), 2)//.shuffleGrouping("spout") ;
		      //.fieldsGrouping("spout", new Fields("guid"));
		      .globalGrouping("spout");
		      
		
		Config conf = new Config();
	    conf.setDebug(false);   //调试模式，多输出日志
	    
	    try{
	    	if (args != null && args.length > 0) {
		        conf.setNumWorkers(3);

		        StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
		      }
		   else {
//		        conf.setMaxTaskParallelism(3);

		        LocalCluster cluster = new LocalCluster();
		        cluster.submitTopology("FileCount", conf, builder.createTopology());

//		        Thread.sleep(10000);

//		        cluster.shutdown();
		      }
	    }
	    catch (Exception e) {
			e.printStackTrace();
		}
	    
	    
	    
	    
	    
		
		
		
		
		
		
		
		
	}

}
