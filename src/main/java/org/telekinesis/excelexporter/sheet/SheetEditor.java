package org.telekinesis.excelexporter.sheet;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.telekinesis.excelexporter.assigner.cell.CellAssigner;
import org.telekinesis.excelexporter.assigner.row.RowAssigner;

public class SheetEditor {
	public static void replaceCell(Sheet sheet, int rowIndex, int columnIndex,
			String content) {
		Cell cell = getCell(sheet, rowIndex, columnIndex);
		cell.setCellValue(content);
	}

	public static Row getRow(Sheet sheet, int rowIndex) {
		Row row = sheet.getRow(rowIndex);
		if (row == null)
			row = sheet.createRow(rowIndex);
		return row;
	}

	public static Cell getCell(Row row, int columnIndex) {
		Cell cell = row.getCell(columnIndex);
		if (cell == null)
			cell = row.createCell(columnIndex);
		return cell;
	}

	public static Cell getCell(Sheet sheet, int rowIndex, int columnIndex) {
		Row row = getRow(sheet, rowIndex);
		return getCell(row, columnIndex);
	}

	public static void removeContinuousRows(Sheet sheet, int startIndex) {
		int count = countContinuousRows(sheet, startIndex);
		removeRows(sheet, startIndex, count);
	}

	public static int countContinuousRows(Sheet sheet, int startIndex) {
		Row row = sheet.getRow(startIndex);
		int currentIndex = startIndex;
		int count = 0;
		while (row != null) {
			count++;
			row = sheet.getRow(++currentIndex);
		}
		return count;
	}

	// Remove does not make the lower rows shrink upwards
	public static void removeRows(Sheet sheet, int startRowIndex, int count) {
		for (int i = 0; i < count; i++) {
			int index = startRowIndex + i;
			Row row = sheet.getRow(index);
			sheet.removeRow(row);
		}
	}

	public static void fillColumns(Sheet sheet,
			Map<Integer, CellAssigner> columnIndexedAssigners, int fromRow,
			int toRow) {
		for (int columnIndex : columnIndexedAssigners.keySet()) {
			CellAssigner assigner = columnIndexedAssigners.get(columnIndex);
			for (int rowIndex = fromRow; rowIndex < toRow; rowIndex++) {
				Cell cellToBeFilled = getCell(sheet, rowIndex, columnIndex);
				assigner.assign(cellToBeFilled, rowIndex, columnIndex);
			}
		}
	}

	public static void fillRows(RowAssigner assigner, Sheet sheet, int fromRow,
			int toRow) {
		for (int i = fromRow; i <= toRow; i++) {
			Row row = getRow(sheet, i);
			assigner.assign(row, i);
		}
	}
}
