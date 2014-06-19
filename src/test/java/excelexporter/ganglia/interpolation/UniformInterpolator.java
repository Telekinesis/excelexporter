package excelexporter.ganglia.interpolation;

import java.util.Arrays;

public class UniformInterpolator implements Interpolator<Double> {

	@Override
	public Double[] interpolate(Double former, Double latter, int missingNumbers) {
		Double interpolated = former;
		if (former == null || former.equals(Double.NaN))
			interpolated = latter;
		if (interpolated == null || interpolated.equals(Double.NaN))
			interpolated = 0D;
		Double[] result = new Double[missingNumbers];
		Arrays.fill(result, interpolated);
		return result;
	}

}
