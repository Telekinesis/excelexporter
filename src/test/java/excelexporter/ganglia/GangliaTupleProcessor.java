package excelexporter.ganglia;

import excelexporter.ganglia.interpolation.GangliaTupleInterpolator;
import excelexporter.ganglia.lineparser.GangliaCsvLineParser;

public class GangliaTupleProcessor<T> {
	private final GangliaCsvLineParser<T> parser;
	private final GangliaTupleInterpolator<T> middleInterpolator;
	private final GangliaTupleInterpolator<T> borderInterpolator;

	public GangliaTupleProcessor(GangliaCsvLineParser<T> parser,
			GangliaTupleInterpolator<T> middleInterpolator,
			GangliaTupleInterpolator<T> borderInterpolator) {
		this.parser = parser;
		this.middleInterpolator = middleInterpolator;
		this.borderInterpolator = borderInterpolator;
	}

	public GangliaCsvLineParser<T> getParser() {
		return parser;
	}

	public GangliaTupleInterpolator<T> getMiddleInterpolator() {
		return middleInterpolator;
	}

	public GangliaTupleInterpolator<T> getBorderInterpolator() {
		return borderInterpolator;
	}

}
