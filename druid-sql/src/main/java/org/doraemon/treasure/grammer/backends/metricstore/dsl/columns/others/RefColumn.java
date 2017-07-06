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
public class RefColumn implements Column {
    private String type = "ref";
    private String name;
    
    public RefColumn(String name) {
        this.name = name;
    }
}
