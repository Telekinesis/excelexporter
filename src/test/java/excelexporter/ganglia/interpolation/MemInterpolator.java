package excelexporter.ganglia.interpolation;

import excelexporter.ganglia.lineparser.MemTuple;

public class MemInterpolator extends GangliaTupleInterpolator<MemTuple> {

	public MemInterpolator(InterpolationType type) {
		super(type);
	}

	@Override
	public MemTuple[] interpolate(MemTuple former, MemTuple latter,
			int missingNumbers) {
		if(former == null)
			former = new MemTuple(Double.NaN, Double.NaN, Double.NaN, Double.NaN);
		if(latter == null)
			latter = new MemTuple(Double.NaN, Double.NaN, Double.NaN, Double.NaN);
		Double[] interpolatedBuffer = innerInterpolator.interpolate(former.getBuffer(), latter.getBuffer(), missingNumbers);
		Double[] interpolatedCached = innerInterpolator.interpolate(former.getCached(), latter.getCached(), missingNumbers);
		Double[] interpolatedFree = innerInterpolator.interpolate(former.getFree(), latter.getFree(), missingNumbers);
		Double[] interpolatedShared = innerInterpolator.interpolate(former.getShared(), latter.getShared(), missingNumbers);
		MemTuple[] result = new MemTuple[missingNumbers];
		for (int i = 0; i < missingNumbers; i++) {
			result[i] = new MemTuple(interpolatedBuffer[i], interpolatedCached[i], interpolatedFree[i], interpolatedShared[i]);	
		}
		return result;
	}

}
