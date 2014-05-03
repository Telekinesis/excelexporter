package excelexporter.ganglia.filler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.telekinesis.commonclasses.io.CsvColumnExtractor;
import org.telekinesis.excelexporter.assigner.cell.CellAssigner;
import org.telekinesis.excelexporter.assigner.cell.CellValueType;
import org.telekinesis.excelexporter.assigner.cell.DoubleValueAssigner;
import org.telekinesis.excelexporter.assigner.cell.ListTranslatingAssigner;
import org.telekinesis.excelexporter.sheet.SheetEditor;

import excelexporter.ganglia.GangliaExcelExporter;
import excelexporter.ganglia.importing.CsvLoader;

public class NetContentFiller extends SheetFiller<CsvColumnExtractor> {
	public NetContentFiller(CsvLoader reader) {
		super(reader, new CsvColumnExtractor(2, 3));
	}

	@Override
	protected void writeToSheet(CsvColumnExtractor lineParser, Sheet sheet) {
		Map<Integer, List<String>> extractedColumns = lineParser
				.getExtractedColumns();
		int recordCount = lineParser.getRecordCount();
		Map<Integer, CellAssigner> assigners = new HashMap<Integer, CellAssigner>();
		assigners.put(1, new DoubleValueAssigner() {

			@Override
			protected Double create(int rowIndex, int columnIndex) {
				return (rowIndex - 1) * GangliaExcelExporter.gangliaInterval
						* 1D;
			}
		});
		assigners.put(2, new ListTranslatingAssigner(CellValueType.DOUBLE,
				extractedColumns.get(2), 1));
		assigners.put(3, new ListTranslatingAssigner(CellValueType.DOUBLE,
				extractedColumns.get(3), 1));
		SheetEditor.fillColumns(sheet, assigners, 1, 1 + recordCount - 1);
	}
}
