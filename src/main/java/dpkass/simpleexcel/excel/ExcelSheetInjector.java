package dpkass.simpleexcel.excel;

import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;

import dpkass.simpleexcel.SheetInjector;
import dpkass.simpleexcel.core.SimpleColumn;
import dpkass.simpleexcel.core.SimpleColumns;
import dpkass.simpleexcel.core.SimpleRange;
import dpkass.simpleexcel.core.SimpleRow;
import dpkass.simpleexcel.core.SimpleRows;
import dpkass.simpleexcel.core.base.SimpleCell;
import java.io.IOException;
import java.io.OutputStream;
import javax.naming.OperationNotSupportedException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelSheetInjector implements SheetInjector {

  final Sheet sheet;

  Row currentRow;
  Cell currentCell;

  int currentRowIndex;
  int currentColumnIndex;

  boolean vertical;

  public ExcelSheetInjector(Sheet sheet) {
    this.sheet = sheet;
  }

  @Override
  public void singleRow(SimpleRow row) {
    vertical = false;
    singleRange(row);
  }

  @Override
  public void singleColumn(SimpleColumn col) {
    vertical = true;
    singleRange(col);
  }

  private void singleRange(SimpleRange range) {
    range.forEach(this::setNextCellValue);
    nextRange();
  }

  private void setNextCellValue(SimpleCell cell) {
    nextCell();
    switch (cell.type()) {
      case STRING -> currentCell.setCellValue((String) cell.value());
      case NUMERIC -> currentCell.setCellValue((double) cell.value());
      case BLANK -> currentCell.setCellValue("");
      default -> throw new RuntimeException(new OperationNotSupportedException());
    }
  }

  @Override
  public void rows(SimpleRows rows) {
    vertical = false;
    rows.forEach(this::singleRange);
  }

  @Override
  public void columns(SimpleColumns cols) {
    vertical = true;
    cols.forEach(this::singleRange);
  }

  // save
  @Override
  public void save(OutputStream os) {
    try {
      sheet.getWorkbook().write(os);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  // next
  // range
  private void nextRange() {
    if (vertical)
      nextColumn();
    else
      nextRow();
  }

  private void nextLogicalRange() {
    if (vertical)
      nextLogicalColumn();
    else
      nextLogicalRow();
  }

  // row
  private void nextRow() {
    currentColumnIndex = 0;
    nextLogicalRow();
  }

  private void nextLogicalRow() {
    currentRowIndex++;
    currentRow = sheet.getRow(currentRowIndex);
    if (currentRow == null)
      currentRow = sheet.createRow(currentRowIndex);
  }

  // column
  private void nextColumn() {
    currentRowIndex = 0;
    nextLogicalColumn();
  }

  private void nextLogicalColumn() {
    currentColumnIndex++;
  }


  // cell
  private void nextCell() {
    if (vertical)
      nextCellDown();
    else
      nextCellRight();
  }

  private void nextCellRight() {
    currentCell = currentRow.getCell(currentColumnIndex++, CREATE_NULL_AS_BLANK);
  }

  private void nextCellDown() {
    nextLogicalRow();
    currentCell = currentRow.getCell(currentColumnIndex, CREATE_NULL_AS_BLANK);
  }
}
