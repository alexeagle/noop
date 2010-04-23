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

  public void addParameter(Parameter parameter) {
    this.parameters.add(parameter);
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
