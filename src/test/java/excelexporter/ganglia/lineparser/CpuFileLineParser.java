package excelexporter.ganglia.lineparser;


public class CpuFileLineParser extends GangliaCsvLineParser<CpuTuple> {
	
	@Override
	public void parse(String line) {
		String[] terms = line.split(",");
		long time = Long.parseLong(terms[0]);
		double user = Double.parseDouble(terms[2]);
		double system = Double.parseDouble(terms[3]);
		double wio = Double.parseDouble(terms[4]);
		double idle = Double.parseDouble(terms[6]);
		result.put(time, new CpuTuple(user, system, wio, idle));
	}

	@Override
	public void onEnd() {
	}

}
