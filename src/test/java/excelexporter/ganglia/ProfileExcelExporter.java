package excelexporter.ganglia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.telekinesis.commonclasses.entity.Pair;
import org.telekinesis.excelexporter.assigner.row.RowAssigner;
import org.telekinesis.excelexporter.sheet.SheetEditor;

import excelexporter.ganglia.importing.CsvLoader;
import excelexporter.ganglia.interpolation.GangliaTupleInterpolator;
import excelexporter.ganglia.lineparser.CpuTuple;
import excelexporter.ganglia.lineparser.MemTuple;
import excelexporter.ganglia.lineparser.NetTuple;

public class ProfileExcelExporter {
	private static final String CPU_PAGE = "ggCpu";
	private static final String MEM_PAGE = "ggMem";
	private static final String NET_PAGE = "ggNet";
	private static final String DISK_PAGE = "ggDiskState";

	private final long timeInterval;
	private final long totalMemory;
	private final CsvLoader csvLoader;
	private final GangliaTupleProcessor<CpuTuple> cpuParser;
	private final GangliaTupleProcessor<MemTuple> memParser;
	private final GangliaTupleProcessor<NetTuple> netParser;
	private final GangliaTupleProcessor<Double> diskParser;

	public ProfileExcelExporter(long totalMemory, long timeInterval,
			CsvLoader csvLoader,
			GangliaTupleProcessor<CpuTuple> cpuParser,
			GangliaTupleProcessor<MemTuple> memParser,
			GangliaTupleProcessor<NetTuple> netParser,
			GangliaTupleProcessor<Double> diskParser) {
		this.totalMemory = totalMemory;
		this.timeInterval = timeInterval;
		this.csvLoader = csvLoader;
		this.cpuParser = cpuParser;
		this.memParser = memParser;
		this.netParser = netParser;
		this.diskParser = diskParser;
	}

