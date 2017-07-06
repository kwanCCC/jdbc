package org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators;

import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

public interface IPostAggregator extends IAggregator
{


  /**
   * get aggregations used by Druid.io; not postAggregations
   *
   * @return
   */
  @JsonIgnore
  Set<IAggregator> getAggregators();
}
