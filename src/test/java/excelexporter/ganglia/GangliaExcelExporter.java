package excelexporter.ganglia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.telekinesis.excelexporter.sheet.SheetEditor;

import excelexporter.ganglia.filler.CpuContentFiller;
import excelexporter.ganglia.filler.DiskContentFiller;
import excelexporter.ganglia.filler.MemContentFiller;
import excelexporter.ganglia.filler.NetContentFiller;

public class GangliaExcelExporter {
	private static final String CPU_PAGE = "ggCpu";
	private static final String MEM_PAGE = "ggMem";
	private static final String NET_PAGE = "ggNet";
	private static final String DISK_PAGE = "ggDiskState";
	public static final long gangliaInterval = 15L;
	private final CpuContentFiller cpuFiller;
	private final MemContentFiller memFiller;
	private final NetContentFiller netFiller;
	private final DiskContentFiller diskFiller;

	public GangliaExcelExporter(CpuContentFiller cpuFiller,
			MemContentFiller memFiller, NetContentFiller netFiller,
			DiskContentFiller diskFiller) {
		this.cpuFiller = cpuFiller;
		this.memFiller = memFiller;
		this.netFiller = netFiller;
		this.diskFiller = diskFiller;
	}

	public void export(String templatePath, String exportPath, String cpuPath,
			String memPath, String netPath, String diskPath) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File(
				templatePath)));
		modifyCpuPage(workbook, cpuPath);
		modifyMemPage(workbook, memPath);
		modifyNetPage(workbook, netPath);
		modifyDiskPage(workbook, diskPath);
		workbook.write(new FileOutputStream(exportPath));
	}

	private void modifyCpuPage(HSSFWorkbook workbook, String cpuPath) {
		Sheet sheet = extractAndClearSheet(workbook, CPU_PAGE);
		cpuFiller.fill(sheet, cpuPath);
	}

	private void modifyMemPage(Workbook workbook, String memPath) {
		Sheet sheet = extractAndClearSheet(workbook, MEM_PAGE);
		memFiller.fill(sheet, memPath);
	}

	private void modifyNetPage(Workbook workbook, String netPath) {
		Sheet sheet = extractAndClearSheet(workbook, NET_PAGE);
		netFiller.fill(sheet, netPath);
	}

	private void modifyDiskPage(Workbook workbook, String diskPath) {
		Sheet sheet = extractAndClearSheet(workbook, DISK_PAGE);
		diskFiller.fill(sheet, diskPath);
	}

	private Sheet extractAndClearSheet(Workbook workbook, String sheetName) {
		Sheet sheet = workbook.getSheet(sheetName);
		SheetEditor.removeContinuousRows(sheet, 1);
		return sheet;
	}
}
