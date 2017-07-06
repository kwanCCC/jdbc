package org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.others;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AliasColumn implements Column {
    private String type = "alias";
    private Column column;
    private String alias;
    
    public AliasColumn(Column column, String alias) {
        this.column = column;
        this.alias = alias;
    }
}
