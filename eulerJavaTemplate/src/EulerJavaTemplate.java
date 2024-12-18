import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.io.File;
import java.util.Scanner;


public class EulerJavaTemplate {
    public static void main(String[] args) {

        // check that argument was added
        if (args.length == 0) {
            System.out.println("No problem number given.");
            System.out.println("Usage: java -jar EulerJavaTemplate.jar <number of problem>");
            return;
        }

        int problemNumber = Integer.parseInt(args[0]);
        String fileName = "./" + problemNumber + ".java";

        Problem problem = new Problem(problemNumber);
        problem.fetchPage(); //get page from projecteuler.net
        problem.parsePage(); //get title and prompt from HTML

        //check if file already exists, if it does, ask for confirmation
        File file = new File(fileName);
        if (file.exists()) {
            System.out.println("File already exists. Overwrite? [y/n]");
            Scanner scanner = new Scanner(System.in);
            if (!scanner.next().toLowerCase().equals("y")) {
                scanner.close();
                System.out.println("Aborting...");
                return;
            } else {
                System.out.println("Overwriting...");
            }
            scanner.close();
        }


        //create file with starter code
        try (PrintWriter write = new PrintWriter(fileName)) {
            write.println("// Project Euler Problem #" + problemNumber + ": " + problem.getTitle());
            write.println("// Author: Christian Brewer");
            write.println("// Date: " + LocalDate.now());
            write.println("// " + problem.getPrompt());
            write.println("\npublic class EulerJava" + problemNumber + " {\n\tpublic static void main(String[] args) {\n\t}\n}");
            System.out.println(fileName + " created.");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}