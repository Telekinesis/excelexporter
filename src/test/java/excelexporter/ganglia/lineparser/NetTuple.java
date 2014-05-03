package excelexporter.ganglia.lineparser;

public class NetTuple {
	private final double bytesIn;
	private final double bytesOut;

	public NetTuple(double bytesIn, double bytesOut) {
		this.bytesIn = bytesIn;
		this.bytesOut = bytesOut;
	}

	public double getBytesIn() {
		return bytesIn;
	}

	public double getBytesOut() {
		return bytesOut;
	}

	@Override
	public String toString() {
		return "NetTuple [bytesIn=" + bytesIn + ", bytesOut=" + bytesOut + "]";
	}

}
