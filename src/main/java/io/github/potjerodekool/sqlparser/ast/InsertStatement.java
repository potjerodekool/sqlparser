package io.github.potjerodekool.sqlparser.ast;

import java.util.List;
import java.util.stream.Collectors;

public class InsertStatement implements Statement {

    private final String tableName;
    private final List<String> columnNames;
    private final List<Object> values;
    private final int lineNumber;

    public InsertStatement(final String tableName,
                           final List<String> columnNames,
                           final List<Object> values,
                           final int lineNumber) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.values = values;
        this.lineNumber = lineNumber;
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<Object> getValues() {
        return values;
    }

    public <T> ValueContainer<T> getValue(final String columnName) {
        final var columnIndex = columnNames.indexOf(columnName);
        if (columnIndex < 0) {
            return ValueContainer.absent();
        } else {
            return (ValueContainer<T>) ValueContainer.of(values.get(columnIndex));
        }
    }

    @Override
    public String toString() {
        final var columnNamesStr = String.join(",", columnNames);
        final var valuesStr = values.stream()
                .map(this::valueToString)
                .collect(Collectors.joining(","));
        return String.format("INSERT INTO %s (%s) VALUES (%s);", tableName, columnNamesStr, valuesStr);
    }

    private String valueToString(final Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return "'" + value + "'";
        }
        return value.toString();
    }

}
