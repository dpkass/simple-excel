package dpkass.simpleexcel;

import dpkass.simpleexcel.core.SimpleColumn;
import dpkass.simpleexcel.core.SimpleColumns;
import dpkass.simpleexcel.core.SimpleRow;
import dpkass.simpleexcel.core.SimpleRows;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public interface SheetInjector {

  void singleRow(SimpleRow row);

  void singleColumn(SimpleColumn col);

  void rows(SimpleRows rows);

  void columns(SimpleColumns cols);

  void save(OutputStream os);
}
