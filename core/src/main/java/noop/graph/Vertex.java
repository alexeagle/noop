package noop.graph;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.UUID;

/**
 * A vertex in the semantic graph, which allows serializable references to language elements.
 * Each vertex is addressable by the library that contains it, and the index of the vertex within the library.
 * Edges have one Vertex as their source and one as the destination.
 * 
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Vertex {
  public final UUID libraryUid;
  public final int index;
  public static final Vertex NONE = new Vertex(null, -1);

  public Vertex(UUID libraryUid, int index) {
    this.libraryUid = libraryUid;
    this.index = index;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(libraryUid).append(index).toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }
    Vertex rhs = (Vertex) obj;
    return new EqualsBuilder()
        .append(libraryUid, rhs.libraryUid)
        .append(index, rhs.index)
        .isEquals();
  }

  @Override
  public String toString() {
    if (this == NONE) {
      return "NO_VERTEX";
    }
    return index + "(" + libraryUid.toString() + ")";
  }
}
