package user_visit;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class DeepVisitBolt implements IBasicBolt{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}
	Map<String, Integer> counts = new HashMap<String, Integer>();
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		// TODO Auto-generated method stub
		String dateString = input.getStringByField("date");
		String session_id = input.getStringByField("session_id");
		Integer count = counts.get(dateString+"_"+session_id);
		if (count == null) {
			count = 0;
		}
		count ++ ;
		
		counts.put(dateString+"_"+session_id,count) ;  //日期_sessionid, pv
		collector.emit(new Values(dateString+"_"+session_id,count)) ;
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("date_session_id","count"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
