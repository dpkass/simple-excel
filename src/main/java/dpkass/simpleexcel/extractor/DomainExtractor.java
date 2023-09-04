package dpkass.simpleexcel.extractor;

import static dpkass.simpleexcel.extractor.AttributeInfo.DataType.BOOL;
import static dpkass.simpleexcel.extractor.AttributeInfo.DataType.FLOAT;
import static dpkass.simpleexcel.extractor.AttributeInfo.DataType.INT;
import static dpkass.simpleexcel.extractor.AttributeInfo.DataType.STR;
import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

import dpkass.simpleexcel.core.SimpleRow;
import dpkass.simpleexcel.core.SimpleRows;
import dpkass.simpleexcel.core.base.SimpleCell;
import dpkass.simpleexcel.extractor.exceptions.InvalidCell;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class DomainExtractor<T> {

  private final List<AttributeInfo> rowInfos;
  protected final List<InvalidCell> invalidCells = new ArrayList<>();

  private final Constructor<T> constructor = constructor();

  private int rowIndex = 0, columnIndex;

  protected abstract Constructor<T> constructor();

  public DomainExtractor(List<AttributeInfo> rowInfos) {
    this.rowInfos = rowInfos;
  }

  protected List<T> extractRows(SimpleRows rows) {
    return rows.stream().map(this::extractRow).map(this::construct).collect(Collectors.toList());
  }

  protected T construct(List<Object> args) {
    try {
      Object[] varArgs = args.toArray(new Object[0]);
      return constructor.newInstance(varArgs);
    } catch (ReflectiveOperationException e) {
      System.err.println("Couldn't instantiate");
      return null;
    }
  }

  protected List<Object> extractRow(SimpleRow row) {
    rowIndex++;
    columnIndex = 0;
    List<Object> args = new ArrayList<>();
    for (AttributeInfo info : rowInfos) {
      if (info.length() > 1)
        args.add(list(row.subList(columnIndex, columnIndex + info.length()), info));
      else if (info.length() == 1)
        args.add(singleton(row.get(columnIndex), info));
      else
        args.add(list(row.subList(columnIndex, row.size()), info));

      columnIndex += info.length();
    }

    return args;
  }

  // redundant casts for type safety later
  @SuppressWarnings("RedundantCast")
  private Object singleton(SimpleCell cell, AttributeInfo info) {
    Object value;
    if (cell.type() == STRING && info.type() == STR)
      value = (String) cell.value();
    else if (cell.type() == NUMERIC && info.type() == INT)
      value = (long) (double) cell.value();
    else if (cell.type() == NUMERIC && info.type() == FLOAT)
      value = (double) cell.value();
    else if (cell.type() == STRING && info.type() == BOOL) {
      value = (String) cell.value();
      value = value.equals("Yes");
    } else if (cell.type() == BLANK)
      value = null;
    else {
      invalidInput(cell, info);
      return null;
    }

    if (!info.validate(Optional.ofNullable(value))) {
      invalidInput(cell, info);
      return null;
    }

    return info.create(value);
  }

  private Object list(List<SimpleCell> cells, AttributeInfo info) {
    return info.processCollection(
        cells.stream().map(c -> singleton(c, info)).filter(Objects::nonNull).toList());
  }

  private void invalidInput(SimpleCell cell, AttributeInfo info) {
    invalidCells.add(InvalidCell.of("%d,%d".formatted(columnIndex + 1, rowIndex + 1), cell.value(),
        info.error()));
  }

  public List<InvalidCell> invalidCells() {
    return invalidCells;
  }
}
