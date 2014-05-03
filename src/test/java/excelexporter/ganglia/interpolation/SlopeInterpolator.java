package excelexporter.ganglia.interpolation;

public class SlopeInterpolator implements Interpolator<Double> {

	@Override
	public Double[] interpolate(Double former, Double latter, int missingNumbers) {
		if(former == null || former == Double.NaN)
			former = 0D;
		if(latter == null || latter == Double.NaN)
			latter = 0D;
		int slots = missingNumbers + 1;
		double interval = (latter - former) / slots;
		Double[] result = new Double[missingNumbers];
		double nowValue = former + interval;
		for (int i = 0; i < missingNumbers; i++) {
			result[i] = nowValue;
			nowValue += interval;
		}
		return result;
	}

}
