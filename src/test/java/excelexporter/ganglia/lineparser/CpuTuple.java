package excelexporter.ganglia.lineparser;

public class CpuTuple {
	private final double user;
	private final double system;
	private final double wio;
	private final double idle;

	public CpuTuple(double user, double system, double wio, double idle) {
		this.user = user;
		this.system = system;
		this.wio = wio;
		this.idle = idle;
	}

	public double getUser() {
		return user;
	}

	public double getSystem() {
		return system;
	}

	public double getWio() {
		return wio;
	}

	public double getIdle() {
		return idle;
	}

	@Override
	public String toString() {
		return "CpuTuple [user=" + user + ", system=" + system + ", wio=" + wio
				+ ", idle=" + idle + "]";
	}

}
