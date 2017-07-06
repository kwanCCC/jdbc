package org.doraemon.treasure.grammer.mid.model.granularities;

import org.doraemon.treasure.grammer.mid.model.Granularity;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.joda.time.Period;

@Data
@AllArgsConstructor
@Deprecated
public class SimpleGranularity implements Granularity
{

    private final String name;

    @Override
    public long getValue() {
        return Period.parse(name).toStandardDuration().getMillis();
    }

    @Override
    public int hashCode() {
        return name.toUpperCase().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof SimpleGranularity)) return false;
        SimpleGranularity that = (SimpleGranularity) o;
        return this.name != null && this.name.equalsIgnoreCase(that.name);
    }
}
