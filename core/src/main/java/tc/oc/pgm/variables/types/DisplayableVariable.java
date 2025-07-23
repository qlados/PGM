package tc.oc.pgm.variables.types;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import tc.oc.pgm.api.match.Match;
import tc.oc.pgm.features.StateHolder;
import tc.oc.pgm.filters.FilterMatchModule;
import tc.oc.pgm.filters.Filterable;
import tc.oc.pgm.variables.Variable;
import tc.oc.pgm.variables.event.VariableChangeEvent;

public class DisplayableVariable<T extends Filterable<?>> extends AbstractVariable<T>
    implements StateHolder<DisplayableVariable<T>.Values>, Variable.Displayable<T> {

  private final double def;
  private Component scoreboardFormat;
  private boolean showOnScoreboard;

  private final boolean showValue;

  public DisplayableVariable(
      Class<T> scope, double def, Boolean isDisplayable, Component format, Boolean showValue) {
    super(scope);
    this.def = def;
    this.showOnScoreboard = isDisplayable;
    this.scoreboardFormat = format;
    this.showValue = showValue;
  }

  @Override
  public void load(Match match) {
    // Register the state for this variable when loaded into a match
    match.getFeatureContext().registerState(this, new Values());
  }

  @Override
  public void setValue(Filterable<?> context, double value) {
    // Fire VariableChangeEvent for Variables displayed on Scoreboard
    double oldValue = getValue(context);
    setValueImpl(getAncestor(context), value);
    double newValue = getValue(context);
    // Fire event only if value changed and variable is displayed
    if (oldValue != newValue && showOnScoreboard) {
      context
          .getMatch()
          .callEvent(
              new VariableChangeEvent(context.getMatch(), this, context, oldValue, newValue));
    }
  }

  @Override
  protected double getValueImpl(T obj) {
    return obj.state(this).value;
  }

  @Override
  protected void setValueImpl(T obj, double value) {
    obj.state(this).setValue(value);

    // For performance reasons, let's avoid launching an event for every variable change
    obj.moduleRequire(FilterMatchModule.class).invalidate(obj);
  }

  public class Values {
    private double value;

    public Values() {
      this.value = def;
    }

    protected void setValue(double newValue) {
      this.value = newValue;
    }
  }

  @Override
  public Component getScoreboardFormat(Filterable<?> context) {
    if (!showValue) return scoreboardFormat;
    return scoreboardFormat
        .append(Component.space())
        .append(Component.text(getValue(context), NamedTextColor.GRAY));
  }

  @Override
  public void setFormat(Component scoreboardFormat) {
    this.scoreboardFormat = scoreboardFormat;
  }

  @Override
  public boolean isShowOnScoreboard() {
    return showOnScoreboard;
  }

  @Override
  public void setShowOnScoreboard(Boolean newShowOnScoreboard) {
    this.showOnScoreboard = newShowOnScoreboard;
  }

  @Override
  public boolean isShowValue() {
    return showValue;
  }
}
