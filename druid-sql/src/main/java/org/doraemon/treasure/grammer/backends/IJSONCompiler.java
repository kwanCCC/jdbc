package org.doraemon.treasure.grammer.backends;

import org.doraemon.treasure.grammer.parser.Query;

import java.sql.SQLException;

/**
 * interface for compiling query object to JSON
 *
 * @author zhxiaog
 */
public interface IJSONCompiler
{
  String compile(Query query) throws SQLException;

  boolean canCompile(Query query) throws SQLException;
}
