package org.telekinesis.excelexporter.assigner.cell;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;

public class FormulaValueSetter extends CellValueSetter<String>
{

    @Override
    protected String parse(String valueString)
    {
	return valueString;
    }

    @Override
    protected void set(Cell cell, String value)
    {
	cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
	cell.setCellFormula(value);
    }

}
