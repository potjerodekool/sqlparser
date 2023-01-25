lexer grammar SqlLexer;

@header {
package io.github.potjerodekool.sqlparser;
}

options { caseInsensitive = true; }

INSERT: 'insert';
INTO: 'into';
VALUES: 'values';

NullLiteral
	:	'null'
	;

IntegerLiteral
:	DecimalIntegerLiteral
;

FloatingPointLiteral
	:	DecimalFloatingPointLiteral
	;

BooleanLiteral
	:	'true'
	|	'false'
	;

StringLiteral
	:	'\'' StringCharacters? '\''
	;

IDENTIFIER:         Letter LetterOrDigit*;

WS:                 [ \t\r\n\u000C]+ -> channel(HIDDEN);

COMMA  : ',';
SEMICOLON: ';';
COLON: ':';
LPAREN : '(';
RPAREN : ')';
SPACE: ' ';
MIN: '-';
DOT: '.';

fragment Digits
    : [0-9] ([0-9_]* [0-9])?
    ;

fragment Letter
    : [a-zA-Z$_];

fragment LetterOrDigit
    : Letter
    | [0-9]
    ;

fragment
DecimalFloatingPointLiteral
	:	Digits '.' Digits?
	|	'.' Digits
	|	Digits
	|	Digits
	;

fragment
DecimalIntegerLiteral
	:	DecimalNumeral
	;

fragment
DecimalNumeral
	:	'0'
	|	NonZeroDigit (Digits?)
	;

fragment
NonZeroDigit
	:	[1-9]
	;

fragment
StringCharacters
	:	StringCharacter+
	;

fragment
StringCharacter
	:	~['\\]
	|	EscapeSequence
	;

fragment
EscapeSequence
	:	'\\' [btnfr"'\\];
