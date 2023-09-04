package dpkass.simpleexcel.core.base;

import dpkass.simpleexcel.core.SimpleRow;
import dpkass.simpleexcel.core.SimpleRows;
import java.util.Collection;

public class BaseSimpleRowMatrix extends BaseSimpleMatrix<SimpleRow> implements SimpleRows {

  public BaseSimpleRowMatrix() {
    super();
  }

  public BaseSimpleRowMatrix(Collection<? extends SimpleRow> c) {
    super(c);
  }

  public BaseSimpleRowMatrix subList(int from, int to) {
    return new BaseSimpleRowMatrix(super.subList(from, to));
  }
}
