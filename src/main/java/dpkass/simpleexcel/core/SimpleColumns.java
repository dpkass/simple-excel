package dpkass.simpleexcel.core;

import dpkass.simpleexcel.core.base.BaseSimpleColumnMatrix;

public interface SimpleColumns extends SimpleMatrix<SimpleColumn> {

  static SimpleColumns empty() {
    return new BaseSimpleColumnMatrix();
  }
}
