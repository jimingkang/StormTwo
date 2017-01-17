package user_visit;

import java.util.Map;

import tools.DateFmt;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class FmtLogBolt implements IBasicBolt{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}
	String eachLog = null;
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		// TODO Auto-generated method stub
		eachLog = input.getString(0) ;
		if (eachLog != null && eachLog.length() > 0 ) {
			
			collector.emit(new Values(DateFmt.getCountDate(eachLog.split("\t")[2],DateFmt.date_short),eachLog.split("\t")[1])) ;// 日期, session_id
			
			
		}
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("date","session_id"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
