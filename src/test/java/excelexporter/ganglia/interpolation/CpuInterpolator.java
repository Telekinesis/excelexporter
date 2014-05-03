package excelexporter.ganglia.interpolation;

import excelexporter.ganglia.lineparser.CpuTuple;

public class CpuInterpolator extends GangliaTupleInterpolator<CpuTuple> {

	public CpuInterpolator(InterpolationType type) {
		super(type);
	}

	@Override
	public CpuTuple[] interpolate(CpuTuple former, CpuTuple latter,
			int missingNumbers) {
		if(former == null)
			former = new CpuTuple(Double.NaN, Double.NaN, Double.NaN, Double.NaN);
		if(latter == null)
			latter = new CpuTuple(Double.NaN, Double.NaN, Double.NaN, Double.NaN);
		Double[] interpolatedUser = innerInterpolator.interpolate(former.getUser(), latter.getUser(), missingNumbers);
		Double[] interpolatedSystem = innerInterpolator.interpolate(former.getSystem(), latter.getSystem(), missingNumbers);
		Double[] interpolatedWio = innerInterpolator.interpolate(former.getWio(), latter.getWio(), missingNumbers);
		Double[] interpolatedIdle = innerInterpolator.interpolate(former.getIdle(), latter.getIdle(), missingNumbers);
		CpuTuple[] interpolated = new CpuTuple[missingNumbers];
		for (int i = 0; i < missingNumbers; i++) {
			interpolated[i] = new CpuTuple(interpolatedUser[i], interpolatedSystem[i], interpolatedWio[i], interpolatedIdle[i]);
		}
		return interpolated;
	}

}
