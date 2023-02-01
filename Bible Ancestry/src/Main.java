import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner receptionist = new Scanner(System.in);
        System.out.println("What text file would you like to use to base your family tree off of?");
        String textFile = receptionist.nextLine();

        FamilyAncestry newTree = new FamilyAncestry();

        try {
            File aFile = new File(textFile);
            Scanner reader = new Scanner(aFile);
            newTree.createAncestryTree(reader);
            reader.close();


        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }

        boolean endOfProgram = false;
        int numOfChanges = 0;

        while (endOfProgram == false) {

            System.out.println("Please choose an option for this family tree by entering a letter:\n" +
                    "a. Trace a person's ancestry\n" + "b. Add a new child\n" + "c. Undo most recent child addition\n" +
                    "d. Display entire tree\n" + "e. Save tree\n" + "f. Exit program");
            Scanner inputReader = new Scanner(System.in);
            String userInput = inputReader.nextLine();
            userInput = userInput.toLowerCase();

            if (userInput.compareTo("a") == 0) {
                System.out.println("Whose ancestry should I trace?");
                userInput = inputReader.nextLine();
                newTree.traceAncestors(userInput);
            } else if (userInput.compareTo("b") == 0) {
                String[] inputs = new String[2];
                System.out.println("Which parent should I add a child to?");
                inputs[0] = inputReader.nextLine();
                System.out.println("What is the child's name?");
                inputs[1] = inputReader.nextLine();
                newTree.addKid(inputs[0], inputs[1]);
                numOfChanges++;
            } else if (userInput.compareTo("c") == 0) {
                newTree.undoAdd();
            } else if (userInput.compareTo("d") == 0) {
                newTree.displayTree();
            } else if (userInput.compareTo("e") == 0) {
                File saveFile = new File(textFile);
                if (saveFile.exists()) {
                    saveFile.delete();
                    saveFile = new File(textFile);
                    PrintStream writer = new PrintStream(saveFile);
                    newTree.saveTree(writer);
                    System.out.println("Successfully saved.");
                } else {
                    System.out.println("File error");
                }
                numOfChanges = 0;
            } else if (userInput.compareTo("f") == 0) {

                boolean isWrongInput = true;

                while (isWrongInput) {
                    if (numOfChanges > 0) {
                        System.out.println("Save changes before exiting?[y/n]");
                        String yesOrNoInput = inputReader.nextLine().toLowerCase();

                        if (yesOrNoInput.compareTo("y") == 0) {
                            File saveFile = new File(textFile);
                            if (saveFile.exists()) {
                                saveFile.delete();
                                saveFile = new File(textFile);
                                PrintStream writer = new PrintStream(saveFile);
                                newTree.saveTree(writer);
                            } else {
                                System.out.println("File error");
                            }
                            isWrongInput = false;
                            endOfProgram = true;
                        } else if (yesOrNoInput.compareTo("n") == 0) {
                            endOfProgram = true;
                            isWrongInput = false;
                        } else {
                            System.out.println("Invalid input. Please try again.");
                        }
                    } else {
                        isWrongInput = false;
                        endOfProgram = true;
                    }
                }
            } else {
                System.out.println("Invalid input. Please try again.");
            }
            System.out.println();
        }

    }
}