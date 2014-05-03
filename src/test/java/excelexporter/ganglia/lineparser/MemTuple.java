package excelexporter.ganglia.lineparser;

public class MemTuple {
	private final double buffer;
	private final double cached;
	private final double free;
	private final double shared;

	public MemTuple(double buffer, double cached, double free, double shared) {
		super();
		this.buffer = buffer;
		this.cached = cached;
		this.free = free;
		this.shared = shared;
	}

	public double getBuffer() {
		return buffer;
	}

	public double getCached() {
		return cached;
	}

	public double getFree() {
		return free;
	}

	public double getShared() {
		return shared;
	}

	@Override
	public String toString() {
		return "MemTuple [buffer=" + buffer + ", cached=" + cached + ", free="
				+ free + ", shared=" + shared + "]";
	}

}
