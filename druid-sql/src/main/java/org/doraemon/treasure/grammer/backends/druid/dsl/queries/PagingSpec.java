package org.doraemon.treasure.grammer.backends.druid.dsl.queries;

import lombok.*;

import java.util.HashMap;
import java.util.Map;


@Builder
@EqualsAndHashCode
public class PagingSpec {

  private Boolean fromNext;

  @NonNull
  @Getter
  private Integer threshold;

  private Map<String, Integer> pagingIdentifiers;

  public Boolean getFromNext() {
    return true;
  }


  public Map<String, Integer> getPagingIdentifiers() {
    return new HashMap<String, Integer>();
  }

}
