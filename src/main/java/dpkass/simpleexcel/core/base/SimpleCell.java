package dpkass.simpleexcel.core.base;

import static org.apache.poi.ss.usermodel.CellType.BLANK;

import org.apache.poi.ss.usermodel.CellType;

public record SimpleCell(Object value, CellType type) {

  public static SimpleCell blank() {
    return new SimpleCell("", BLANK);
  }
}
