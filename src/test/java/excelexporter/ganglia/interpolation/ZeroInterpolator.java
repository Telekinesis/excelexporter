package excelexporter.ganglia.interpolation;

import java.util.Arrays;

public class ZeroInterpolator implements Interpolator<Double> {

	@Override
	public Double[] interpolate(Double former, Double latter,
			int missingNumbers) {
		Double[] result = new Double[missingNumbers];
		Arrays.fill(result, 0D);
		return result;
	}

}
