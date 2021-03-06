package excelexporter.ganglia.filler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.telekinesis.commonclasses.io.CsvColumnExtractor;
import org.telekinesis.excelexporter.assigner.cell.CellAssigner;
import org.telekinesis.excelexporter.assigner.cell.CellValueType;
import org.telekinesis.excelexporter.assigner.cell.DoubleValueAssigner;
import org.telekinesis.excelexporter.assigner.cell.FormulaAssigner;
import org.telekinesis.excelexporter.assigner.cell.ListTranslatingAssigner;
import org.telekinesis.excelexporter.sheet.SheetEditor;

import excelexporter.ganglia.GangliaExcelExporter;
import excelexporter.ganglia.importing.CsvLoader;

public class MemContentFiller extends SheetFiller<CsvColumnExtractor>
{
	private final long totalMemory;
	
	public MemContentFiller(CsvLoader reader, long totalMemory) {
		super(reader, new CsvColumnExtractor(2,3,4,5));
		this.totalMemory = totalMemory;
	}

	@Override
	protected void writeToSheet(CsvColumnExtractor lineParser, Sheet sheet) {
		Map<Integer, List<String>> extractedColumns = lineParser.getExtractedColumns();
		int recordCount = lineParser.getRecordCount();
		Map<Integer, CellAssigner> assigners = new HashMap<Integer, CellAssigner>();
		assigners.put(3, new ListTranslatingAssigner(CellValueType.DOUBLE, extractedColumns.get(2), 1));
		assigners.put(4, new ListTranslatingAssigner(CellValueType.DOUBLE, extractedColumns.get(3), 1));
		assigners.put(5, new ListTranslatingAssigner(CellValueType.DOUBLE, extractedColumns.get(4), 1));
		assigners.put(6, new ListTranslatingAssigner(CellValueType.DOUBLE, extractedColumns.get(5), 1));
		assigners.put(7, new DoubleValueAssigner()
		{
		    
		    @Override
		    protected Double create(int rowIndex, int columnIndex)
		    {
			return totalMemory * 1D;
		    }
		});
		assigners.put(8, new DoubleValueAssigner()
		{
		    
		    @Override
		    protected Double create(int rowIndex, int columnIndex)
		    {
			return (rowIndex - 1) * GangliaExcelExporter.gangliaInterval * 1D;
		    }
		});
		assigners.put(9, new FormulaAssigner()
		{
		    
		    @Override
		    protected String create(int rowIndex, int columnIndex)
		    {
			return String.format("H%d-F%d-E%d-D%d", rowIndex + 1, rowIndex + 1, rowIndex + 1, rowIndex + 1);
		    }
		});
		SheetEditor.fillColumns(sheet, assigners, 1, 1 + recordCount - 1);	
	}

}
