package dpkass.simpleexcel.extractor;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unchecked"}) // can't use parameters, because of variable types
public class AttributeInfo {

  public enum DataType {STR, INT, FLOAT, BOOL}

  private final String name;
  private final DataType type;
  private int length = 1;
  private boolean optional = true;


  private final Predicate defaultSingletonValidator = o -> optional();

  private Predicate singletonValidator = defaultSingletonValidator;
  private Function singletonCreator = Function.identity();
  private Supplier singletonNullHandler = () -> null;
  private Function collectionCreator;


  private final String error;

  public AttributeInfo(String name, DataType type) {
    this.name = name;
    this.type = type;
    this.error = "Invalid " + name;
  }

  public static AttributeInfo of(String name, DataType type) {
    return new AttributeInfo(name, type);
  }

  public AttributeInfo setLength(int length) {
    this.length = length;
    return this;
  }

  public AttributeInfo addSingletonValidator(Predicate singletonValidator) {
    this.singletonValidator = this.singletonValidator.or(singletonValidator);
    return this;
  }

  public AttributeInfo notNull() {
    this.optional = false;
    return this;
  }

  public AttributeInfo setSingletonCreator(Function singletonCreator) {
    this.singletonCreator = singletonCreator;
    return this;
  }

  public AttributeInfo setSingletonNullHandler(Supplier singletonNullHandler) {
    this.singletonNullHandler = singletonNullHandler;
    return this;
  }

  public AttributeInfo setCollectionCreator(Function collectionCreator) {
    this.collectionCreator = collectionCreator;
    return this;
  }

  public String name() {
    return name;
  }

  public DataType type() {
    return type;
  }

  public int length() {
    return length;
  }

  public String error() {
    return error;
  }

  private boolean optional() {
    return optional;
  }

  public boolean validate(Object o) {
    return singletonValidator.test(o);
  }

  public Object create(Object o) {
    return o == null ? singletonNullHandler.get() : singletonCreator.apply(o);
  }

  public Object processCollection(Object o) {
    if (collectionCreator != null)
      return collectionCreator.apply(o);
    else
      throw new IllegalStateException("No collection creator given for %s".formatted(name));
  }
}
