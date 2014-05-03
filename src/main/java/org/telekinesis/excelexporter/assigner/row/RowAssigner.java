package org.telekinesis.excelexporter.assigner.row;

import org.apache.poi.ss.usermodel.Row;

public interface RowAssigner {
	public void assign(Row row, int rowIndex);
}
