import java.io.*;
import java.nio.file.*;

class ArgumentException extends Exception {
    public ArgumentException() {
        super();
    }

    public ArgumentException(String message) {
        super(message);
    }
}

// DONE.
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
        String[] commandWords = input.split(" ");
        if (commandWords != null && commandWords.length > 0) {
            commandName = commandWords[0];
            args = new String[commandWords.length - 1];
            for (int i = 1; i < commandWords.length; i++)
                args[i - 1] = commandWords[i];
            return true;
        }
        return false;
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
    private Path currentDirectory;

    public Terminal() {
        currentDirectory = Path.of(System.getProperty("user.dir"));
    }
    public static void main(String[] args) {
        Terminal terminal = new Terminal();
       
        while (true) {
            System.out.print(terminal.currentDirectory.normalize() + ": ");
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
                try {
                    ls(args);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "cd":
                if(args.length>1)
                    System.err.println("Error: Too many arguments");
                else
                    cd(args);
                break;
            case "cat":
                try {
                    cat(args);
                } catch (Exception e) {
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
                if(args.length>1)
                    System.err.println("Error: Too many arguments");
                else
                    rmdir(args);
                break;
            case "rm":
                rm(args);
                break;
            case "touch":
                if(args.length>1)
                    System.err.println("Error: Too many arguments");
                else
                    touch(args);
                break;
            case "cp":
                cp(args);
                break;
            case "wc":
                try {
                    wc(args);
                } catch (Exception e) {
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
        System.out.println(currentDirectory);
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
        if(args.length == 0)
        {
            currentDirectory = Path.of(System.getProperty("user.home"));
            return;
        }
        String newPath = args[0];
        Path newFilePath = currentDirectory.resolve(newPath);
        currentDirectory = (newFilePath);
 
    }

    // TODO: Takes 1 argument and prints it.
    public void echo(String[] args) {

    }

    /*
     * TODO: merge with alieldeen code after accepting his pull request.
     */
    public void ls(String[] args) throws Exception {
    
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
                throw new ArgumentException("ls: takes no argument or take '-r' as an argument only!");
            }
        } else {
            throw new Exception("ls: No such file or directory");
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
        for(int i = 0; i < args.length;i++)
        {
            Path directoryPath =currentDirectory.resolve(args[i]);

            File directory = new File(directoryPath.toString());
        
            createDirectory(directory);
        }
    }
    void createDirectory(File directory)
    {
        if (!directory.exists()) 
            {
                boolean created = directory.mkdir();
                if (created) 
                    System.out.println("Directory created successfully.");
                else 
                    System.out.println("Failed to create the directory.");
            } 
            else 
                System.out.println("Directory already exists.");
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
        Path directoryPath;

        if(args[0].equals("*") )
        {
            directoryPath = currentDirectory;
            deleteEmptyDirectories(directoryPath.toFile());
            return;
        }
        else
            directoryPath = currentDirectory.resolve(args[0]);

        File directory = new File(directoryPath.toString());
        removeDirectory(directory);
    }

    public void deleteEmptyDirectories(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
    
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        rmdir(new String[]{file.getName()});
                    }
                }
            }
        }
    }

    void removeDirectory(File directory)
    {
        if (directory.exists()) 
        {
            boolean removed = directory.delete();
            if (removed) {
                System.out.println(" Directory removed successfully: " + "\"" + directory.getName() + "\"");
            } else {
                System.out.println(" Failed to remove the directory: " + "\"" + directory.getName() + "\"");
            }
        } 
    }

    

    /*
     * TODO: Takes 1 argument which is either the full path or the (short) path that
     * ends with a file name
     * and creates this file.
     */
    //newResultPath = currentDirectory.resolve(relative_or_absolute_path);
    public void touch(String[] args) {
        Path newFilePath = currentDirectory.resolve(args[0]);
        
        try{
            Files.createFile(newFilePath);
            System.out.println("file was created successfully");
        }
        catch (IOException e) {
            System.err.println("Failed to create or write to the file: " + e.getMessage());
        }
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

    /*
     * TODO: merge with alieldeen code after accepting his pull request.
     */
    public void cat(String[] args) throws Exception {
        String currentDirectory = System.getProperty("user.dir");
        File directory = new File(currentDirectory);

        if (directory.exists() && directory.isDirectory()) {
            if (args.length > 0 && args.length < 3) {
                File[] files = new File[args.length];

                for (int i = 0; i < args.length; i++)
                    files[i] = new File((currentDirectory + "/" + args[i]).trim());

                printFilesContent(files);
            } else {
                throw new ArgumentException("cat: takes 1 or 2 arguments only!");
            }
        } else {
            throw new Exception("cat: No such file or directory!");
        }
    }

    /*
     * TODO: merge with alieldeen code after accepting his pull request.
     */
    public void wc(String[] args) throws Exception {
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
                throw new Exception("wc: No such file or directory!");
            }
        } else {
            throw new ArgumentException("wc: takes 1 argument 'file name' only!");
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