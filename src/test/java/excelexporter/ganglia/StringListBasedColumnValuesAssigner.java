package excelexporter.ganglia;

import java.util.List;

import org.telekinesis.excelexporter.assigner.cell.StringValueAssigner;

public class StringListBasedColumnValuesAssigner extends StringValueAssigner
{
    private final List<String> contents;
    private final int startRow;

    public StringListBasedColumnValuesAssigner(List<String> contents, int startRow)
    {
	this.contents = contents;
	this.startRow = startRow;
    }

    @Override
    protected String create(int rowIndex, int columnIndex)
    {
	int index = rowIndex - startRow;
	return contents.get(index);
    }

}
