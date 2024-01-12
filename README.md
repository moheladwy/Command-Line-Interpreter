# Terminal Command Interpreter

This Java program implements a simple terminal command interpreter. It provides a set of basic commands similar to those found in a Unix-like shell. Users can interact with the program by entering commands, and the program will execute the corresponding actions.

## Features

- **Basic Commands**: Supports basic file and directory manipulation commands such as `pwd`, `ls`, `cd`, `cat`, `echo`, `mkdir`, `rmdir`, `rm`, `touch`, `cp`, `cp_r`, `wc`, and `history`.
- **Command History**: Keeps track of the command history, allowing users to view previously executed commands.
- **Redirection**: Supports output redirection using `>` and `>>` operators.

## Usage

1. **Compile**: Compile the Java program using a Java compiler (e.g., `javac Terminal.java`).

2. **Run**: Execute the compiled program (e.g., `java Terminal`).

3. **Enter Commands**: Enter commands at the prompt, and the program will execute the corresponding actions.

4. **Exit**: To exit the program, enter the command `exit`.

## Commands

- `pwd`: Print the current working directory.
- `ls [-r]`: List files and directories in the current directory. The optional `-r` flag reverses the order.
- `cd [directory]`: Change the current working directory. If no directory is provided, it goes to the home directory.
- `cat [file1] [file2] ...`: Concatenate and display the content of one or more files.
- `echo [text] [> or >> file]`: Display text or redirect it to a file. Use `>` to overwrite or `>>` to append.
- `mkdir [directory1] [directory2] ...`: Create one or more directories.
- `rmdir [directory]`: Remove an empty directory.
- `rm [file]`: Remove a file.
- `touch [file]`: Create an empty file.
- `cp [source] [destination]`: Copy a file or directory to another location.
- `cp_r [source] [destination]`: Recursively copy a directory and its contents to another location.
- `wc [file]`: Count the number of lines, words, and characters in a file.
- `history`: Display the command history.

## Redirection

- Use `>` to redirect output to a file, overwriting its content.
- Use `>>` to redirect output to a file, appending to its content.

## Notes

- The program supports both absolute and relative paths.
- The program handles exceptions such as invalid commands, file not found, and other relevant errors.

## Example Usage

```bash
$ java Terminal
/home/user: ls
file1.txt file2.txt
/home/user: cat file1.txt
Contents of file1.txt
...
/home/user: echo "Hello, World!" > output.txt
/home/user: cat output.txt
Hello, World!
/home/user: exit
```

Feel free to explore and experiment with the provided commands.
