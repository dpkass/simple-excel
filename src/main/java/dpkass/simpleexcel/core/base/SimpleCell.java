package dpkass.simpleexcel.core.base;

import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

import org.apache.poi.ss.usermodel.CellType;

public record SimpleCell(Object value, CellType type) {

  public static SimpleCell blank() {
    return new SimpleCell("", BLANK);
  }

  public static SimpleCell of(String string) {
    return "".equals(string) ? SimpleCell.blank() : new SimpleCell(string, STRING);
  }

  public static SimpleCell of(Number number) {
    return new SimpleCell(number, NUMERIC);
  }
}
