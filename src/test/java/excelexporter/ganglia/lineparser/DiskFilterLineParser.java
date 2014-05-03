package excelexporter.ganglia.lineparser;


public class DiskFilterLineParser extends GangliaCsvLineParser<Double> {
	private final String selectedDisk;

	public DiskFilterLineParser(String selectedDisk) {
		this.selectedDisk = selectedDisk;
	}

	@Override
	public void parse(String line) {
		String[] terms = line.split(",");
		String disk = terms[2];
		if (disk.equals(selectedDisk)) {
			long time = Long.parseLong(terms[0]);
			double ratio = Double.parseDouble(terms[3]) / 100 / 4;
			result.put(time, ratio);
		}
	}

	@Override
	public void onEnd() {
	}

}
