package excelexporter.ganglia.importing;

import org.telekinesis.commonclasses.io.LineParser;

public interface CsvLoader {
	public void load(LineParser parser, String path);
}
