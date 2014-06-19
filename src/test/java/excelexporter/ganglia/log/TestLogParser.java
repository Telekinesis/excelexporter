package excelexporter.ganglia.log;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;
import org.telekinesis.commonclasses.debug.MapPrinter;
import org.telekinesis.commonclasses.io.ByLineReader;
import org.telekinesis.commonclasses.io.SshLineFileReader;

import com.jcraft.jsch.JSchException;

public class TestLogParser
{
    private static final String line = "2014-04-30 07:46:58,256 INFO org.apache.hadoop.mapred.MapTask: io.sort.mb = 100";
    private static final String host = "9.186.107.15";
    private static final String username = "biadmin4";
    private static final String password = "biadmin4";
    private static final String logRoot = "/home/home/data/bi21d/logs/userlogs";
    private static final String jobID = "job_201405050921_0003";

    @Test
    public void testRegex()
    {
	String regex = "(\\d+-\\d+-\\d+\\s\\d+\\:\\d+\\:\\d+\\,\\d+)(\\s+)(\\S+)(\\s+)(\\S+)(\\:\\s*)(.*)";
	Pattern pattern = Pattern.compile(regex);
	Matcher m = pattern.matcher(line);
	if (m.find())
	{
	    for (int i = 0; i <= m.groupCount(); i++)
	    {
		System.out.println(m.group(i));
	    }
	}
    }
    
    @Ignore@Test
    public void testVerifyTaskName(){
	String regex = "(attempt)_(\\d+)_(\\d+)_(m|r)_(\\d+)_(\\d+)";
	String taskName = "attempt_201405050222_0009_m_000309_1";
	Pattern pattern = Pattern.compile(regex);
	Matcher m = pattern.matcher(taskName);
	if (m.matches())
	{
	    System.out.println(m.groupCount());
	    for (int i = 0; i <= m.groupCount(); i++)
	    {
		System.out.println(m.group(i));
	    }
	}
    }
    
    @Ignore@Test
    public void testLogFinder() throws JSchException, IOException{
	LogFileFinder finder = new LogFileFinder(host, username, password);
	ByLineReader<String> reader = new SshLineFileReader(host, username, password);
	MapLogParser mapParser = new MapLogParser(reader);
	ReduceLogParser reduceParser = new ReduceLogParser(reader);
	TaskTimeCostExtractor extractor = new TaskTimeCostExtractor(finder, mapParser, reduceParser, false);
	System.out.println("biginsights: job_201405051258_0003");
	System.out.println(MapPrinter.print(extractor.extract(logRoot, "job_201405051258_0003")));
	System.out.println("bigant: job_201405050921_0003");
	System.out.println(MapPrinter.print(extractor.extract(logRoot, "job_201405050921_0003")));
    }
}
