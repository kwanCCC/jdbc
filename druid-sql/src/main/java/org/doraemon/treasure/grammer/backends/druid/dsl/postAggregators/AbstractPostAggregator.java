package org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators;


public abstract class AbstractPostAggregator implements IPostAggregator {

    public boolean isPostAggregator() {
        return true;
    }

}
