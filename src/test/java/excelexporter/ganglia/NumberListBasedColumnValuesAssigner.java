package excelexporter.ganglia;

import java.util.List;

import org.telekinesis.excelexporter.assigner.cell.DoubleValueAssigner;

public class NumberListBasedColumnValuesAssigner extends DoubleValueAssigner
{
    private final List<? extends Number> numbers;
    private final int startRow;
    
    public NumberListBasedColumnValuesAssigner(List<? extends Number> numbers, int startRow)
    {
	this.numbers = numbers;
	this.startRow = startRow;
    }

    @Override
    protected Double create(int rowIndex, int columnIndex)
    {
	int index = rowIndex - startRow;
	return numbers.get(index).doubleValue();
    }

}
