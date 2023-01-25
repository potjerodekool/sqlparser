package io.github.potjerodekool.sqlparser.visitor;

import io.github.potjerodekool.sqlparser.SqlParser;
import io.github.potjerodekool.sqlparser.SqlParserVisitor;
import io.github.potjerodekool.sqlparser.ast.InsertStatement;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InsertVisitor implements SqlParserVisitor<Object> {

    @Override
    public Object visitSql(final SqlParser.SqlContext ctx) {
        return ctx.statement().stream()
                .map(it -> it.accept(this))
                //.filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Object visitStatement(final SqlParser.StatementContext ctx) {
        if (ctx.insertStatement() != null) {
            return ctx.insertStatement().accept(this);
        }
        return null;
    }

    @Override
    public Object visitInsertStatement(final SqlParser.InsertStatementContext ctx) {
        final var lineNumber = ctx.getStart().getLine();

        final var tableName = ctx.tableName.getText();
        final var columnNames = (List<String>) ctx.columnNames.accept(this);
        final var values = (List<Object>) ctx.values().accept(this);
        return new InsertStatement(tableName, columnNames, values, lineNumber);
    }

    @Override
    public Object visitFormalColumnNames(final SqlParser.FormalColumnNamesContext ctx) {
        return ctx.formalColumnName().stream()
                .map(it -> it.accept(this))
                .toList();
    }

    @Override
    public Object visitFormalColumnName(final SqlParser.FormalColumnNameContext ctx) {
        return ctx.IDENTIFIER().getText();
    }

    @Override
    public Object visitValues(final SqlParser.ValuesContext ctx) {
        return ctx.literal().stream()
                .map(it -> it.accept(this))
                .collect(Collectors.toList());
    }

    @Override
    public Object visitLiteral(final SqlParser.LiteralContext ctx) {
        return ctx.getChild(0).accept(this);
    }

    @Override
    public Object visit(final ParseTree tree) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visitChildren(final RuleNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visitTerminal(final TerminalNode node) {
        return switch (node.getSymbol().getType()) {
            case SqlParser.IntegerLiteral -> Integer.parseInt(node.getText());
            case SqlParser.StringLiteral -> {
                final var text = node.getText();
                yield text.substring(1, text.length() - 1);
            }
            case SqlParser.NullLiteral -> null;
            case SqlParser.BooleanLiteral -> "true".equals(node.getText());
            default -> throw new UnsupportedOperationException("" + node.getSymbol().getType());
        };
    }

    @Override
    public Object visitErrorNode(final ErrorNode node) {
        return null;
    }
}