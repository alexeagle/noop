package noop.grammar

/**
 * Thrown by our parser when ANTLR reports some errors while lexing or parsing.
 *
 * @author alexeagle@google.com (Alex Eagle)
 */

class ParseException(val message: String) extends RuntimeException(message) {

}