package org.doraemon.treasure.grammer.mid.model.granularities;

import org.doraemon.treasure.grammer.mid.model.Granularity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class DurationGranularity implements Granularity
{

    private final String type = "duration";
    /**
     * milliseconds
     */
    private final long duration;
    private final long origin;
    
    @Override
    public long getValue() {
        return duration;
    }
}
