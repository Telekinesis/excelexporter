package excelexporter.ganglia.interpolation;

public enum InterpolationType {
	ZERO(new ZeroInterpolator()),
	UNIFORM(new UniformInterpolator()), 
	LINEAR(new LinearInterpolator());
	
	private final Interpolator<Double> interpolator;

	private InterpolationType(Interpolator<Double> interpolator) {
		this.interpolator = interpolator;
	}

	public Interpolator<Double> getInterpolator() {
		return interpolator;
	}
	
}
