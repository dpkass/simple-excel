package dpkass.simpleexcel.extractor.exceptions;

public record InvalidCell(String address, String value, String comment) {

  public static InvalidCell of(Object address, Object value, Object comment) {
    return new InvalidCell(String.valueOf(address), String.valueOf(value), String.valueOf(comment));
  }

  public String format(String format) {
    return format.formatted(address, value, comment);
  }
}
