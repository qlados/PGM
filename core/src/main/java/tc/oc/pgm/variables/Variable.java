package tc.oc.pgm.variables;

import java.util.Collection;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import tc.oc.pgm.api.feature.FeatureDefinition;
import tc.oc.pgm.api.match.Match;
import tc.oc.pgm.filters.Filterable;

public interface Variable<T extends Filterable<?>> extends FeatureDefinition {

  double getValue(Filterable<?> context);

  void setValue(Filterable<?> context, double value);

  Class<T> getScope();

  default boolean isDynamic() {
    return false;
  }

  default boolean isIndexed() {
    return false;
  }

  default boolean isReadonly() {
    return false;
  }

  default boolean isExclusive() {
    return false;
  }

  default boolean isDisplayable() {
    return false;
  }

  default void load(Match match) {}

  /**
   * Variable that has indexing, ie: arrays Note: you must always check {@link #isIndexed} to know
   * if it actually is an indexed variable or not.
   *
   * @param <T> The filter type of this variable
   */
  interface Indexed<T extends Filterable<?>> extends Variable<T> {

    @Override
    default boolean isIndexed() {
      return true;
    }

    double getValue(Filterable<?> context, int idx);

    void setValue(Filterable<?> context, int idx, double value);

    int size();
  }

  interface Exclusive<T extends Filterable<?>> extends Variable<T> {
    @Override
    default boolean isExclusive() {
      return getCardinality() != null;
    }

    Integer getCardinality();

    Optional<T> getHolder(Filterable<?> context);

    Collection<T> getHolders(Filterable<?> context);
  }

  interface Displayable<T extends Filterable<?>> extends Variable<T> {

    @Override
    default boolean isDisplayable() {
      return true;
    }

    Component getScoreboardFormat(Filterable<?> context);

    void setFormat(Component scoreboardFormat);

    void setShowOnScoreboard(Boolean newShowOnScoreboard);

    default boolean isShowOnScoreboard() {
      return false;
    }

    default Component getScoreboardFormat() {
      return null;
    }

    boolean isShowValue();
  }
}
