package org.doraemon.treasure.grammer.backends.druid.dsl;

public enum EnumOrderByDirection {
    ascending, descending;
    public static EnumOrderByDirection get(boolean isDesc) {
        return isDesc ? descending : ascending;
    }
}
