parser grammar SqlParser;

@header {
package io.github.potjerodekool.sqlparser;
}

options { tokenVocab=SqlLexer; }

sql: statement (statement)*;

statement:
insertStatement
;

insertStatement: INSERT INTO tableName=IDENTIFIER '(' columnNames=formalColumnNames ')'
VALUES '(' values ')' ';'
;

formalColumnNames
: formalColumnName (',' formalColumnName)*;

formalColumnName: IDENTIFIER;

values: literal (',' literal)*;

literal
	:	IntegerLiteral
	|	FloatingPointLiteral
	|	BooleanLiteral
	|	NullLiteral
	|	StringLiteral
	;