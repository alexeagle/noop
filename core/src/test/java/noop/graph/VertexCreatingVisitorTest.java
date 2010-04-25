package noop.graph;

import noop.model.Clazz;
import noop.model.Library;
import noop.model.Method;
import noop.model.Project;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class VertexCreatingVisitorTest {
  @Test
  public void shouldCreateVerticesForEveryElementUnderAProject() {
    Project p = new Project("example", "p", "MIT license");
    UUID uid = UUID.randomUUID();
    Library l = new Library(uid, "l");
    Clazz c = new Clazz("c");
    Method m = new Method("m");
    Library l2 = new Library(UUID.randomUUID(), "l2");

    p.addLibrary(l);
    p.addLibrary(l2);
    l.addClazz(c);
    c.addBlock(m);

    p.accept(new VertexCreatingVisitor());

    assertEquals(l, l.getElements().get(0));
    assertEquals(c, l.getElements().get(1));
    assertEquals(m, l.getElements().get(2));
    assertEquals(new Vertex(uid, 0), l.vertex);
    assertEquals(new Vertex(uid, 1), c.vertex);
    assertEquals(new Vertex(uid, 2), m.vertex);
    assertFalse(l2.vertex.libraryUid.equals(uid));
  }
}
