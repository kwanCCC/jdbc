package org.doraemon.treasure.grammer.parser;

import org.doraemon.treasure.grammer.parser.exceptions.DruidSQLException;
import org.doraemon.treasure.grammer.parser.impl.LexerErrorListener;
import org.doraemon.treasure.grammer.parser.impl.ThrowingExceptionErrorStrategy;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang3.StringUtils;

import org.doraemon.treasure.grammer.parser.DruidQuery.ProgContext;
import org.doraemon.treasure.grammer.parser.impl.DruidQueryVisitor;
import org.doraemon.treasure.grammer.parser.impl.SyntaxErrorListener;

public class ParserEngine {

    public static Query parse(String sql) {
        if (StringUtils.isBlank(sql))
            throw new DruidSQLException("blank sql is not allowed");


        ANTLRInputStream input = new ANTLRInputStream(sql);

        ThrowingExceptionErrorStrategy errorHandler = new ThrowingExceptionErrorStrategy();

        DruidLexer lexer = new DruidLexer(input);
        lexer.removeErrorListeners();
        // 分词的错误处理
        lexer.addErrorListener(new LexerErrorListener());

        CommonTokenStream token = new CommonTokenStream(lexer);

        DruidQuery query = new DruidQuery(token);
        // 解析的错误处理
        query.setErrorHandler(errorHandler);
        query.removeErrorListeners();
        query.addErrorListener(new SyntaxErrorListener());


        ProgContext progTree = query.prog();
        DruidQueryVisitor visitor = new DruidQueryVisitor();
        if (visitor.visit(progTree)) {
            return visitor.getQuery();
        }
        throw new DruidSQLException("SQL parser error!!!");
    }
}
