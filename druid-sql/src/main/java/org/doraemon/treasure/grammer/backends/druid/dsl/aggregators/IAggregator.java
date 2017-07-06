package org.doraemon.treasure.grammer.backends.druid.dsl.aggregators;


import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IAggregator {
    String getType();

    String getName();

    // TODO: remove this
    void setName(String value);

    @JsonIgnore
    boolean isPostAggregator();
}
