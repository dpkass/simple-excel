package dpkass.simpleexcel.core.base;

import dpkass.simpleexcel.core.SimpleMatrix;
import dpkass.simpleexcel.core.SimpleRange;
import java.util.ArrayList;
import java.util.Collection;

public class BaseSimpleMatrix<T extends SimpleRange> extends ArrayList<T>
    implements SimpleMatrix<T> {

  public BaseSimpleMatrix() {
    super();
  }

  public BaseSimpleMatrix(Collection<? extends T> c) {
    super(c);
  }

  public BaseSimpleMatrix<T> subList(int from, int to) {
    return new BaseSimpleMatrix<T>(super.subList(from, to));
  }
}
