class Parser {
    private String commandName;
    private String[] args;

    // Constructor.
    public Parser(String commandName, String[] args) {
        this.commandName = (commandName == null) ? "" : commandName;
        this.args = (args == null) ? new String[0] : args;
    }

    // This method will divide the input into commandName and args
    // where "input" is the string command entered by the user
    public boolean parse(String input) {
        return true;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }
}

public class Terminal {
    Parser parser = new Parser(null, null);

    public static void main(String[] args) {
        Terminal terminal = new Terminal();

        while (true) {
            String command = System.console().readLine();
            if (command.equalsIgnoreCase("exit"))
                terminal.exit();
            terminal.parser.parse(command);
            terminal.chooseCommandAction();
        }
    }

    // This method will choose the suitable command method to be called
    public void chooseCommandAction() {

    }

    // TODO: Takes no arguments and prints the current path.
    public String pwd() {
        return new String();
    }

    /*
     * TODO: Implement all these cases:
     * 
     * 1. cd takes no arguments and changes the current path to the path of your
     * home directory.
     * 2. cd takes 1 argument which is “..” (e.g. cd ..) and changes the current
     * directory to the previous directory.
     * 3. cd takes 1 argument which is either the full path or the relative (short)
     * path
     * and changes the current path to that path.
     */
    public void cd(String[] args) {

    }

    // TODO: Takes 1 argument and prints it.
    public void echo(String[] args) {

    }

    /*
     * TODO: Implement all these cases:
     * 1- if takes no arguments then lists the contents of the current directory
     * sorted alphabetically,
     * 2- if got -r as an argument then prints it in the reversed order.
     */
    public void ls(String[] args) {

    }

    /*
     * TODO: Takes 1 or more arguments and creates a directory for each argument.
     * Each argument can be:
     * 1- Directory name (in this case the new directory is created in the current
     * directory)
     * 2- Path (full/short) that ends with a directory name (in this case
     * the new directory is created in the given path)
     */
    public void mkdir(String[] args) {

    }

    /*
     * TODO: Implement all these cases:
     * 1- rmdir takes 1 argument which is “*” (e.g. rmdir *) and removes all the
     * empty directories in
     * the current directory.
     * 2- rmdir takes 1 argument which is either the full path or the relative
     * (short) path and removes
     * the given directory only if it is empty.
     */
    public void rmdir(String[] args) {

    }

    /*
     * TODO: Takes 1 argument which is either the full path or the (short) path that
     * ends with a file name
     * and creates this file.
     */
    public void touch(String[] args) {

    }

    /*
     * TODO: Implement all these cases:
     * 1- Takes 2 arguments, both are files and copies the first onto the second.
     * 2- Takes 3 arguments, the first is "-r" and then 2 arguments, both are
     * directories (empty or not)
     * and copies the first directory (with all its content) into the second one.
     */
    public void cp(String[] args) {

    }

    // TODO: Takes 1 argument which is a file name that exists in the current
    // directory and removes this file.
    public void rm(String[] args) {

    }

    /*
     * TODO: Takes 1 argument and prints the file’s content or takes 2 arguments
     * and concatenates the content of the 2 files and prints it.
     */
    public void cat(String[] args) {

    }

    /*
     * TODO: Wc stands for “word count,” and as the name suggests, it is mainly used
     * for counting purpose.
     * By default, it displays four-columnar output.
     * 1- First column shows number of lines present in a file specified,
     * 2- second column shows number of words present in the file,
     * 3- third column shows number of characters present in file, and
     * 4- fourth column itself is the file name which are given as argument
     * Example:
     * wc file.txt
     * Output:
     * 9 79 483 file.txt
     * Explanation:
     * # 9 lines, 79 word, 483 character with spaces, file name
     */
    public void wc(String[] args) {

    }

    /*
     * TODO: Format: command > FileName
     * Redirects the output of the first command to be written to a file.
     * If the file doesn’t exist, it will be created.
     * If the file exists, its original content will be replaced.
     * Example:
     * echo Hello World > myfile.txt
     * ls > file
     */
    public void redirect(String[] args) {

    }

    // TODO: Like > but appends to the file if it exists.
    public void redirectOrAppend(String[] args) {

    }

    /*
     * TODO: Takes no parameters and displays an enumerated list with the commands
     * you’ve used in the past
     * Example:
     * history
     * Output:
     * 1 ls
     * 2 mkdir tutorial
     * 3 history
     */
    public void history(String[] args) {

    }

    // TODO: Implement the “exit” command which will allow the CLI to terminate.
    public void exit() {

    }
}