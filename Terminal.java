class Parser {
    String commandName;
    String[] args;

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
    Parser parser;

    public static void main(String[] args) {

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
     *       1. cd takes no arguments and changes the current path to the path of your home directory.
     *       2. cd takes 1 argument which is “..” (e.g. cd ..) and changes the current directory to the previous directory.
     *       3. cd takes 1 argument which is either the full path or the relative (short) path 
     *          and changes the current path to that path.
     */
    public void cd(String[] args) {

    }

    // TODO: Takes 1 argument and prints it.
    public void echo(String[] args) {

    }

    /*
    * TODO: Implement all these cases: 
    *       1- if takes no arguments then lists the contents of the current directory sorted alphabetically,
    *       2- if got -r as an argument then prints it in the reversed order.
    */
    public void ls(String[] args) {

    }

    /*
     * TODO: Takes 1 or more arguments and creates a directory for each argument. 
     *      Each argument can be: 
     *          1- Directory name (in this case the new directory is created in the current directory) 
     *          2- Path (full/short) that ends with a directory name (in this case 
     *              the new directory is created in the given path)
     */
    public void mkdir(String[] args) {

    }

    /*
     * TODO: Implement all these cases: 
     *      1- rmdir takes 1 argument which is “*” (e.g. rmdir *) and removes all the empty directories in 
     *         the current directory.  
     *      2- rmdir takes 1 argument which is either the full path or the relative (short) path and removes 
     *         the given directory only if it is empty.
     */
    public void rmdir(String[] args) {

    }

    // TODO: Takes 1 argument which is either the full path or the (short) path that ends with a file name and creates this file.
    public void touch(String[] args) {

    }

    /*
     * TODO: Implement all these cases: 
     *       1- Takes 2 arguments, both are files and copies the first onto the second.
     *       2- Takes 3 arguments, the first is "-r" and then 2 arguments, both are directories (empty or not) 
     *          and copies the first directory (with all its content) into the second one.
     */
    public void cp(String[] args) {

    }
}