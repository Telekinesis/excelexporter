package excelexporter.ganglia.lineparser;


public class MemFileLineParser extends GangliaCsvLineParser<MemTuple> {

	@Override
	public void parse(String line) {
		String[] terms = line.split(",");
		long time = Long.parseLong(terms[0]);
		double buffer = Double.parseDouble(terms[2]);
		double cached = Double.parseDouble(terms[3]);
		double free = Double.parseDouble(terms[4]);
		double shared = Double.parseDouble(terms[5]);
		result.put(time, new MemTuple(buffer, cached, free, shared));
	}

	@Override
	public void onEnd() {
	}

}
