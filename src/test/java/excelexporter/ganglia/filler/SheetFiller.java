package excelexporter.ganglia.filler;

import org.apache.poi.ss.usermodel.Sheet;
import org.telekinesis.commonclasses.io.LineParser;

import excelexporter.ganglia.importing.CsvLoader;

public abstract class SheetFiller<T extends LineParser> {

	private final CsvLoader reader;
	private final T lineParser;

	public SheetFiller(CsvLoader reader, T lineParser) {
		this.reader = reader;
		this.lineParser = lineParser;
	}

	public void fill(Sheet sheet, String csvPath) {
		reader.load(lineParser, csvPath);
		writeToSheet(lineParser, sheet);
	}

	protected abstract void writeToSheet(T lineParser, Sheet sheet);
}
