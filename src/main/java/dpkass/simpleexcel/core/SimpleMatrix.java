package dpkass.simpleexcel.core;

import java.util.List;

public interface SimpleMatrix<T extends SimpleRange> extends List<T> {

  SimpleMatrix<T> subList(int from, int to);
}
