package excelexporter.ganglia.interpolation;

public class DiskInterpolator extends GangliaTupleInterpolator<Double> {

	public DiskInterpolator(InterpolationType type) {
		super(type);
	}

	@Override
	public Double[] interpolate(Double former, Double latter, int missingNumbers) {
		Double[] interpolated = innerInterpolator.interpolate(former, latter, missingNumbers);
		return interpolated;
	}

}
