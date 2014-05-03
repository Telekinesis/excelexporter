package org.telekinesis.excelexporter.assigner.cell;

import org.apache.poi.ss.usermodel.Cell;

public class StringValueSetter extends CellValueSetter<String>
{

    @Override
    protected String parse(String valueString)
    {
	return valueString;
    }

    @Override
    protected void set(Cell cell, String value)
    {
	cell.setCellValue(value);
    }

}
