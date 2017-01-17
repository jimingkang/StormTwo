package ack_fail;

import java.util.HashMap;
import java.util.Map;
 
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.AuthorizationException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class Ack_FailTopo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("spout", new AckSpout(), 1);   // 1004行
//		builder.setSpout("spout2", new AckSpout(), 1); 
		builder.setBolt("bolt", new AckBolt(), 1).shuffleGrouping("spout");
		builder.setBolt("bolt2", new AckBolt2(), 1).shuffleGrouping("bolt");
//		builder.setBolt("bolt", new MyBolt(), 2).fieldsGrouping("spout", new Fields("session_id"));
//		builder.setBolt("bolt", new MyBolt(), 2).globalGrouping("spout");
		
		
//		
		Config conf = new Config() ;
//		conf.put(Config.TOPOLOGY_ACKER_EXECUTORS, 0);
		conf.setDebug(false);
//		conf.put("dev.zookeeper.path", "/tmp/dev-storm-zookeeper2");
		
		
		
		if (args.length > 0) {
			try {
				try {
					StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
				} catch (AuthorizationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (AlreadyAliveException e) {
				e.printStackTrace();
			} catch (InvalidTopologyException e) {
				e.printStackTrace();
			}
		}else {
			LocalCluster localCluster = new LocalCluster();
			localCluster.submitTopology("mytopology", conf, builder.createTopology());
		}
		
		
		
		

	}

}
