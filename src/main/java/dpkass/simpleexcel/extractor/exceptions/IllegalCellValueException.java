package dpkass.simpleexcel.extractor.exceptions;

import java.util.Collection;
import java.util.stream.Collectors;

public class IllegalCellValueException extends RuntimeException {

  private static final String default_format = "%-10s %-40s %s";

  public IllegalCellValueException(String sheet, Collection<InvalidCell> cells) {
    this(sheet, cells, default_format);
  }

  public IllegalCellValueException(String sheet, Collection<InvalidCell> cells, String format) {
    super("""
                
        Illegal cell value encountered in sheet "%s".
        %s
        """.formatted(sheet, format)
           .formatted("Address", "Cell value", "Comment")
        + toMessage(cells, format));
  }

  private static String toMessage(Collection<InvalidCell> cells, String format) {
    return cells.stream().map(ic -> ic.format(format)).collect(Collectors.joining("\n"));
  }
}