	public void export(String templatePath, String exportPath, String cpuPath,
			String memPath, String netPath, String diskPath) throws IOException {
		loadDataFromCsv(cpuPath, memPath, netPath, diskPath);
		Pair<Long, Long> timeBound = getTimeBound();
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File(templatePath)));
		fillCpuData(workbook, timeBound);
		fillMemData(workbook, timeBound);
		fillNetData(workbook, timeBound);
		fillDiskData(workbook, timeBound);
		workbook.write(new FileOutputStream(exportPath));
	}

	private void loadDataFromCsv(String cpuPath, String memPath,
			String netPath, String diskPath) {
		csvLoader.load(cpuParser.getParser(), cpuPath);
		csvLoader.load(memParser.getParser(), memPath);
		csvLoader.load(netParser.getParser(), netPath);
		csvLoader.load(diskParser.getParser(), diskPath);
	}

	private Pair<Long, Long> getTimeBound() {
		long minTime = Long.MAX_VALUE;
		long maxTime = Long.MIN_VALUE;
		minTime = Math.min(minTime, cpuParser.getParser().getResult().firstKey());
		minTime = Math.min(minTime, memParser.getParser().getResult().firstKey());
		minTime = Math.min(minTime, netParser.getParser().getResult().firstKey());
		minTime = Math.min(minTime, diskParser.getParser().getResult().firstKey());
		maxTime = Math.max(maxTime, cpuParser.getParser().getResult().lastKey());
		maxTime = Math.max(maxTime, memParser.getParser().getResult().lastKey());
		maxTime = Math.max(maxTime, netParser.getParser().getResult().lastKey());
		maxTime = Math.max(maxTime, diskParser.getParser().getResult().lastKey());
		return new Pair<Long, Long>(minTime, maxTime);
	}
	
	private void fillCpuData(Workbook workbook, final Pair<Long, Long> timeBound){
		final List<CpuTuple> interpolatedData = interpolate(cpuParser, timeBound);
		Sheet sheet = extractAndClearSheet(workbook, CPU_PAGE);
		RowAssigner assigner = new RowAssigner() {

			@Override
			public void assign(Row row, int rowIndex) {
				long time = timeBound.getA() + (rowIndex - 1) * timeInterval;
				CpuTuple tuple = interpolatedData.get(rowIndex - 1);
				SheetEditor.getCell(row, 0).setCellValue(time);
				SheetEditor.getCell(row, 2).setCellValue(tuple.getUser());
				SheetEditor.getCell(row, 3).setCellValue(tuple.getSystem());
				SheetEditor.getCell(row, 4).setCellValue(tuple.getWio());
				SheetEditor.getCell(row, 5).setCellValue(tuple.getIdle());
				SheetEditor.getCell(row, 6).setCellFormula(String.format("SUM(C%d:F%d)", rowIndex + 1, rowIndex + 1));
				SheetEditor.getCell(row, 7).setCellValue((rowIndex - 1) * timeInterval * 1D);
				SheetEditor.getCell(row, 8).setCellFormula(String.format("C%d/G%d", rowIndex + 1, rowIndex + 1));
				SheetEditor.getCell(row, 9).setCellFormula(String.format("D%d/G%d", rowIndex + 1, rowIndex + 1));
				SheetEditor.getCell(row, 10).setCellFormula(String.format("E%d/G%d", rowIndex + 1, rowIndex + 1));
			}
		};
		SheetEditor.fillRows(assigner, sheet, 1, 1 + interpolatedData.size() - 1);
	}
	
	private void fillMemData(Workbook workbook, final Pair<Long, Long> timeBound){
		final List<MemTuple> interpolatedData = interpolate(memParser, timeBound);
		Sheet sheet = extractAndClearSheet(workbook, MEM_PAGE);
		RowAssigner assigner = new RowAssigner() {
			
			@Override
			public void assign(Row row, int rowIndex) {
				long time = timeBound.getA() + (rowIndex - 1) * timeInterval;
				MemTuple tuple = interpolatedData.get(rowIndex - 1);
				SheetEditor.getCell(row, 0).setCellValue(time);
				SheetEditor.getCell(row, 3).setCellValue(tuple.getBuffer());
				SheetEditor.getCell(row, 4).setCellValue(tuple.getCached());
				SheetEditor.getCell(row, 5).setCellValue(tuple.getFree());
				SheetEditor.getCell(row, 6).setCellValue(tuple.getShared());
				SheetEditor.getCell(row, 7).setCellValue(totalMemory);
				SheetEditor.getCell(row, 8).setCellValue((rowIndex - 1) * timeInterval * 1D);
				SheetEditor.getCell(row, 9).setCellFormula(String.format("H%d-F%d-E%d-D%d", rowIndex + 1, rowIndex + 1, rowIndex + 1, rowIndex + 1));
			}
		};
		SheetEditor.fillRows(assigner, sheet, 1, 1 + interpolatedData.size() - 1);
	}
	
	private void fillNetData(Workbook workbook, final Pair<Long, Long> timeBound){
		final List<NetTuple> interpolatedData = interpolate(netParser, timeBound);
		Sheet sheet = extractAndClearSheet(workbook, NET_PAGE);
		RowAssigner assigner = new RowAssigner() {
			
			@Override
			public void assign(Row row, int rowIndex) {
				long time = timeBound.getA() + (rowIndex - 1) * timeInterval;
				NetTuple tuple = interpolatedData.get(rowIndex - 1);
				SheetEditor.getCell(row, 0).setCellValue(time);
				SheetEditor.getCell(row, 1).setCellValue((rowIndex - 1) * timeInterval * 1D);
				SheetEditor.getCell(row, 2).setCellValue(tuple.getBytesIn());
				SheetEditor.getCell(row, 3).setCellValue(tuple.getBytesOut());
			}
		};
		SheetEditor.fillRows(assigner, sheet, 1, 1 + interpolatedData.size() - 1);
	}
	
	private void fillDiskData(Workbook workbook, final Pair<Long, Long> timeBound){
		final List<Double> interpolatedData = interpolate(diskParser, timeBound);
		Sheet sheet = extractAndClearSheet(workbook, DISK_PAGE);
		RowAssigner assigner = new RowAssigner() {
			
			@Override
			public void assign(Row row, int rowIndex) {
				long time = timeBound.getA() + (rowIndex - 1) * timeInterval;
				Double tuple = interpolatedData.get(rowIndex - 1);
				SheetEditor.getCell(row, 0).setCellValue((rowIndex - 1) * timeInterval * 1D);
				SheetEditor.getCell(row, 1).setCellValue(tuple);
				SheetEditor.getCell(row, 2).setCellValue(time);
			}
		};
		SheetEditor.fillRows(assigner, sheet, 1, 1 + interpolatedData.size() - 1);
	}

	private <T> List<T> interpolate(GangliaTupleProcessor<T> processor, Pair<Long, Long> timeBound) {
		List<T> interpolated = new ArrayList<T>();
		SortedMap<Long, T> result = processor.getParser().getResult();
		GangliaTupleInterpolator<T> borderInterpolator = processor.getBorderInterpolator();
		GangliaTupleInterpolator<T> middleInterpolator = processor.getMiddleInterpolator();
		if (result.isEmpty()) {
			int missingNumber = (int) ((timeBound.getB() - timeBound.getA()) / timeInterval) + 1; 
			System.out.println(String.format("Interpolated using %s from %d to %d", borderInterpolator.getClass(), timeBound.getA(), timeBound.getB()));
			System.out.println("Interpolated using " + borderInterpolator.getClass());
			T[] blankList = borderInterpolator
					.interpolate(null, null, missingNumber);
			Collections.addAll(interpolated, blankList);
			return interpolated;
		} else {
			Long firstTime = result.firstKey();
			if (!firstTime.equals(timeBound.getA())) {
				int missingNumber = (int) ((firstTime - timeBound.getA()) / timeInterval);
				System.out.println(String.format("Interpolated using %s from %d to %d", borderInterpolator.getClass(), timeBound.getA(), firstTime));
				System.out.println("Interpolated using " + borderInterpolator.getClass());
				T[] starting = borderInterpolator.interpolate(null,
						result.get(firstTime), missingNumber);
				Collections.addAll(interpolated, starting);
			}
			Iterator<Long> iterator = result.keySet().iterator();
			long formerTime = iterator.next();
			T formerData = result.get(formerTime);
			interpolated.add(formerData);
			while (iterator.hasNext()) {
				long latterTime = iterator.next();
				T latterData = result.get(latterTime);
				long difference = latterTime - formerTime;
				if(difference > timeInterval){
					int missingNumber = (int)(difference / timeInterval) - 1;
					System.out.println(String.format("Interpolated using %s from %d to %d", middleInterpolator.getClass(), formerTime, latterTime));
					T[] missing = middleInterpolator.interpolate(formerData, latterData, missingNumber);
					Collections.addAll(interpolated, missing);
				}
				interpolated.add(latterData);
				formerTime = latterTime;
				formerData = latterData;
			}
			Long lastTime = result.lastKey();
			if (!lastTime.equals(timeBound.getB())) {
				int missingNumber = (int) ((timeBound.getB() - lastTime) / timeInterval);
				System.out.println(String.format("Interpolated using %s from %d to %d", borderInterpolator.getClass(), lastTime, timeBound.getB()));
				T[] ending = borderInterpolator.interpolate(result.get(lastTime),
						null, missingNumber);
				Collections.addAll(interpolated, ending);
			}
			return interpolated;
		}
	}
	
	private Sheet extractAndClearSheet(Workbook workbook, String sheetName) {
		Sheet sheet = workbook.getSheet(sheetName);
		SheetEditor.removeContinuousRows(sheet, 1);
		return sheet;
	}

}
