package dpkass.simpleexcel.core;

import dpkass.simpleexcel.core.base.BaseSimpleRowMatrix;

public interface SimpleRows extends SimpleMatrix<SimpleRow> {

  static SimpleRows empty() {
    return new BaseSimpleRowMatrix();
  }
}
