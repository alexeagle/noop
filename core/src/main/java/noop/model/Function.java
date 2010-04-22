package noop.model;

import com.google.common.collect.Lists;
import noop.graph.ModelVisitor;

import java.util.List;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Function extends Block<Function> {

  protected final List<Parameter> parameters = Lists.newArrayList();

  public Function(String name) {
    super(name);
  }

  @Override
  public boolean adoptChild(LanguageElement child) {
    if (child instanceof Parameter) {
      parameters.add((Parameter) child);
      return true;
    }
    return super.adoptChild(child);
  }

  @Override
  public void accept(ModelVisitor v) {
    v.visit(this);
    for (Parameter parameter : parameters) {
      v.enter(parameter);
      parameter.accept(v);
      v.leave(parameter);
    }
    super.accept(v);
  }
}
