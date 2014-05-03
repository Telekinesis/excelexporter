package org.telekinesis.excelexporter.assigner.cell;

import org.apache.poi.ss.usermodel.Cell;

public abstract class CellValueSetter<T>
{
    public void parseAndSet(Cell cell, String valueString){
	T value = parse(valueString);
	set(cell, value);
    }
    
    protected abstract T parse(String valueString);
    
    protected abstract void set(Cell cell, T value);
    
}
