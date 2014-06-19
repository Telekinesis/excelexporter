package excelexporter.ganglia.log;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.telekinesis.commonclasses.io.ByLineReader;

public class ReduceLogParser
{
    private final ByLineReader<String> reader;

    public ReduceLogParser(ByLineReader<String> reader)
    {
	this.reader = reader;
    }

    public Map<String, Timestamp> parse(String path) throws IOException
    {
	TaskLineParser parser = new TaskLineParser();
	reader.read(path, parser, 0);
	List<TaskRecord> records = parser.getRecords();
	final Map<String, Timestamp> timeSpots = new HashMap<String, Timestamp>();
	TargetTaskLogExtractor extractor = new TargetTaskLogExtractor(
	        new TaskRecordFilter()
	        {

		    @Override
		    public void doSomething(TaskRecord record)
		    {
		        timeSpots.put("t1", record.getTime());
		    }

		    @Override
		    public boolean accept(TaskRecord record)
		    {
		        return record.getSourceClass().equals(
		                "org.apache.hadoop.mapred.ReduceTask")
		                && record.getInformation().startsWith(
		                        "In-memory merge complete");
		    }
	        }, new TaskRecordFilter()
	        {

		    @Override
		    public void doSomething(TaskRecord record)
		    {
		        timeSpots.put("t2", record.getTime());
		    }

		    @Override
		    public boolean accept(TaskRecord record)
		    {
		        return record
		                .getSourceClass()
		                .equals("com.ibm.spss.afcomponentloader.AFComponentLoaderModule")
		                && record.getInformation().startsWith(
		                        "Components location");
		    }
	        }, new TaskRecordFilter()
	        {

		    @Override
		    public void doSomething(TaskRecord record)
		    {
		        timeSpots.put("t3", record.getTime());
		    }

		    @Override
		    public boolean accept(TaskRecord record)
		    {
		        return record.getSourceClass().equals(
		                "ComponentFramework")
		                && record.getInformation().startsWith(
		                        "Using application classloader");
		    }
	        });
	extractor.extract(records);
	timeSpots.put("t0", records.get(0).getTime());
	timeSpots.put("t4", records.get(records.size() - 1).getTime());
	return timeSpots;
    }
}
