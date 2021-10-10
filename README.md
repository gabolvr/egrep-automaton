# egrep-automaton
## DAAR - Project 01: egrep clone

### File Structure
- The *src* folder contains the following java packages :
    - Automaton : automaton representation.
    - Egrep : run a pattern search on a text file.
    - RegEx : build a RegExTree (provided by the teacher).
    - Text : file reader.
    - Main.java : run the project.
- Tests : test files.
- The root directory also contains the *README* file, the *build* configuration file from ```ant``` and the project report.
### Build

We can compile the project by using the following ```ant``` command inside the root folder:

```
$ ant dist
```
which will create the ```.jar``` file ```egrep-automaton.jar``` in ```./dist```

### Execution

To execute, simply run
```shell script
java -jar egrep-automaton.jar [-DFA] [regEx] [filePath]
```

- **-DFA** : Force the use of the DFA strategy only.

Exemple :
```shell script
java -jar egrep-automaton.jar "S(a|g|r)+on" "../tests/babylon.txt"
```