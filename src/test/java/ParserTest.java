import io.github.potjerodekool.sqlparser.SqlQueryParser;
import io.github.potjerodekool.sqlparser.ValueReplacer;
import io.github.potjerodekool.sqlparser.ast.InsertStatement;
import io.github.potjerodekool.sqlparser.ast.Statement;
import io.github.potjerodekool.sqlparser.ast.ValueContainer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class ParserTest {

    @Test
    void test() {
        final var query = """
                insert into person (id, firstName, lastName) values (1, 'Evert', 'Tigchelaar');
                insert into person (id, firstName, lastName) values (2, 'Piet', 'van Pruttelen');
                insert into person (id, firstName, lastName) values (3, 'Sjaak', 'van Deursen');
                """;

        final var parser = new SqlQueryParser();
        final var statements = parser.parse(query);

        var insertStatements = statements.stream()
                .filter(statement -> statement instanceof InsertStatement)
                .map(statement -> (InsertStatement) statement)
                .collect(Collectors.toList());

        final var names = List.of("Klaas", "Jan");

        insertStatements = new ValueReplacer(insertStatements)
                .replace("person.firstName", (stm) -> {
                    final ValueContainer<Integer> idValueContainer = stm.getValue("id");
                    return idValueContainer
                            .map(id -> {
                                final var index = id - 1;
                                if (index < 0 || index >= names.size()) {
                                    return stm.getValue("firstName").getValue();
                                } else {
                                    return names.get(index);
                                }
                            })
                            .getValue();
                })
                .getInsertStatements();

        for (Statement statement : insertStatements) {
            System.out.println(statement);
        }
    }
}