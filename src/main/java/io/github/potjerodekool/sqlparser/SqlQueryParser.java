package io.github.potjerodekool.sqlparser;

import io.github.potjerodekool.sqlparser.ast.Statement;
import io.github.potjerodekool.sqlparser.visitor.InsertVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.List;

public class SqlQueryParser {

    public List<Statement> parse(final String query) {
        final var lexer = new SqlLexer(CharStreams.fromString(query));
        final var parser = new SqlParser(new CommonTokenStream(lexer));
        final var sql = parser.sql();
        return (List<Statement>) sql.accept(new InsertVisitor());
    }
}
