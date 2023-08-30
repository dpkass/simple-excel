package dpkass;

import dpkass.datacontainers.SimpleColumn;
import dpkass.datacontainers.SimpleColumns;
import dpkass.datacontainers.SimpleRow;
import dpkass.datacontainers.SimpleRows;

public interface Extractor {

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
