package org.telekinesis.excelexporter.assigner.cell;

public enum CellValueType
{
    STRING(new StringValueSetter()),
    DOUBLE(new DoubleValueSetter()),
    FORMULA(new FormulaValueSetter());
    
    private final CellValueSetter<?> cellValueSetter;
    
    private CellValueType(CellValueSetter<?> cellValueSetter)
    {
	this.cellValueSetter = cellValueSetter;
    }

    public CellValueSetter<?> getCellValueSetter()
    {
        return cellValueSetter;
    }
    
}
