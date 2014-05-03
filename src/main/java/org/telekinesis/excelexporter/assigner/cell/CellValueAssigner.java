package org.telekinesis.excelexporter.assigner.cell;

import org.apache.poi.ss.usermodel.Cell;

public abstract class CellValueAssigner<T> implements CellAssigner
{

    @Override
    public void assign(Cell cell, int rowIndex, int columnIndex){
	T value = create(rowIndex, columnIndex);
	assignValueToCell(cell, value);
    }
    
    protected abstract T create(int rowIndex, int columnIndex);

    protected abstract void assignValueToCell(Cell cell, T value);

}
