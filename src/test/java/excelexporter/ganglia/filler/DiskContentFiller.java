package excelexporter.ganglia.filler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.telekinesis.commonclasses.debug.MapPrinter;
import org.telekinesis.excelexporter.assigner.cell.CellAssigner;
import org.telekinesis.excelexporter.assigner.cell.DoubleValueAssigner;
import org.telekinesis.excelexporter.sheet.SheetEditor;

import excelexporter.ganglia.GangliaExcelExporter;
import excelexporter.ganglia.NumberListBasedColumnValuesAssigner;
import excelexporter.ganglia.importing.CsvLoader;
import excelexporter.ganglia.lineparser.GangliaCsvLineParser;

public class DiskContentFiller extends SheetFiller<GangliaCsvLineParser<Double>> {

	public DiskContentFiller(CsvLoader reader, GangliaCsvLineParser<Double> lineParser) {
		super(reader, lineParser);
	}

	@Override
	protected void writeToSheet(GangliaCsvLineParser<Double> lineParser, Sheet sheet) {
		Map<Long, Double> result = lineParser.getResult();
		MapPrinter.print(result);
		List<Double> numbers = convertParseResultToList(result);
		Map<Integer, CellAssigner> assigners = new HashMap<Integer, CellAssigner>();
		assigners.put(0, new DoubleValueAssigner() {

			@Override
			protected Double create(int rowIndex, int columnIndex) {
				return (rowIndex - 1) * GangliaExcelExporter.gangliaInterval
						* 1D;
			}
		});
		assigners.put(1, new NumberListBasedColumnValuesAssigner(numbers, 1));
		SheetEditor.fillColumns(sheet, assigners, 1, 1 + numbers.size() - 1);
	}

	private List<Double> convertParseResultToList(
			Map<Long, Double> result) {
		List<Double> numbers = new ArrayList<Double>();
		for (Long time : result.keySet()) {
			numbers.add(result.get(time));
		}
		return numbers;
	}

}
