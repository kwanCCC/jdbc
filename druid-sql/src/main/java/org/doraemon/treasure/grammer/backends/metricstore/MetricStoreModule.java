package org.doraemon.treasure.grammer.backends.metricstore;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.ConstColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.MaxColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.MinColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.SumColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.arithmetric.AddColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.arithmetric.DivColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.arithmetric.MinusColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.arithmetric.MultiplyColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.others.AliasColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.others.CountColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.others.RefColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.DoubleColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.FloatColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.IPColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.IntColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.LongColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.StringColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.AndFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.CompareFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.EqualFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.OrFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.limits.Limit;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.limits.OrderByLimit;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.limits.SimpleLimit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.queries.GroupByQuery;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.queries.Query;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.queries.SelectQuery;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.queries.TimeSeriesQuery;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.queries.TopNQuery;

public class MetricStoreModule extends SimpleModule {

    public MetricStoreModule() {
        super();
        this.addSerializer(ConstColumn.class, new ConstColumnSerializer());
        setMixInAnnotation(Column.class, ColumnsMixin.class);
        setMixInAnnotation(Filter.class, FiltersMixin.class);
        setMixInAnnotation(Query.class, QueriesMixin.class);
        setMixInAnnotation(Limit.class, LimitsMixin.class);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
    @JsonSubTypes(value = {@JsonSubTypes.Type(name = "double", value = DoubleColumn.class),
                                  @JsonSubTypes.Type(name = "float", value = FloatColumn.class),
                                  @JsonSubTypes.Type(name = "long", value = LongColumn.class),
                                  @JsonSubTypes.Type(name = "int", value = IntColumn.class),
                                  @JsonSubTypes.Type(name = "string", value = StringColumn.class),
                                  @JsonSubTypes.Type(name = "ip", value = IPColumn.class),
                                  @JsonSubTypes.Type(name = "add", value = AddColumn.class),
                                  @JsonSubTypes.Type(name = "div", value = DivColumn.class),
                                  @JsonSubTypes.Type(name = "multiply", value = MultiplyColumn.class),
                                  @JsonSubTypes.Type(name = "minus", value = MinusColumn.class),
                                  @JsonSubTypes.Type(name = "min", value = MinColumn.class),
                                  @JsonSubTypes.Type(name = "max", value = MaxColumn.class),
                                  @JsonSubTypes.Type(name = "sum", value = SumColumn.class),
                                  @JsonSubTypes.Type(name = "const", value = ConstColumn.class),
                                  @JsonSubTypes.Type(name = "count", value = CountColumn.class),
                                  @JsonSubTypes.Type(name = "alias", value = AliasColumn.class),
                                  @JsonSubTypes.Type(name = "ref", value = RefColumn.class)})
    public interface ColumnsMixin {

    }


    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
    @JsonSubTypes(value = {@JsonSubTypes.Type(name = "equal", value = EqualFilter.class),
                                  @JsonSubTypes.Type(name = "or", value = OrFilter.class),
                                  @JsonSubTypes.Type(name = "and", value = AndFilter.class),
                                  @JsonSubTypes.Type(name="compare",value= CompareFilter.class)})
    public interface FiltersMixin {
    }


    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
    @JsonSubTypes(value = {@JsonSubTypes.Type(name = "timeseries", value = TimeSeriesQuery.class),
                                  @JsonSubTypes.Type(name = "topn", value = TopNQuery.class),
                                  @JsonSubTypes.Type(name = "select", value = SelectQuery.class),
                                  @JsonSubTypes.Type(name = "groupby", value = GroupByQuery.class)})
    public interface QueriesMixin {
    }


    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
    // 反序列化（setter）时忽略 type 属性， 序列化（getter）时不忽略
    @JsonIgnoreProperties(value = {"type"}, allowGetters = true)
    @JsonSubTypes(value = {@JsonSubTypes.Type(name = "orderby", value = OrderByLimit.class),
                                  @JsonSubTypes.Type(name = "simple", value = SimpleLimit.class)})
    public interface LimitsMixin {
    }

}
