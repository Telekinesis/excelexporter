package excelexporter.ganglia.interpolation;


public interface Interpolator<T> {
	public T[] interpolate(T former, T latter, int missingNumbers);
}
