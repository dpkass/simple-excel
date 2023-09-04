package dpkass.simpleexcel.core;

import dpkass.simpleexcel.core.base.BaseSimpleRange;

public interface SimpleRow extends SimpleRange {

  static SimpleRange empty() {
    return new BaseSimpleRange();
  }
}
