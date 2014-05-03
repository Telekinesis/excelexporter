package excelexporter.ganglia.importing;

import org.telekinesis.commonclasses.io.LineParser;
import org.telekinesis.commonclasses.io.LocalLineFileReader;

public class LocalCsvLoader implements CsvLoader {

	@Override
	public void load(LineParser parser, String path) {
		LocalLineFileReader reader = new LocalLineFileReader();
		reader.read(path, parser, 1);
	}

}
