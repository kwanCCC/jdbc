package org.doraemon.treasure.grammer.backends.metricstore.format.columns;

import static org.hamcrest.Matchers.is;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.MinColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.SumColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.DoubleColumn;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.math.MaxOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.MinOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.SumOperand;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.MaxColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.LongColumn;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;

@RunWith(Parameterized.class)
public class AggregationColumnFormatTest {

    private AggregationColumnFormat format;

    private Operand operand;
    private Column  column;

    public AggregationColumnFormatTest(Operand operand, Column column) {
        this.operand = operand;
        this.column = column;
    }

    @Before
    public void setUp() {
        format = new AggregationColumnFormat();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        String table = "table1";
        String field = "field1";
        return Arrays.asList(new Object[][] {
                {new SumOperand("doubleSum", new NameOperand(table, field)), new SumColumn(new DoubleColumn(field))},
                {new SumOperand("longSum", new NameOperand(table, field)), new SumColumn(new LongColumn(field))},
                {new MinOperand("longMin", new NameOperand(table, field)), new MinColumn(new LongColumn(field))},
                {new MinOperand("doubleMin", new NameOperand(table, field)), new MinColumn(new DoubleColumn(field))},
                {new MaxOperand("longMax", new NameOperand(table, field)), new MaxColumn(new LongColumn(field))},
                {new MaxOperand("doubleMax", new NameOperand(table, field)), new MaxColumn(new DoubleColumn(field))},
        });
    }

    @Test
    public void testFormatWithRefColumn() throws SQLException {
        List<String> aliasNames = Collections.emptyList();
        Column formatedColumn = format.formatWithRefColumn(aliasNames, operand);
        Assert.assertThat(formatedColumn, is(column));
    }

}
