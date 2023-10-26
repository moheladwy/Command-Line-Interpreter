import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

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
        return commandName.trim().toLowerCase();
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
            System.out.print(System.getProperty("user.dir") + ": ");
            String command = System.console().readLine();

            if (command.trim().equalsIgnoreCase("exit"))
                System.exit(0);
            terminal.parser.parse(command);
            terminal.chooseCommandAction();
        }
    }

    // This method will choose the suitable command method to be called
    public void chooseCommandAction() {
        String[] args = parser.getArgs();

        switch (parser.getCommandName()) {
            case "pwd":
                pwd();
                break;
            case "ls":
                ls(args);
                break;
            case "cd":
                cd(args);
                break;
            case "cat":
                try {
                    cat(args);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "echo":
                echo(args);
                break;
            case "mkdir":
                mkdir(args);
                break;
            case "rmdir":
                rmdir(args);
                break;
            case "rm":
                rm(args);
                break;
            case "touch":
                touch(args);
                break;
            case "cp":
                cp(args);
                break;
            case "wc":
                try {
                    wc(args);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case ">":
                redirect(args);
                break;
            case ">>":
                redirectOrAppend(args);
                break;
            case "history":
                history(args);
                break;
            default:
                break;
        }
    }

    // Takes no arguments and prints the current path.
    public void pwd() {
        System.out.println(System.getProperty("user.dir"));
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
        File directory = new File(System.getProperty("user.dir"));
        if (directory.exists() && directory.isDirectory()) {
            String[] files = directory.list();

            if (args.length == 0) {
                if (files != null) {
                    for (String fileName : files)
                        System.out.println(fileName);
                }
            } else if (args.length == 1 && args[0].equalsIgnoreCase("-r")) {
                if (files != null) {
                    for (int i = files.length - 1; i > -1; i--)
                        System.out.println(files[i]);
                }
            } else {
                // TODO: Throw Exception couse it supose to take 0 or 1 argument only.
            }
        } else {
            // TODO: Throw Exception couse an error with the current directory.
        }
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

    // Takes an array of File and prints the content of all files.
    private static void printFilesContent(File[] files) throws IOException {
        for (File file : files) {
            if (file != null && file.isFile() && file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;

                while ((line = reader.readLine()) != null)
                    System.out.println(line);

                reader.close();
            }
        }
    }

    // Takes a File and prints the content of it.
    private static void printFileContent(File file) throws IOException {
        if (file != null && file.isFile() && file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null)
                System.out.println(line);

            reader.close();
        }
    }

    /*
     * TODO: Takes 1 argument and prints the file’s content or takes 2 arguments
     * and concatenates the content of the 2 files and prints it.
     */
    public void cat(String[] args) throws IOException {
        String currentDirectory = System.getProperty("user.dir");
        File directory = new File(currentDirectory);

        if (directory.exists() && directory.isDirectory()) {
            if (args.length == 1) {
                // gets file object from the absolute path of the file.
                File file = new File((currentDirectory + "/" + args[0]).trim());
                printFileContent(file);

            } else if (args.length == 2) {
                File[] files = new File[2];
                files[0] = new File((currentDirectory + "/" + args[0]).trim());
                files[1] = new File((currentDirectory + "/" + args[1]).trim());

                printFilesContent(files);

            } else {
                // TODO: Throw an Exception for number of arguments.
            }
        } else {
            // TODO: Throw an Exception for the error with directory.
        }
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
    public void wc(String[] args) throws IOException {
        if (args.length == 1) {
            File file = new File((System.getProperty("user.dir") + "/" + args[0]).trim());
            if (file != null && file.isFile()) {

                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                int nLines = 0, nWords = 0, nChars = 0;
                while ((line = reader.readLine()) != null) {
                    String[] words = line.split(" ");
                    nLines++;
                    nWords += words.length;
                    nChars += line.length();
                }
                reader.close();

                System.out.println((nLines + " " + nWords + " " + nChars + " " + file.getName()).trim());
            } else {
                // TODO: Throw an Excpetion for the error of the file.
            }
        } else {
            // TODO: Throw an Exception for the error of the number of the arguments.
        }
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
     * you’ve used in the past.
     * Example:
     * history
     * Output:
     * 1 ls
     * 2 mkdir tutorial
     * 3 history
     */
    public void history(String[] args) {

    }
}