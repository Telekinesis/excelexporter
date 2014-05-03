package excelexporter.ganglia.lineparser;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class DiskAverageLineParser extends GangliaCsvLineParser<Double> {
	private final SortedMap<Long, List<Double>> buffer = new TreeMap<Long, List<Double>>();

	@Override
	public void parse(String line) {
		String[] terms = line.split(",");
		long time = Long.parseLong(terms[0]);
		String diskName = terms[2];
		double percent = Double.parseDouble(terms[3]) / 100 / 4;
		if (!diskName.equals("sda")) {
			putToAverage(time, percent);
		}
	}

	private void putToAverage(long time, double value) {
		List<Double> numberList = buffer.get(time);
		if (numberList == null) {
			numberList = new ArrayList<Double>();
			buffer.put(time, numberList);
		}
		numberList.add(value);
	}

	@Override
	public void onEnd() {
		for (Long time : buffer.keySet()) {
			List<Double> valuelist = buffer.get(time);
			double sum = 0;
			for (Double d : valuelist) {
				sum += d;
			}
			result.put(time, sum / valuelist.size());
		}
	}
	
}
