package dpkass.simpleexcel.excel;

import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;

import dpkass.simpleexcel.SheetExtractor;
import dpkass.simpleexcel.core.SimpleColumn;
import dpkass.simpleexcel.core.SimpleColumns;
import dpkass.simpleexcel.core.SimpleRange;
import dpkass.simpleexcel.core.SimpleRow;
import dpkass.simpleexcel.core.SimpleRows;
import dpkass.simpleexcel.core.base.SimpleCell;
import java.util.stream.StreamSupport;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelSheetExtractor implements SheetExtractor {

  final Sheet sheet;

  Row currentRow;
  Cell currentCell;

  int currentRowIndex;
  int currentColumnIndex;

  final int maxRowIndex;
  final int maxColumnIndex;

  boolean vertical;

  public ExcelSheetExtractor(Sheet sheet) {
    this.sheet = sheet;
    this.maxRowIndex = sheet.getLastRowNum();
    this.maxColumnIndex = StreamSupport.stream(sheet.spliterator(), false)
                                       .mapToInt(Row::getLastCellNum)
                                       .max()
                                       .orElse(0);
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

    SimpleRange result = vertical ? SimpleColumn.empty() : SimpleRow.empty();

    while (nextCell())
      result.add(toSimpleCell(currentCell));

    return result;
  }

  @Override
  public SimpleRows rows(int from, int to) {
    SimpleRows result = SimpleRows.empty();

    currentRowIndex = from;
    while (currentRowIndex <= to)
      result.add((SimpleRow) singleRange());

    return result;
  }

  @Override
  public SimpleColumns columns(int from, int to) {
    SimpleColumns result = SimpleColumns.empty();

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

  public SimpleCell toSimpleCell(Cell cell) {
    return switch (cell.getCellType()) {
      case STRING -> SimpleCell.of(cell.getStringCellValue());
      case NUMERIC -> SimpleCell.of(cell.getNumericCellValue());
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
    return currentColumnIndex <= maxColumnIndex;
  }

  private boolean nextCellDown() {
    nextLogicalRow();
    currentCell = currentRow.getCell(currentColumnIndex, CREATE_NULL_AS_BLANK);
    return currentRowIndex <= maxRowIndex;
  }
}
