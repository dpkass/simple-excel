package dpkass.excel;

import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;

import dpkass.datacontainers.SimpleCell;
import dpkass.datacontainers.SimpleColumn;
import dpkass.datacontainers.SimpleColumns;
import dpkass.datacontainers.SimpleRange;
import dpkass.datacontainers.SimpleRow;
import dpkass.datacontainers.SimpleRows;
import dpkass.Extractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class SheetExtractor implements Extractor {

  Sheet sheet;

  Row currentRow;
  Cell currentCell;

  int currentRowIndex;
  int currentColumnIndex;
  int currentMaxIndex;

  boolean vertical;

  public SheetExtractor(Sheet sheet) {
    this.sheet = sheet;
  }

  @Override
  public SimpleRow singleRow(int index) {
    vertical = false;
    currentRowIndex = index;
    return (SimpleRow) singleRange();
  }

  @Override
  public SimpleColumn singleColumn(int index) {
    vertical = true;
    currentColumnIndex = index;
    return (SimpleColumn) singleRange();
  }

  public SimpleRange singleRange() {
    nextRange();

    SimpleRange result = vertical ? new SimpleColumn() : new SimpleRow();
    currentMaxIndex = vertical ? sheet.getLastRowNum() : currentRow.getLastCellNum();

    while (nextCell())
      result.add(toSimpleCell(currentCell));

    return result;
  }

  @Override
  public SimpleRows rows(int from, int to) {
    SimpleRows result = new SimpleRows();

    currentRowIndex = from;
    while (currentRowIndex <= to)
      result.add((SimpleRow) singleRange());

    return result;
  }

  @Override
  public SimpleColumns columns(int from, int to) {
    SimpleColumns result = new SimpleColumns();

    currentColumnIndex = from;
    while (currentColumnIndex <= to)
      result.add((SimpleColumn) singleRange());

    return result;
  }

  @Override
  public SimpleRows allAsRows() {
    vertical = false;
    return rows(sheet.getFirstRowNum(), sheet.getLastRowNum());
  }

  @Override
  public SimpleColumns allAsColumns() {
    vertical = false;
    return columns(sheet.getFirstRowNum(), sheet.getLastRowNum());
  }


  // cell transform
  private SimpleCell toSimpleCell(Cell cell) {
    return switch (cell.getCellType()) {
      case STRING -> new SimpleCell(cell.getStringCellValue(), STRING);
      case NUMERIC -> new SimpleCell(cell.getNumericCellValue(), NUMERIC);
      default -> SimpleCell.blank();
    };
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
    do {
      currentRow = sheet.getRow(currentRowIndex++);
    } while (currentRow == null);
    currentColumnIndex = 0;
  }

  private void nextLogicalRow() {
    currentRow = sheet.getRow(currentRowIndex);
    if (currentRow == null)
      sheet.createRow(currentRowIndex);
    currentRowIndex++;
  }

  // column
  private void nextColumn() {
    currentRowIndex = 0;
    while (isEmptyColumn())
      nextLogicalColumn();
  }

  private void nextLogicalColumn() {
    currentColumnIndex++;
  }

  private boolean isEmptyColumn() {
    int maxRow = sheet.getLastRowNum();
    for (int i = 0; i < maxRow; i++) {
      nextCellDown();
      if (currentCell.getCellType() != BLANK)
        return false;
    }
    return true;
  }

  // cell
  private boolean nextCell() {
    if (vertical)
      return nextCellDown();
    else
      return nextCellRight();
  }

  private boolean nextCellRight() {
    currentCell = currentRow.getCell(currentColumnIndex++, CREATE_NULL_AS_BLANK);
    return currentColumnIndex < currentMaxIndex;
  }

  private boolean nextCellDown() {
    nextLogicalRow();
    currentCell = currentRow.getCell(currentColumnIndex, CREATE_NULL_AS_BLANK);
    return currentRowIndex < currentMaxIndex;
  }
}
