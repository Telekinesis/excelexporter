package excelexporter;

import java.io.IOException;

import org.junit.Test;
import org.telekinesis.commonclasses.io.ByLineReader;
import org.telekinesis.commonclasses.io.SshLineFileReader;

import com.jcraft.jsch.JSchException;

import excelexporter.ganglia.GangliaTupleProcessor;
import excelexporter.ganglia.ProfileExcelExporter;
import excelexporter.ganglia.importing.CsvLoader;
import excelexporter.ganglia.importing.SshCsvLoader;
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
import excelexporter.ganglia.log.LogFileFinder;
import excelexporter.ganglia.log.MapLogParser;
import excelexporter.ganglia.log.ReduceLogParser;
import excelexporter.ganglia.log.TaskTimeCostExtractor;

public class TestExportGangliaData {
	private static final String templatePath = "src/test/resources/gangliaChart.xls";
	private static final String exportPath = "src/test/resources/updatedGangliaCharts.xls";
	private static final long timeInterval = 15L;
	private static final long memorySize = 858872080L;
	
	private static final String host = "9.186.107.15";
	private static final String username = "biadmin4";
	private static final String password = "biadmin4";
	private static final String cpuCsv = "/home/biadmin4/qyj/ggCpu.csv";
	private static final String memCsv = "/home/biadmin4/qyj/ggMems.csv";
	private static final String netCsv = "/home/biadmin4/qyj/ggNets.csv";
	private static final String diskCsv = "/home/biadmin4/qyj/ggDiskStateSummaries.csv";
	private static final InterpolationType innerInterpolation = InterpolationType.LINEAR;
	private static final InterpolationType borderInterpolation = InterpolationType.UNIFORM;
	private static final String logRoot = "/home/home/data/bi21d/logs/userlogs";
	private static final String jobID = "job_201405080623_0001";
	private static final String jobName = "smartreport";
	private static final boolean isSlowStart = false;
	
	@Test
	public void testNewExporter() throws IOException, JSchException {
		CsvLoader csvLoader = new SshCsvLoader(host, username, password);
		GangliaTupleProcessor<CpuTuple> cpuParser = new GangliaTupleProcessor<CpuTuple>(new CpuFileLineParser(), new CpuInterpolator(innerInterpolation), new CpuInterpolator(borderInterpolation));
		GangliaTupleProcessor<MemTuple> memParser = new GangliaTupleProcessor<MemTuple>(new MemFileLineParser(), new MemInterpolator(innerInterpolation), new MemInterpolator(borderInterpolation));
		GangliaTupleProcessor<NetTuple> netParser = new GangliaTupleProcessor<NetTuple>(new NetFileLineParser(), new NetInterpolator(innerInterpolation), new NetInterpolator(borderInterpolation));
		GangliaTupleProcessor<Double> diskParser = new GangliaTupleProcessor<Double>(new DiskAverageLineParser(), new DiskInterpolator(innerInterpolation), new DiskInterpolator(borderInterpolation));
		LogFileFinder finder = new LogFileFinder(host, username, password);
		ByLineReader<String> reader = new SshLineFileReader(host, username, password);
		MapLogParser mapParser = new MapLogParser(reader);
		ReduceLogParser reduceParser = new ReduceLogParser(reader);
		TaskTimeCostExtractor timeCostExtractor = new TaskTimeCostExtractor(finder, mapParser, reduceParser, isSlowStart);
		ProfileExcelExporter exporter = new ProfileExcelExporter(
				memorySize, 
				timeInterval,
				jobName,
				csvLoader, 
				cpuParser,
				memParser,
				netParser,
				diskParser,
				timeCostExtractor
				);
		exporter.export(templatePath, exportPath, cpuCsv, memCsv, netCsv, diskCsv, logRoot, jobID);
	}

}
