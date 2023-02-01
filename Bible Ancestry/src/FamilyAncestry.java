import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class FamilyAncestry {

    AncestryTreeNode First;
    AncestryTreeNode Curr;
    AncestryTreeNode UndoPointer;

    public FamilyAncestry() {
        First = Curr = UndoPointer = new AncestryTreeNode();
    }

    public void createAncestryTree(Scanner reader) {
        First = convertFileToTree(First, reader);
    }

    private AncestryTreeNode convertFileToTree(AncestryTreeNode node, Scanner reader) {

        if (reader.hasNextLine() == false) {
            return null;
        } else if (reader.hasNext("noKid")) {
            reader.nextLine();
            return null;
        } else if (reader.hasNext("noSib")) {
            reader.nextLine();
            return null;
        } else {
            node = new AncestryTreeNode(reader.nextLine());
            node.setDown(convertFileToTree(node.down(), reader));
            node.setRight(convertFileToTree(node.right(), reader));
            return node;
        }
    }


    public boolean traceAncestors(String name) {
        Curr = First;
        if (doesPersonExist(Curr, name) == true) {
            innerTraceAncestors(Curr, name);
            return true;
        } else {
            System.out.println("The person you inputted does not exist. Please check your spelling. If the person has "
                    + "more than one name, please try the other one.");
            return false;
        }
    }

    private void innerTraceAncestors(AncestryTreeNode searcher, String name) {

        name = name.toLowerCase();
        String[] tokens = searcher.person().split(" ");

        while (tokens[1].toLowerCase().compareTo(name) != 0) {

            if (searcher.down() != null) {

                if (doesPersonExist(searcher.down(), name) == true) {
                    tokens = searcher.person().split(" ");
                    System.out.println(tokens[1]);
                    searcher = searcher.down();
                    tokens = searcher.person().toLowerCase().split(" ");
                } else {
                    searcher = searcher.right();
                    tokens = searcher.person().toLowerCase().split(" ");
                }

            } else {
                searcher = searcher.right();
                tokens = searcher.person().toLowerCase().split(" ");
            }

        }

        tokens = searcher.person().split(" ");
        System.out.println(tokens[1]);
    }

    private boolean doesPersonExist(AncestryTreeNode searcher, String name) {
        String[] tokens = new String[2];

        if (searcher != null) {
            tokens = searcher.person().split(" ");
        }

        if (searcher == null) {
            return false;
        } else if (tokens[1].toLowerCase().compareTo(name.toLowerCase()) == 0) {
            return true;
        } else {
            boolean matchFoundInDown = doesPersonExist(searcher.down(), name);
            boolean matchFoundInRight = doesPersonExist(searcher.right(), name);
            return matchFoundInDown || matchFoundInRight;
        }
    }

    public void addKid(String parent, String kid) {
        Curr = First;
        UndoPointer = innerAddKid(Curr, parent, kid);
    }

    private AncestryTreeNode innerAddKid(AncestryTreeNode searcher, String parent, String kid) {

        parent = parent.toLowerCase();
        String[] tokens = searcher.person().split(" ");

        while (tokens[1].toLowerCase().compareTo(parent) != 0) {

            if (searcher.down() != null) {
                if (doesPersonExist(searcher.down(), parent) == true) {
                    searcher = searcher.down();
                    tokens = searcher.person().split(" ");
                } else {
                    searcher = searcher.right();
                    tokens = searcher.person().split(" ");
                }
            } else {
                searcher = searcher.right();
                tokens = searcher.person().split(" ");
            }
        }

        boolean hasChild = searcher.down() != null;
        boolean hasSibling = searcher.right() != null;


        if (hasChild) {
            searcher = searcher.down();

            while (hasSibling) {
                searcher = searcher.right();
            }

            searcher.setRight(new AncestryTreeNode("K: " + kid));

        } else {
            String parentName = searcher.person();

            tokens = parentName.split(" ");

            searcher.setPerson("P: " + tokens[1]);
            searcher.setDown(new AncestryTreeNode("K: " + kid));
        }

        return searcher;

    }

    public void undoAdd() {

        boolean theAdditionToRemoveIsRight = UndoPointer.right() != null;
        boolean theAdditionToRemoveIsDown = UndoPointer.down() != null;

        if (theAdditionToRemoveIsRight) {
            UndoPointer.setRight(null);
        } else if (theAdditionToRemoveIsDown) {
            UndoPointer.setDown(null);
        }

    }

    public void displayTree() {
        printSubtree(First, 0, 0);
    }

    private void printSubtree(AncestryTreeNode parent, Integer numlabel, Integer nextNumLabel) {

        String[] tokensOfParent = parent.person().split(" ");

        if (numlabel != 0) {
            System.out.println(tokensOfParent[1] + "(" + numlabel + ")");
        } else {
            System.out.println(tokensOfParent[1]);
        }

        Integer parentNumber = nextNumLabel;

        AncestryTreeNode firstWalker = parent;
        firstWalker = firstWalker.down();

        ArrayList<AncestryTreeNode> walkerList = new ArrayList<>();
        ArrayList<AncestryTreeNode> walkerList2 = new ArrayList<>();

        while (firstWalker.right() != null) {

            walkerList.add(firstWalker);
            firstWalker = firstWalker.right();
        }

        walkerList.add(firstWalker);
        firstWalker = firstWalker.right();

        ArrayList<AncestryTreeNode> parentsWithMultipleKids = new ArrayList<>();
        ArrayList<Integer> parentNumbers = new ArrayList<>();

        boolean walkerListContainsSomething = true;

        while (walkerListContainsSomething) {

            for (AncestryTreeNode aWalker : walkerList) {
                if (aWalker != null) {

                    String[] tokensOfPerson = aWalker.person().split(" ");

                    boolean hasAKid = aWalker.down() != null;
                    if (hasAKid) {
                        boolean hasMultipleKids = aWalker.down().right() != null;

                        if (hasMultipleKids) {

                            walkerList2.add(null);
                            parentsWithMultipleKids.add(aWalker);
                            parentNumber++;
                            parentNumbers.add(parentNumber);

                            String personWithLabel = tokensOfPerson[1] + "(" + parentNumber + ")";
                            System.out.print(personWithLabel);

                            if (aWalker.right() != null) {
                                String neededDashes = "";
                                for (int i = personWithLabel.length(); i < 14; i++) {
                                    neededDashes += "-";
                                }
                                if (aWalker != walkerList.get(walkerList.size() - 1)) {
                                    System.out.print(neededDashes);
                                }
                            } else {
                                String neededSpaces = "";
                                for (int i = personWithLabel.length(); i < 14; i++) {
                                    neededSpaces += " ";
                                }
                                System.out.print(neededSpaces);
                            }


                        } else {
                            //if the person does not have multiple kids, print normally,
                            System.out.print(tokensOfPerson[1]);

                            if (aWalker.right() != null) {
                                String neededDashes = "";
                                for (int i = tokensOfPerson[1].length(); i < 14; i++) {
                                    neededDashes += "-";
                                }

                                if (aWalker != walkerList.get(walkerList.size() - 1)) {
                                    System.out.print(neededDashes);
                                }
                            } else {
                                String neededSpaces = "";
                                for (int i = tokensOfPerson[1].length(); i < 14; i++) {
                                    neededSpaces += " ";
                                }
                                System.out.print(neededSpaces);
                            }
                            walkerList2.add(aWalker.down());
                        }
                    } else {
                        //if the person does not have a kid, print normally.
                        System.out.print(tokensOfPerson[1]);
                        if (aWalker.right() != null) {
                            String neededDashes = "";
                            for (int i = tokensOfPerson[1].length(); i < 14; i++) {
                                neededDashes += "-";
                            }
                            if (aWalker != walkerList.get(walkerList.size() - 1)) {
                                System.out.print(neededDashes);
                            }
                        } else {
                            String neededSpaces = "";
                            for (int i = tokensOfPerson[1].length(); i < 14; i++) {
                                neededSpaces += " ";
                            }
                            System.out.print(neededSpaces);
                        }
                        walkerList2.add(null);
                    }
                } else {
                    walkerList2.add(null);
                    String neededSpaces = "";
                    for (int i = 0; i < 14; i++) {
                        neededSpaces += " ";
                    }

                    System.out.print(neededSpaces);

                }

            }

            System.out.println();
            walkerList.clear();

            for (AncestryTreeNode aWalker : walkerList2) {

                if (aWalker != null) {
                    String[] tokensOfPerson = aWalker.person().split(" ");

                    boolean hasAKid = aWalker.down() != null;
                    if (hasAKid) {
                        boolean hasMultipleKids = aWalker.down().right() != null;
                        if (hasMultipleKids) {
                            walkerList.add(null);
                            parentsWithMultipleKids.add(aWalker);
                            parentNumber++;
                            parentNumbers.add(parentNumber);

                            String personWithLabel = tokensOfPerson[1] + "(" + parentNumber + ")";
                            System.out.print(personWithLabel);

                            if (aWalker.right() != null) {
                                String neededDashes = "";
                                for (int i = tokensOfPerson[1].length(); i < 14; i++) {
                                    neededDashes += "-";
                                }
                                if (aWalker != walkerList2.get(walkerList2.size() - 1)) {
                                    System.out.print(neededDashes);
                                }
                            } else {
                                String neededSpaces = "";
                                for (int i = tokensOfPerson[1].length(); i < 14; i++) {
                                    neededSpaces += " ";
                                }
                                System.out.print(neededSpaces);
                            }

                        } else {
                            //if the person does NOT have multiple kids print normally
                            System.out.print(tokensOfPerson[1]);

                            if (aWalker.right() != null) {
                                String neededDashes = "";
                                for (int i = tokensOfPerson[1].length(); i < 14; i++) {
                                    neededDashes += "-";
                                }

                                if (aWalker != walkerList2.get(walkerList2.size() - 1)) {
                                    System.out.print(neededDashes);
                                }
                            } else {
                                String neededSpaces = "";
                                for (int i = tokensOfPerson[1].length(); i < 14; i++) {
                                    neededSpaces += " ";
                                }
                                System.out.print(neededSpaces);
                            }

                            walkerList.add(aWalker.down());
                        }
                    } else {
                        //if the person does not have a kid, print normally
                        System.out.print(tokensOfPerson[1]);

                        if (aWalker.right() != null) {
                            String neededDashes = "";
                            for (int i = tokensOfPerson[1].length(); i < 14; i++) {
                                neededDashes += "-";
                            }

                            if (aWalker != walkerList2.get(walkerList2.size() - 1)) {
                                System.out.print(neededDashes);
                            }
                        } else {
                            String neededSpaces = "";
                            for (int i = tokensOfPerson[1].length(); i < 14; i++) {
                                neededSpaces += " ";
                            }
                            System.out.print(neededSpaces);
                        }
                        walkerList.add(null);
                    }


                } else {
                    walkerList.add(null);
                    String neededSpaces = "";
                    for (int i = 0; i < 14; i++) {
                        neededSpaces += " ";
                    }
                    System.out.print(neededSpaces);

                }

            }
            System.out.println();

            walkerListContainsSomething = false;
            for (AncestryTreeNode aWalker : walkerList) {

                if (aWalker != null) {
                    walkerListContainsSomething = true;
                }
            }

            boolean recentWalker2ListContainedSomething = false;
            if (walkerListContainsSomething == false) {

                for (AncestryTreeNode aWalker : walkerList2) {
                    if (aWalker != null) {
                        recentWalker2ListContainedSomething = true;
                    }
                }

                if (recentWalker2ListContainedSomething == true) {
                    System.out.println();
                }

            }
            walkerList2.clear();
        }

        if (parentsWithMultipleKids.isEmpty() == false) {
            for (int i = 0; i < parentsWithMultipleKids.size(); i++) {
                printSubtree(parentsWithMultipleKids.get(i), parentNumbers.get(i),
                        nextNumLabel + parentsWithMultipleKids.size() + i);
            }
        }

    }

    public void saveTree(PrintStream writer) {
        Curr = First;
        convertTreetoFile(writer, Curr);
    }

    private void convertTreetoFile(PrintStream writer, AncestryTreeNode walker) {

        if (walker != null) {
            if (walker.down() == null && walker.right() == null) {
                writer.println(walker.person());
                writer.println("noKid");
                writer.println("noSib");
            } else {
                writer.println(walker.person());
                convertTreetoFile(writer, walker.down());
                if (walker.down() == null) {
                    writer.println("noKid");
                }
                convertTreetoFile(writer, walker.right());
                if (walker.right() == null) {
                    writer.println("noSib");
                }
            }
        }

    }


    private class AncestryTreeNode { //AKA ATN

        private String Person;
        private AncestryTreeNode Right;
        private AncestryTreeNode Down;


        public AncestryTreeNode() {
            Person = null;
            Right = Down = null;
        }

        public AncestryTreeNode(String name) {
            Person = name;
            Right = Down = null;
        }

        public AncestryTreeNode(String name, AncestryTreeNode right, AncestryTreeNode down) {
            Person = name;
            Right = right;
            Down = down;
        }


        String person() {
            return Person;
        }

        void setPerson(String name) {
            Person = name;
        }

        AncestryTreeNode right() {
            return Right;
        }

        void setRight(AncestryTreeNode right) {
            Right = right;
        }

        AncestryTreeNode down() {
            return Down;
        }

        void setDown(AncestryTreeNode down) {
            Down = down;
        }

    }
}
