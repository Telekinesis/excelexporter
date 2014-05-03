package org.telekinesis.excelexporter.assigner.cell;

import org.apache.poi.ss.usermodel.Cell;

public interface CellAssigner
{
    public void assign(Cell cell, int rowIndex, int columnIndex);
}
