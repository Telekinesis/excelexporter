package org.telekinesis.excelexporter.assigner.cell;

import org.apache.poi.ss.usermodel.Cell;

public abstract class StringValueAssigner extends CellValueAssigner<String>
{
    @Override
    protected void assignValueToCell(Cell cell, String value)
    {
        cell.setCellValue(value);
    }
}
