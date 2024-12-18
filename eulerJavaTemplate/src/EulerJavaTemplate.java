public class EulerJavaTemplate {
    public static void main(String[] args) {

        // check that argument was added
        if (args.length == 0) {
            System.out.println("No problem number given.");
            System.out.println("Usage: java -jar EulerJavaTemplate.jar <number of problem>");
            return;
        }

        Problem problem = new Problem(Integer.parseInt(args[0]));
        problem.fetchPage();
        problem.parsePage();
        System.out.println(problem.getNumber());
        System.out.println(problem.getTitle());
        System.out.println(problem.getPrompt());


    }
}