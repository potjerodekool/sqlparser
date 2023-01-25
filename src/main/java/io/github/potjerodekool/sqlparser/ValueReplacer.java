package io.github.potjerodekool.sqlparser;

import io.github.potjerodekool.sqlparser.ast.InsertStatement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ValueReplacer {

    private final Map<String, Function<InsertStatement, Object>> replacements = new HashMap<>();

    private final List<InsertStatement> insertStatements;

    public ValueReplacer(final List<InsertStatement> insertStatements) {
        this.insertStatements = insertStatements;
    }

    public List<InsertStatement> getInsertStatements() {
        return insertStatements;
    }

    public ValueReplacer replace(final String columnName,
                                 final Object value) {
        return replace(columnName, (insertStatement -> value));
    }

    public ValueReplacer replace(final String columnName,
                                 final Function<InsertStatement, Object> replaceFunction) {
        replacements.put(columnName, replaceFunction);
        execute(this.insertStatements);
        return this;
    }

    private List<InsertStatement> execute(final List<InsertStatement> insertStatements) {
        return insertStatements.stream()
                .map(this::replace)
                .collect(Collectors.toList());
    }

    private InsertStatement replace(final InsertStatement insertStatement) {
        for (Map.Entry<String, Function<InsertStatement, Object>> replacement : replacements.entrySet()) {
            final var fullColumnName = replacement.getKey();
            final var replaceFunction = replacement.getValue();

            final var parts = fullColumnName.split("\\.");
            final String tableName;
            final String columnName;

            if (parts.length == 1) {
                tableName = null;
                columnName = parts[0];
            } else {
                tableName = parts[0];
                columnName = parts[1];
            }

            if (tableName != null &&
                    !tableName.equals(insertStatement.getTableName())) {
                continue;
            }

            final var columnIndex = insertStatement.getColumnNames().indexOf(columnName);

            if (columnIndex > -1) {
                final var newValue = replaceFunction.apply(insertStatement);
                insertStatement.getValues().remove(columnIndex);
                insertStatement.getValues().add(columnIndex, newValue);
            }
        }

        return insertStatement;
    }
}
