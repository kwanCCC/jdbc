package org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.others;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class CountColumn implements Column {
    @Override
    public String getType() {
        return "count";
    }
}
