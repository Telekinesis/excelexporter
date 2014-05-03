package excelexporter.ganglia.lineparser;

public class NetFileLineParser extends GangliaCsvLineParser<NetTuple> {

	@Override
	public void parse(String line) {
		String[] terms = line.split(",");
		long time = Long.parseLong(terms[0]);
		double bytesIn = Double.parseDouble(terms[2]);
		double bytesOut = Double.parseDouble(terms[3]);
		result.put(time, new NetTuple(bytesIn, bytesOut));
	}

	@Override
	public void onEnd() {
	}

}
