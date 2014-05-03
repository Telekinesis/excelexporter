package excelexporter.ganglia.lineparser;

import java.util.SortedMap;
import java.util.TreeMap;

import org.telekinesis.commonclasses.io.LineParser;

public abstract class GangliaCsvLineParser<R> implements LineParser {
	
	protected SortedMap<Long, R> result = new TreeMap<Long, R>();
	
	public SortedMap<Long, R> getResult(){
		return result;
	}
}
