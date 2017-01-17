/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibf.cloudy.lesson1;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.task.ShellBolt;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import storm.starter.spout.RandomSentenceSpout;

import java.util.HashMap;
import java.util.Map;

/**
 * This topology demonstrates Storm's stream groupings and multilang capabilities.
 */
public class WordCountTopology {
  public static class SplitBolt extends BaseBasicBolt{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		// TODO Auto-generated method stub
		String words = tuple.getString(0);
		System.out.println(words);
		
		for(String word : words.split(" ")){
			collector.emit(new Values(word));
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("word"));
	}
	  
	  
	  
  }
  
  public static class WordCount extends BaseBasicBolt {
	  /**
	 * 
	 */
	  
	long t1 = System.currentTimeMillis();
	private static final long serialVersionUID = 1L;
	// map里存的是单词和其个数
    Map<String, Integer> counts = new HashMap<String, Integer>();
    
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
      String word = tuple.getString(0);
      Integer count = counts.get(word);
      if (count == null)
      {
    	  count = 0;
      }
      count++;
      counts.put(word, count);
//      collector.emit(new Values(word, count));
      //写数据库、或其他
      long t2 = System.currentTimeMillis();
      
      if ( t2 - t1 >= 5000 ) {
    	  for(String key : counts.keySet())
          {
        	  System.out.println(key+"====>"+counts.get(key));
          }
      }
      
    }
    
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
//      declarer.declare(new Fields("word", "count"));
    }
  }

  public static void main(String[] args) throws Exception {
    
    TopologyBuilder builder = new TopologyBuilder();

    builder.setSpout("spout", new RandomSentenceSpout(), 5);//  这里的5、8、12都是进程数，而不是线程数
                                                            //  默认每个进程含1个线程
                                                            //  excutor     task
    

    builder.setBolt("split", new SplitBolt(), 8).shuffleGrouping("spout").setNumTasks(16);
    builder.setBolt("count", new WordCount(), 12).fieldsGrouping("split", new Fields("word"));

    /**
     * select word,count(1)
     *  from table
     *   group by word ;
     *   
     *   
     * select date,provinceid,cityid,cateid,count(url)pv ,sum...
     *  from table
     * group by date,provinceid,cityid,cateid;  
     *   
     */
    Config conf = new Config();
    conf.setDebug(false);


    if (args != null && args.length > 0) {
      conf.setNumWorkers(3);

      StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
    }
    else {
      conf.setMaxTaskParallelism(3);

      LocalCluster cluster = new LocalCluster();
      cluster.submitTopology("word-count", conf, builder.createTopology());

//      Thread.sleep(10000);
//
//      cluster.shutdown();
    }
  }
}
