package tc.oc.pgm.variables.event;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import tc.oc.pgm.api.match.Match;
import tc.oc.pgm.api.match.event.MatchEvent;
import tc.oc.pgm.filters.Filterable;
import tc.oc.pgm.variables.Variable;

public class VariableChangeEvent extends MatchEvent {
  private static final HandlerList HANDLERS = new HandlerList();

  private final @NotNull Variable<?> variable;
  private final @NotNull Filterable<?> context;
  private final double oldValue;
  private final double newValue;

  public VariableChangeEvent(
      @NotNull Match match,
      @NotNull Variable<?> variable,
      @NotNull Filterable<?> context,
      double oldValue,
      double newValue) {
    super(match);
    this.variable = variable;
    this.context = context;
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  public @NotNull Variable<?> getVariable() {
    return variable;
  }

  public @NotNull Filterable<?> getContext() {
    return context;
  }

  public double getOldValue() {
    return oldValue;
  }

  public double getNewValue() {
    return newValue;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS;
  }

  public static @NotNull HandlerList getHandlerList() {
    return HANDLERS;
  }
}
