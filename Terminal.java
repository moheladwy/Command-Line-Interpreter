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
    private String redirectOutputFile;
    private boolean redirectFlag = false, redirectOrAppendFlag = false;

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

            if (input.contains(">>")) {
                redirectOrAppendFlag = true;
                String[] parts = input.split(">>");
                redirectOutputFile = parts[1].trim();
                if (parts[1].trim().equals("")) {
                    redirectOutputFile = parts[2].trim();
                }
            }
            if (input.contains(">")) {
                redirectFlag = true;
                String[] parts = input.split(">");
                redirectOutputFile = parts[1].trim();
                if (parts[1].trim().equals("")) {
                    redirectOutputFile = parts[2].trim();
                }
            }

            return true;
        }
        return false;
    }

    public void resetParser() {
        args = new String[0];
        commandName = redirectOutputFile = "";
        redirectFlag = redirectOrAppendFlag = false;
    }

    public String getCommandName() {
        return commandName.trim().toLowerCase();
    }

    public String[] getArgs() {
        return args;
    }

    public boolean hasRedirect() {
        return redirectFlag;
    }

    public boolean hasRedirectOrAppend() {
        return redirectOrAppendFlag;
    }

    public String getRedirectOutputFile() {
        return redirectOutputFile;
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
            try {
                terminal.parser.resetParser();
                terminal.parser.parse(command);
                terminal.chooseCommandAction();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // This method will choose the suitable command method to be called
    public void chooseCommandAction() throws Exception {
        String[] args = parser.getArgs();
        String output = null;

        switch (parser.getCommandName()) {
            case "pwd":
                try {
                    output = pwd();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
            case "ls":
                try {
                    output = ls(args);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
            case "cd":
                if (args.length > 1)
                    System.err.println("Error: Too many arguments");
                else
                    cd(args);
                break;
            case "cat":
                try {
                    output = cat(args);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
            case "echo":
                try {
                    output = echo(args);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "mkdir":
                try {
                    mkdir(args);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
            case "rmdir":
                if (args.length > 1)
                    System.err.println("Error: Too many arguments");
                else
                    rmdir(args);
                break;
            case "rm":
                rm(args);
                break;
            case "touch":
                if (args.length > 1)
                    System.err.println("Error: Too many arguments");
                else
                    touch(args);
                break;
            case "cp":
                cp(args);
                break; 
            case "wc":
                try {
                    output = wc(args);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
            case "history":
                history(args);
                break;
            default:
                throw new ArgumentException(parser.getCommandName() + ": command not found");
        }
        if (output != null) {
            if (parser.hasRedirect() && !parser.hasRedirectOrAppend())
                redirect(output, parser.getRedirectOutputFile());
            else if (parser.hasRedirectOrAppend())
                redirectOrAppend(output, parser.getRedirectOutputFile());
            else
                System.out.println(output);
        }
    }

    // Takes no arguments and returns the current path.
    public String pwd() {
        return currentDirectory.toString();
    }

    /*
     * TODO: Implement all these cases:
     * 
     * 1. cd takes 1 argument which is either the full path or the relative (short)
     * path changes the current path to that path.
     */
    public void cd(String[] args) {
        if (args.length == 0) {
            currentDirectory = Path.of(System.getProperty("user.home"));
            return;
        }
        String newPath = args[0];
        Path newFilePath = currentDirectory.resolve(newPath);
        currentDirectory = (newFilePath);

    }

    // TODO: Takes 1 argument and prints it.
    public String echo(String[] args) {
        // Check if there are arguments provided
        if (args.length == 0) {
            return "No input provided.";
        } else {
            // Concatenate all the arguments to form a single string without quotations
            StringBuilder echoText = new StringBuilder();
            for (String arg : args) {
                if (arg.equals(">") || arg.equals(">>")) {
                    break;
                } else if (arg.startsWith("\"") && arg.endsWith("\"") && arg.length() > 1) {
                    echoText.append(arg, 1, arg.length() - 1).append(" ");
                } else {
                    echoText.append(arg).append(" ");
                }
            }

            // Remove the trailing space and print the result
            String result = echoText.toString().trim();
            return result;
        }
    }

    /*
     * TODO: merge with alieldeen code after accepting his pull request.
     */
    public String ls(String[] args) throws Exception {
        File directory = currentDirectory.toFile();
        String output = null;
        if (directory.exists() && directory.isDirectory()) {
            String[] files = directory.list();

            if (args.length == 0 || (args.length > 1 && (parser.hasRedirect() || parser.hasRedirectOrAppend()))) {
                if (files != null)
                    output = String.join(" ", files);
            } else if (args[0].trim().equalsIgnoreCase("-r")) {
                if (files != null) {
                    String[] reversedFiles = new String[files.length];
                    for (int i = files.length - 1; i > -1; i--)
                        reversedFiles[files.length - i - 1] = files[i];
                    output = String.join(" ", reversedFiles);
                }
            } else
                throw new ArgumentException("ls: takes no argument or take '-r' as an argument only!");
        } else
            throw new Exception("ls: No such file or directory");
        return output;
    }

    /*
     * TODO: Takes 1 or more arguments and creates a directory for each argument.
     * Each argument can be:
     * 1- Directory name (in this case the new directory is created in the current
     * directory)
     * 2- Path (full/short) that ends with a directory name (in this case
     * the new directory is created in the given path)
     */
    public void mkdir(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            Path directoryPath = currentDirectory.resolve(args[i]);

            File directory = new File(directoryPath.toString());

            createDirectory(directory);
        }
    }

    void createDirectory(File directory) throws Exception {
        if (!directory.exists()) {
            boolean created = directory.mkdir();
            if (!created)
                throw new Exception("Failed to create the directory.");
        } else
            throw new Exception("mkdir: cannot create directory '" + directory.getName() + "': File exists");
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

        if (args[0].equals("*")) {
            directoryPath = currentDirectory;
            deleteEmptyDirectories(directoryPath.toFile());
            return;
        } else
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
                        rmdir(new String[] { file.getName() });
                    }
                }
            }
        }
    }

    void removeDirectory(File directory) {
        if (directory.exists()) {
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
    // newResultPath = currentDirectory.resolve(relative_or_absolute_path);
    public void touch(String[] args) {
        Path newFilePath = currentDirectory.resolve(args[0]);

        try {
            Files.createFile(newFilePath);
        } catch (IOException e) {
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
    private static String getFilesContent(File[] files) throws IOException {
        String output = "";
        for (File file : files) {
            if (file != null && file.isFile() && file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;

                while ((line = reader.readLine()) != null)
                    output += line + "\n";

                reader.close();
            }
        }
        return output;
    }

    /*
     * TODO: merge with alieldeen code after accepting his pull request.
     */

    public String cat(String[] args) throws Exception {
        File directory = currentDirectory.toFile();
        String output = null;

        if (directory.exists() && directory.isDirectory()) {

            if ((args.length > 0 && args.length < 3) || parser.hasRedirect() || parser.hasRedirectOrAppend()) {
                File[] files = new File[args.length];

                for (int i = 0; i < args.length; i++)
                    files[i] = new File((currentDirectory + "/" + args[i]).trim());

                output = getFilesContent(files);
            } else
                throw new ArgumentException("cat: takes 1 or 2 arguments only!");
        } else
            throw new Exception("cat: No such file or directory!");
        return output;
    }

    /*
     * TODO: merge with alieldeen code after accepting his pull request.
     * 
     */
    public String wc(String[] args) throws Exception {
        String output = null;
        if (args.length == 1 || (args.length > 1 && (parser.hasRedirect() || parser.hasRedirectOrAppend()))) {
            File file = new File((currentDirectory.toString() + "/" + args[0]).trim());
            if (file != null && file.isFile()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                int nLines = 0, nWords = 0, nChars = 0;
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] words = line.split(" ");
                    nLines++;
                    nWords += words.length;
                    nChars += line.length();
                }
                reader.close();
                output = (nLines + " " + nWords + " " + nChars + " " + file.getName()).trim();
            } else
                throw new Exception("wc: No such file or directory!");
        } else
            throw new ArgumentException("wc: takes 1 argument 'file name' only!");
        return output;
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
    public void redirect(String output, String outputFile) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(output);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    // Appends the output to the file if it exists.
    public void redirectOrAppend(String output, String outputFile) {
        try {
            File file = new File(outputFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            if (file.getTotalSpace() == 0)
                writer.append("\n");
            writer.append(output);
            writer.newLine(); // add a new line after appending the content
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
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
