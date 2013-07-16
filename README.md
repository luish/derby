"derby"
=====

Trabalho da disciplina de Compiladores no curso de Ciência da Computação na UFLA no segundo semestre de 2012.

Foi utilizado o [Coco/R](http://www.ssw.uni-linz.ac.at/coco/) (Compiler Generator) em Java.

### Linguagem

A definição da sintaxe da linguagem é escrita na forma [EBNF](http://en.wikipedia.org/wiki/EBNF) (Extended Backus-Naur Form) e está na pasta *atg*, no arquivo Derby.atg. 

Neste arquivo contém a definição dos tokens, produções, conjunto de caracteres e também comandos que farão a a análise semântica dentro do código gerado.

### Entrada

Uma entrada de exemplo do programa está na pasta *input*.

## Execução

Para criar os arquivos Parser e Scanner:
```
$ java -jar Coco.jar -package compiler -o src/compiler/ atg/Derby.atg
```

Após criar, basta chamar a classe principal:
```
$ java compiler.Compiler input/input1.txt
```

Um exemplo de saída:
```
integer a declared.
integer b declared.
integer c declared.
a = 5 + 3 = 8
b = 6 * 7 = 42
c = 8 + 42 = 50
-- line 8 col 5: d not declared
c = 50
a = 50 - 42 = 8
integer d declared.
d = 50 / 8 = 6
1 erro(s)
```

### Grupo:

* Diego Sarmento Mendes ([@diegosmend](https://twitter.com/diegosmend))
* Luis Henrique Borges ([@luishborges](https://twitter.com/luishborges))


***

*O nome do projeto é uma brincadeira com o apelido de um amigo nosso. :)*

