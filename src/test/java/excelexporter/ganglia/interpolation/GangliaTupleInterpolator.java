package excelexporter.ganglia.interpolation;

public abstract class GangliaTupleInterpolator<T> implements Interpolator<T>{
	protected final Interpolator<Double> innerInterpolator;

	public GangliaTupleInterpolator(InterpolationType type) {
		if(type == InterpolationType.ZERO)
			innerInterpolator = new ZeroInterpolator();
		else if(type == InterpolationType.UNIFORM)
			innerInterpolator = new UniformInterpolator();
		else
			innerInterpolator = new LinearInterpolator();
	}
	
}
