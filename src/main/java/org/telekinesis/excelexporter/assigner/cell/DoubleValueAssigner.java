package org.telekinesis.excelexporter.assigner.cell;

import org.apache.poi.ss.usermodel.Cell;

public abstract class DoubleValueAssigner extends CellValueAssigner<Double>
{

    @Override
    protected void assignValueToCell(Cell cell, Double value)
    {
	cell.setCellValue(value);
    }

}
