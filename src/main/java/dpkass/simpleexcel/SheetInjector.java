package dpkass.simpleexcel;

import dpkass.simpleexcel.core.SimpleColumn;
import dpkass.simpleexcel.core.SimpleColumns;
import dpkass.simpleexcel.core.SimpleRow;
import dpkass.simpleexcel.core.SimpleRows;

public interface SheetInjector {

  void singleRow(SimpleRow row);

  void singleColumn(SimpleColumn col);

  void rows(SimpleRows rows);

  void columns(SimpleColumns cols);
}
