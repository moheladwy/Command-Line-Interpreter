import java.io.*;
import java.util.*;
import java.nio.file.*;

class ArgumentException extends Exception {
    public ArgumentException() {
        super();
    }

    public ArgumentException(String message) {
        super(message);
    }
}

class Parser {
    private String commandName;
    private String[] args;
    private String redirectOutputFile;
    private boolean redirectFlag = false, redirectOrAppendFlag = false;

    public Parser(String commandName, String[] args) {
        this.commandName = (commandName == null) ? "" : commandName;
        this.args = (args == null) ? new String[0] : args;
    }

    public boolean parse(String input) {
        resetParser();
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
    private List<String> commandHistory;

    public Terminal() {
        currentDirectory = Path.of(System.getProperty("user.dir"));
        commandHistory = new ArrayList<>();
    }

    public static void main(String[] args) {
        Terminal terminal = new Terminal();

        while (true) {
            System.out.print(terminal.currentDirectory.normalize() + ": ");
            String command = System.console().readLine();

            if (command.trim().equalsIgnoreCase("exit"))
                System.exit(0);

            try {
                terminal.parser.parse(command);
                terminal.chooseCommandAction();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

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
                try {
                    cd(args);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
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
                try {
                    rmdir(args);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
            case "rm":
                try {
                    rm(args);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
            case "touch":
                try {
                    touch(args);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
            case "cp":
                try {
                    cp(args);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
            case "cp-r":
                try {
                    cp_r(args);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
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
                output = history(args);
                break;
            default:
                throw new ArgumentException(parser.getCommandName() + ": command not found");
        }
        commandHistory.add(parser.getCommandName());
        if (output != null) {
            if (parser.hasRedirect() && !parser.hasRedirectOrAppend())
                redirect(output, parser.getRedirectOutputFile());
            else if (parser.hasRedirectOrAppend())
                redirectOrAppend(output, parser.getRedirectOutputFile());
            else
                System.out.println(output);
        }
    }

    public String pwd() {
        return currentDirectory.toString();
    }

    public void cd(String[] args) throws Exception {
        if (args.length == 0) {
            currentDirectory = Path.of(System.getProperty("user.home"));
            return;
        } else if (args.length == 1) {
            String newPath = args[0];
            Path newFilePath = currentDirectory.resolve(newPath);
            currentDirectory = (newFilePath);
        } else {
            throw new ArgumentException("cd: Too many arguments");
        }

    }

    public String echo(String[] args) {
        // Check if there are arguments provided
        if (args.length == 0) {
            return "No input provided.";
        } else {
            // Concatenate all the arguments to form a single string without quotations
            StringBuilder echoText = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.equals(">") || arg.equals(">>")) {
                    break;
                } else {
                    // Remove the double quotations from the argument, if any
                    arg = arg.replaceAll("\"", "");
    
                    // Add the argument to the echo text
                    echoText.append(arg).append(" ");
                }
            }
    
            // Remove the trailing space and print the result
            String result = echoText.toString().replaceAll("\\s+$", "");
            return result;
        }
    }

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

    public void mkdir(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            Path directoryPath = currentDirectory.resolve(args[i]);

            File directory = new File(directoryPath.toString());

            createDirectory(directory);
        }
    }

    private void createDirectory(File directory) throws Exception {
        if (!directory.exists()) {
            boolean created = directory.mkdir();
            if (!created)
                throw new Exception("Failed to create the directory.");
        } else
            throw new Exception("mkdir: cannot create directory '" + directory.getName() + "': File exists");
    }

    public void rmdir(String[] args) throws Exception {
        Path directoryPath;

        if (args.length == 1) {
            if (args[0].equals("*")) {
                directoryPath = currentDirectory;
                deleteEmptyDirectories(directoryPath.toFile());
                return;
            } else
                directoryPath = currentDirectory.resolve(args[0]);

            File directory = new File(directoryPath.toString());
            removeDirectory(directory);
        } else {
            throw new ArgumentException("rmdir: Too many arguments.");
        }
    }

    private void deleteEmptyDirectories(File directory) throws Exception {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files)
                    if (file.isDirectory())
                        rmdir(new String[] { file.getName() });
            }
        }
    }

    private void removeDirectory(File directory) {
        if (directory.exists()) {
            boolean removed = directory.delete();
            if (removed) {
                System.out.println(" Directory removed successfully: " + "\"" + directory.getName() + "\"");
            } else {
                System.out.println(" Failed to remove the directory: " + "\"" + directory.getName() + "\"");
            }
        }
    }

    public void touch(String[] args) throws Exception {
        if (args.length != 1)
            throw new ArgumentException("touch: takes 1 argument.");
        Path newFilePath = currentDirectory.resolve(args[0]);
        Files.createFile(newFilePath);
    }

    public void cp(String[] args) throws Exception {
        if (args.length != 2)
            throw new ArgumentException("cp: takes 2 arguments.");

        String source = args[0];
        String destination = args[1];
        File sourceFile = new File(source);
        File destinationFile = new File(destination);
        if (!sourceFile.exists())
            throw new ArgumentException("cp: source file does not exist");

        else if (sourceFile.exists() && destinationFile.isDirectory()) {
            // If the destination is a directory, copy the source file to that directory
            String destinationPath = destination = sourceFile.separator + sourceFile.getName();
            File destinationPathFile = new File(destinationPath);
            Files.copy(sourceFile.toPath(), destinationPathFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } else if (!destinationFile.exists())
            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
        else
            throw new ArgumentException(
                    "cp: Destination is not a directory and already exists use 'cp-r' for directories");
    }

    public void cp_r(String[] args) throws Exception {
        if (args.length != 2)
            throw new ArgumentException("cp: takes 2 arguments");

        String sourceDirectory = args[0];
        String destinationDirectory = args[1];

        File source = new File(sourceDirectory);
        File destination = new File(destinationDirectory);

        if (!source.exists() || !source.isDirectory())
            throw new ArgumentException("cp: Source directory does not exist.");

        if (!destination.exists())
            destination.mkdir();

        if (!destination.isDirectory())
            throw new ArgumentException("cp: Destination is not a directory.");

        File[] files = source.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // For directories, recursively call cp_r
                    String subDirectory = destinationDirectory + File.separator + file.getName();
                    cp_r(new String[] { file.getPath(), subDirectory });
                } else {
                    // For files, copy them to the destination directory
                    String destinationPath = destinationDirectory + File.separator + file.getName();
                    File destinationPathFile = new File(destinationPath);
                    Files.copy(file.toPath(), destinationPathFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    public void rm(String[] args) throws Exception {
        if (args.length != 1)
            throw new ArgumentException("rm: takes 1 argument");

        String fileName = args[0];
        File file = new File(fileName);
        if (!file.exists())
            throw new ArgumentException("rm: file does not exist");

        if (file.isDirectory())
            throw new ArgumentException("rm: file is a directory");

        if (!file.delete())
            throw new Exception("rm: file could not be deleted");
    }

    private static String getFilesContent(File[] files) throws Exception {
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

    public void redirect(String output, String outputFile) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(output);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

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

    public String history(String[] args) {
        String output = "Command History: ";

        if (commandHistory.size() == 0)
            output += "Empty\n";

        for (int i = 0; i < commandHistory.size(); i++)
            output += ((i + 1) + " " + commandHistory.get(i)).trim();

        return output;
    }
}
