package org.telekinesis.excelexporter.assigner.cell;

import org.apache.poi.ss.usermodel.Cell;

public class DoubleValueSetter extends CellValueSetter<Double>
{

    @Override
    protected Double parse(String valueString)
    {
	return Double.parseDouble(valueString);
    }

    @Override
    protected void set(Cell cell, Double value)
    {
	cell.setCellValue(value);
    }

}
