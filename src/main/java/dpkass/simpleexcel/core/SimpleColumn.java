package dpkass.simpleexcel.core;

import dpkass.simpleexcel.core.base.BaseSimpleRange;

public interface SimpleColumn extends SimpleRange {

  static SimpleRange empty() {
    return new BaseSimpleRange();
  }
}
