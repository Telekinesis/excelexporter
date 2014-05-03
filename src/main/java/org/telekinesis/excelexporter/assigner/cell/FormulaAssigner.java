package org.telekinesis.excelexporter.assigner.cell;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;

public abstract class FormulaAssigner extends CellValueAssigner<String>
{

    @Override
    protected void assignValueToCell(Cell cell, String value)
    {
	cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
	cell.setCellFormula(value);
    }

}
