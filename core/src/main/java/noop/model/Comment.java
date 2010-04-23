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

  public Comment(String text, String user, Instant timestamp) {
    this.text = text;
    this.user = user;
    this.timestamp = timestamp;
  }

  @Override
  public void accept(ModelVisitor v) {
    v.visit(this);
  }
}
