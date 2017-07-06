package org.doraemon.treasure.grammer.backends.druid.compiler;

import org.doraemon.treasure.grammer.backends.checker.SelectChecker;
import org.doraemon.treasure.grammer.backends.druid.dsl.dimensions.IDimensionSpec;
import org.doraemon.treasure.grammer.backends.druid.util.CompilerUtils;
import org.doraemon.treasure.grammer.backends.druid.dsl.queries.IQueryObject;
import org.doraemon.treasure.grammer.backends.druid.dsl.queries.PagingSpec;
import org.doraemon.treasure.grammer.backends.druid.dsl.queries.SelectQuery;
import org.doraemon.treasure.grammer.mid.model.LimitOperand;
import org.doraemon.treasure.grammer.parser.Query;

import java.util.List;

public class SelectCompiler extends IDruidQueryCompiler
{
    private final int DEFAULT_LIMIT_SIZE = 5000;

    @Override
    public boolean canCompile(Query query) {
         return SelectChecker.isConditionMet(query);
    }

    @Override
    protected IQueryObject compile(CompilerContext context) {
        LimitOperand limit = context.getQuery().getLimit();

      int threshold = limit == null ? DEFAULT_LIMIT_SIZE : limit.getMaxSize();

        //FIXME 如何区分metric and dimensions?
        final List<IDimensionSpec> columns = CompilerUtils.getSelectDims(context);
        return SelectQuery.builder()
                .dataSource(context.getDataSource())
                .intervals(context.getInterval())
                .filter(context.getFilter())
                .dimensions(columns)
                .pagingSpec(PagingSpec.builder().threshold(threshold).build())
                .build();
    }

}
