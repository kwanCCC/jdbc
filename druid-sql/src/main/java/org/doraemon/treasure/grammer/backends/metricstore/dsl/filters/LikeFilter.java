package org.doraemon.treasure.grammer.backends.metricstore.dsl.filters;


import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LikeFilter implements Filter {
    private final String type = "like";
    private Column column;
    private Column value;
}
