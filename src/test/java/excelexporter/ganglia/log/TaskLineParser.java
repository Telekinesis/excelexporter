package excelexporter.ganglia.log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.telekinesis.commonclasses.io.LineParser;

public class TaskLineParser implements LineParser
{
    private static final String regex = "(\\d+-\\d+-\\d+\\s\\d+\\:\\d+\\:\\d+\\,\\d+)(\\s+)(\\S+)(\\s+)(\\S+)(\\:\\s*)(.*)";
    private final Pattern pattern;
    private List<TaskRecord> records = new ArrayList<TaskRecord>();
    
    public TaskLineParser(){
	this.pattern = Pattern.compile(regex);
    }
    
    @Override
    public void parse(String line)
    {
	Matcher m = pattern.matcher(line);
	if(m.find()){
	    String timeString = m.group(1);
	    String sourceClass = m.group(5);
	    String information = m.group(7);
	    try{
		Timestamp time = Timestamp.valueOf(timeString.replaceFirst("\\,", "."));
		records.add(new TaskRecord(time, sourceClass, information));
	    }catch(IllegalArgumentException e){
		System.out.println(line);
		throw e;
	    }
	}
    }

    @Override
    public void onEnd()
    {
    }

    public List<TaskRecord> getRecords()
    {
        return records;
    }
    
}
