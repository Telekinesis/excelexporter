package excelexporter.ganglia.interpolation;

import excelexporter.ganglia.lineparser.NetTuple;

public class NetInterpolator extends GangliaTupleInterpolator<NetTuple> {

	public NetInterpolator(InterpolationType type) {
		super(type);
	}

	@Override
	public NetTuple[] interpolate(NetTuple former, NetTuple latter,
			int missingNumbers) {
		if(former == null)
			former = new NetTuple(Double.NaN, Double.NaN);
		if(latter == null)
			latter = new NetTuple(Double.NaN, Double.NaN);
		Double[] interpolatedBytesIn = innerInterpolator.interpolate(former.getBytesIn(), latter.getBytesOut(), missingNumbers);
		Double[] interpolatedBytesOut = innerInterpolator.interpolate(former.getBytesOut(), latter.getBytesOut(), missingNumbers);
		NetTuple[] result = new NetTuple[missingNumbers];
		for (int i = 0; i < missingNumbers; i++) {
			result[i] = new NetTuple(interpolatedBytesIn[i], interpolatedBytesOut[i]);
		}
		return result;
	}

}
