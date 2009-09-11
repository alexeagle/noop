package noop.model

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

object Modifier extends Enumeration {
  type Modifier = Value;
  val native = Value("native");
  val mutable = Value("mutable");
  val delegate = Value("delegate");
}