package noop.model;

import noop.graph.ModelVisitor;
import org.joda.time.Instant;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Comment extends LanguageElement<Comment> {
  public final String text;
  public final String user;
  public final Instant timestamp;

  public Comment(String text, String user) {
    this.text = text;
    this.user = user;
    this.timestamp = new Instant();
  }

  @Override
  public void accept(ModelVisitor v) {
    v.visit(this);
  }
}
