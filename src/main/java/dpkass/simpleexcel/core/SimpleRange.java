package dpkass.simpleexcel.core;

import dpkass.simpleexcel.core.base.BaseSimpleRange;
import dpkass.simpleexcel.core.base.SimpleCell;
import java.util.List;

public interface SimpleRange extends List<SimpleCell> {

  static SimpleRange empty() {
    return new BaseSimpleRange();
  }
}
