package excelexporter;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.telekinesis.commonclasses.entity.Pair;

import excelexporter.ganglia.GangliaExcelExporter;
import excelexporter.ganglia.GangliaTupleProcessor;
import excelexporter.ganglia.ProfileExcelExporter;
import excelexporter.ganglia.filler.CpuContentFiller;
import excelexporter.ganglia.filler.DiskContentFiller;
import excelexporter.ganglia.filler.MemContentFiller;
import excelexporter.ganglia.filler.NetContentFiller;
import excelexporter.ganglia.importing.CsvLoader;
import excelexporter.ganglia.importing.LocalCsvLoader;
import excelexporter.ganglia.interpolation.CpuInterpolator;
import excelexporter.ganglia.interpolation.DiskInterpolator;
import excelexporter.ganglia.interpolation.InterpolationType;
import excelexporter.ganglia.interpolation.MemInterpolator;
import excelexporter.ganglia.interpolation.NetInterpolator;
import excelexporter.ganglia.lineparser.CpuFileLineParser;
import excelexporter.ganglia.lineparser.CpuTuple;
import excelexporter.ganglia.lineparser.DiskAverageLineParser;
import excelexporter.ganglia.lineparser.MemFileLineParser;
import excelexporter.ganglia.lineparser.MemTuple;
import excelexporter.ganglia.lineparser.NetFileLineParser;
import excelexporter.ganglia.lineparser.NetTuple;

public class TestExportGangliaData {
	private static final String templatePath = "src/test/resources/gangliaGraph.xls";
	private static final String exportPath = "src/test/resources/updatedGangliaCharts.xls";
	private static final long timeInterval = 15L;
	private static final long memorySize = 858872080L;
	
//	private static final String host = "9.186.107.15";
//	private static final String username = "biadmin4";
//	private static final String password = "biadmin4";
	private static final String cpuCsv = "src/test/resources/ggCpu.csv";
	private static final String memCsv = "src/test/resources/ggMems.csv";
	private static final String netCsv = "src/test/resources/ggNets.csv";
	private static final String diskCsv = "src/test/resources/ggDiskStateSummaries.csv";
	
//	private static final String host = "192.168.167.130";
//	private static final String username = "minoru";
//	private static final String password = "65365737";
//	private static final String cpuCsv = "/home/minoru/test/ggCpu.csv";
//	private static final String memCsv = "/home/minoru/test/ggMems.csv";
//	private static final String netCsv = "/home/minoru/test/ggNets.csv";
//	private static final String diskCsv = "/home/minoru/test/ggDiskStateSummaries.csv";
//	
	@Ignore@Test
	public void testParseCpuProfile() throws IOException {
//		CsvLoader reader = new SshCsvLoader(host, username, password);
		CsvLoader reader = new LocalCsvLoader();
		
		GangliaExcelExporter exporter = new GangliaExcelExporter(
				new CpuContentFiller(reader), 
				new MemContentFiller(reader, memorySize), 
				new NetContentFiller(reader), 
				new DiskContentFiller(reader, new DiskAverageLineParser())
				);
		
		exporter.export(templatePath, exportPath, cpuCsv, memCsv, netCsv,
				diskCsv);
	}
	
	@Test
	public void testNewExporter() throws IOException {
		CsvLoader csvLoader = new LocalCsvLoader();
		GangliaTupleProcessor<CpuTuple> cpuParser = new GangliaTupleProcessor<CpuTuple>(new CpuFileLineParser(), new CpuInterpolator(InterpolationType.UNIFORM), new CpuInterpolator(InterpolationType.UNIFORM));
		GangliaTupleProcessor<MemTuple> memParser = new GangliaTupleProcessor<MemTuple>(new MemFileLineParser(), new MemInterpolator(InterpolationType.UNIFORM), new MemInterpolator(InterpolationType.UNIFORM));
		GangliaTupleProcessor<NetTuple> netParser = new GangliaTupleProcessor<NetTuple>(new NetFileLineParser(), new NetInterpolator(InterpolationType.UNIFORM), new NetInterpolator(InterpolationType.UNIFORM));
		GangliaTupleProcessor<Double> diskParser = new GangliaTupleProcessor<Double>(new DiskAverageLineParser(), new DiskInterpolator(InterpolationType.UNIFORM), new DiskInterpolator(InterpolationType.UNIFORM));
		ProfileExcelExporter exporter = new ProfileExcelExporter(
				memorySize, 
				timeInterval,
				csvLoader, 
				cpuParser,
				memParser,
				netParser,
				diskParser
				);
		exporter.export(templatePath, exportPath, cpuCsv, memCsv, netCsv, diskCsv);
	}

}
