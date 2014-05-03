package org.telekinesis.excelexporter.assigner.cell;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;

public class ListTranslatingAssigner implements CellAssigner
{
    private final List<String> list;
    private final int indexOffset;
    private final CellValueSetter<?> cellValueSetter;
    
    public ListTranslatingAssigner(CellValueType valueType, List<String> list, int indexOffset)
    {
	this.list = list;
	this.indexOffset = indexOffset;
	this.cellValueSetter = valueType.getCellValueSetter();
    }
    
    @Override
    public void assign(Cell cell, int rowIndex, int columnIndex){
	int listIndex = rowIndex - indexOffset;
	cellValueSetter.parseAndSet(cell, list.get(listIndex));
    }
    
}
