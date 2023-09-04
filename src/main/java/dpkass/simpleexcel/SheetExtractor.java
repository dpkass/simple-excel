package dpkass.simpleexcel;

import dpkass.simpleexcel.core.SimpleColumn;
import dpkass.simpleexcel.core.SimpleColumns;
import dpkass.simpleexcel.core.SimpleRow;
import dpkass.simpleexcel.core.SimpleRows;

public interface SheetExtractor {

  SimpleRow singleRow(int index);

  SimpleColumn singleColumn(int index);

  SimpleRows rows(int from, int to);

  SimpleColumns columns(int from, int to);

  SimpleRows allAsRows();

  SimpleColumns allAsColumns();

  /**
   * Alias for allAsRows
   */
  default SimpleRows all() {
    return allAsRows();
  }
}
