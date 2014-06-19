package excelexporter.ganglia.log;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.jcraft.jsch.JSchException;

public class TaskTimeCostExtractor
{
    private final LogFileFinder   finder;
    private final MapLogParser    mapLogParser;
    private final ReduceLogParser reduceLogParser;
    private final boolean         isSlowStart;

    public TaskTimeCostExtractor(LogFileFinder finder,
	    MapLogParser mapLogParser, ReduceLogParser reduceLogParser,
	    boolean isSlowStart)
    {
	this.finder = finder;
	this.mapLogParser = mapLogParser;
	this.reduceLogParser = reduceLogParser;
	this.isSlowStart = isSlowStart;
    }
    
    @SuppressWarnings("unused")
    public Map<String, Long> extract(String logRoot, String jobID) throws JSchException, IOException{
	String jobPath = logRoot + "/" + jobID;
	String[] taskName = finder.findTasks(jobPath);
	Map<String, Long> result = new HashMap<String, Long>();
	if(taskName[0] != null){
	    System.out.println("Map Task: " + taskName[0]);
	    Map<String, Timestamp> mapResult = mapLogParser.parse(taskName[0]);
	    long t0 = mapResult.get("t0").getTime();
	    long t1 = mapResult.get("t1").getTime();
	    long t2 = mapResult.get("t2").getTime();
	    long t3 = mapResult.get("t3").getTime();
	    long t4 = mapResult.get("t4").getTime();
	    result.put("Map: AS processing", t3 - t2);
	    result.put("Map: AS load", t2 - t1);
	    result.put("Map: M/R framework", (t1 - t0) + (t4 - t3));
	}
	if(taskName[1] != null){
	    System.out.println("Reduce Task: " + taskName[1]);
	    Map<String, Timestamp> reduceResult = reduceLogParser.parse(taskName[1]);
	    long t0 = reduceResult.get("t0").getTime();
	    long t1 = reduceResult.get("t1").getTime();
	    long t2 = reduceResult.get("t2").getTime();
	    long t3 = reduceResult.get("t3").getTime();
	    long t4 = reduceResult.get("t4").getTime();
	    result.put("Reduce: AS total", t4 - t2);
	    if(isSlowStart)
		result.put("Reduce: M/R framework", t2 - t0);
	    else
		result.put("Reduce: M/R framework", t2 - t1);
	}
	return result;
    }

}
