package dpkass.simpleexcel.core.base;

import dpkass.simpleexcel.core.SimpleColumn;
import dpkass.simpleexcel.core.SimpleColumns;
import java.util.Collection;

public class BaseSimpleColumnMatrix extends BaseSimpleMatrix<SimpleColumn>
    implements SimpleColumns {

  public BaseSimpleColumnMatrix() {
    super();
  }

  public BaseSimpleColumnMatrix(Collection<? extends SimpleColumn> c) {
    super(c);
  }

  public BaseSimpleColumnMatrix subList(int from, int to) {
    return new BaseSimpleColumnMatrix(super.subList(from, to));
  }
}
