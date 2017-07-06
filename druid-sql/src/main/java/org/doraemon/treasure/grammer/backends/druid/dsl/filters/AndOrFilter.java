package org.doraemon.treasure.grammer.backends.druid.dsl.filters;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class AndOrFilter implements Filter {
    private String       type;
    private List<Filter> fields;
}
