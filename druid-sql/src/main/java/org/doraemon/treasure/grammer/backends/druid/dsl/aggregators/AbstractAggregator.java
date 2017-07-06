package org.doraemon.treasure.grammer.backends.druid.dsl.aggregators;


public abstract class AbstractAggregator implements IAggregator {

    public boolean isPostAggregator() {
        return false;
    }
}
