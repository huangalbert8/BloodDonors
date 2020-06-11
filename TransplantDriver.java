/**
 * * @author Albert Huang ID#:112786492 Rec:R01
 * This class represents the Driver class that includes the donor and recipient file that is serializable.
 */
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class TransplantDriver extends TransplantGraph {
    public static final String DONOR_FILE = "donors.txt";//change back to donors.txt
    public static final String RECIPIENT_FILE = "recipients.txt";


    public static void main(String[] args) throws IOException {
        TransplantGraph transplantGraph;
        try {
            FileInputStream file = new FileInputStream("transplant.obj");
            ObjectInputStream fin  = new ObjectInputStream(file);
            transplantGraph = (TransplantGraph) fin.readObject();
            fin.close();
            file.close();
            System.out.println("Loading data to transplant.obj...");
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Loading data from 'donors.txt'...");
            System.out.println("Loading data from 'recipients.txt'...");
            transplantGraph = buildFromFiles(DONOR_FILE, RECIPIENT_FILE);
        }


        Scanner in = new Scanner(System.in);
        String menu = "\nMenu:\n" +
                "(LR) - List all recipients\n" +
                "(LO) - List all donors\n" +
                "(AO) - Add new donor\n" +
                "(AR) - Add new recipient\n" +
                "(RO) - Remove donor\n" +
                "(RR) - Remove recipient\n" +
                "(SR) - Sort recipients\n" +
                "(SO) - Sort donors\n" +
                "(Q) - Quit";
        String subMenu = "\t(I) Sort by ID\n" +
                "\t(N) Sort by Number of Donors\n" +
                "\t(B) Sort by Blood Type\n" +
                "\t(O) Sort by Organ Needed\n" +
                "\t(Q) Back to Main Menu\n";
        while (!in.equals("q")) {
            System.out.println(menu + "\n\n" + "Please enter an option:");
            String selection = in.next().toLowerCase();
            if (selection.equals("lr")) {
                transplantGraph.printAllRecipients();
            } else if (selection.equals("lo")) {
                transplantGraph.printAllDonors();
            } else if (selection.equals("ao")) {
                try {
                    System.out.println("Please enter the organ donor name:");
                    in.nextLine();
                    String name = in.nextLine();
                    System.out.println("Please enter the organs " + name + " is donating:");
                    String organ = in.nextLine();
                    System.out.println("Please enter the blood type of " + name+":");
                    BloodType bloodType = new BloodType(in.nextLine());
                    System.out.println("Please enter the age of " + name+":");
                    int age = in.nextInt();
                    Patient patient = new Patient(name, organ, age, bloodType, (transplantGraph.getDonors().size()), true);
                    transplantGraph.addDonor(patient);
                    System.out.println("The organ donor with ID "+ (transplantGraph.getDonors().size()-1)+" was successfully added to the donor list!");
                } catch (InputMismatchException ex) {
                    in.nextLine();
                    System.out.println("Make sure age is a number. ");
                }


            } else if (selection.equals("ar")) {
                try {
                    System.out.println("Please enter new recipient's name:");
                    in.nextLine();
                    String name = in.nextLine();
                    System.out.println("Please enter the recipient's blood type: ");
                    BloodType bloodType = new BloodType(in.next());
                    System.out.println("Please enter the recipient's age: ");
                    int age = in.nextInt();
                    System.out.println("Please enter the organ needed: ");
                    in.nextLine();
                    String organ = in.nextLine();
                    Patient patient = new Patient(name, organ, age, bloodType, (transplantGraph.getRecipients().size()), false);
                    transplantGraph.addRecipient(patient);
                    System.out.println(name+" is now on the organ transplant wailtlist!");
                } catch (InputMismatchException ex) {
                    in.nextLine();
                    System.out.println("Make sure age is a number.");
                }

            } else if (selection.equals("ro")) {
                boolean found = false;
                System.out.println("Please enter the name of the organ donor to remove:");
                in.nextLine();
                String name = in.nextLine();
                for (int i = 0; i < transplantGraph.getDonors().size(); i++) {
                    if (transplantGraph.getDonors().get(i).getName().toLowerCase().equals(name.toLowerCase())) {
                        System.out.println(transplantGraph.getDonors().get(i).getName() + " was removed from the organ donor list.");
                        transplantGraph.removeDonor(name);
                        found = true;

                    }
                }
                if (!found)
                    System.out.println(name + " not found in the organ donors list.");

            } else if (selection.equals("rr")) {
                boolean found = false;
                System.out.println("Please enter the name of the recipient to remove:");
                in.nextLine();
                String name = in.nextLine();
                for (int i = 0; i < transplantGraph.getRecipients().size(); i++) {
                    if (transplantGraph.getRecipients().get(i).getName().toLowerCase().equals(name.toLowerCase())) {
                        System.out.println(transplantGraph.getRecipients().get(i).getName()  + " was removed from the organ transplant waitlist.");
                        transplantGraph.removeRecipient(name);
                        found = true;
                    }
                }
                if (!found)
                    System.out.println(name + " not found in the organ transplant waitlist.");

            } else if (selection.equals("sr")) {
                do {
                    System.out.println(subMenu);
                    System.out.println("Please select an option:");
                    String option = in.next().toLowerCase();
                    if (option.equals("i")) {
                        Collections.sort(transplantGraph.getRecipients());
                        transplantGraph.resetConnections();
                        transplantGraph.printAllRecipients();
                    } else if (option.equals("n")) {
                        transplantGraph.setNumConnections();
                        Collections.sort(transplantGraph.getRecipients(), new NumConnectionsComparator());
                        transplantGraph.resetConnections();
                        transplantGraph.printAllRecipients();

                    } else if (option.equals("b")) {
                        Collections.sort(transplantGraph.getRecipients(), new BloodTypeComparator());
                        transplantGraph.resetConnections();
                        transplantGraph.printAllRecipients();

                    } else if (option.equals("o")) {
                        Collections.sort(transplantGraph.getRecipients(), new OrganComparator());
                        transplantGraph.resetConnections();
                        transplantGraph.printAllRecipients();

                    } else if (option.equals("q")) {
                        Collections.sort(transplantGraph.getRecipients());
                        transplantGraph.resetConnections();
                        System.out.println("Returning to main menu.");
                        break;
                    } else
                        System.out.println("Invalid Input");
                }
                while (!selection.equals("q"));

            } else if (selection.equals("so")) {
                do {
                    System.out.println(subMenu);
                    System.out.println("Please select an option:");
                    String option = in.next().toLowerCase();
                    if (option.equals("i")) {
                        Collections.sort(transplantGraph.getDonors());
                        transplantGraph.resetConnections();
                        transplantGraph.printAllDonors();
                    } else if (option.equals("n")) {
                        transplantGraph.setNumConnections();
                        Collections.sort(transplantGraph.getDonors(), new NumConnectionsComparator());
                        transplantGraph.resetConnections();
                        transplantGraph.printAllDonors();

                    } else if (option.equals("b")) {
                        Collections.sort(transplantGraph.getDonors(), new BloodTypeComparator());
                        transplantGraph.resetConnections();
                        transplantGraph.printAllDonors();

                    } else if (option.equals("o")) {
                        Collections.sort(transplantGraph.getDonors(), new OrganComparator());
                        transplantGraph.resetConnections();
                        transplantGraph.printAllDonors();

                    } else if (option.equals("q")) {
                        Collections.sort(transplantGraph.getDonors());
                        transplantGraph.resetConnections();
                        System.out.println("Returning to main menu.");
                        break;
                    } else
                        System.out.println("Invalid Input");
                }
                while (!selection.equals("q"));

            } else if (selection.equals("q")) {
                try {
                    FileOutputStream file = new FileOutputStream("transplant.obj");
                    ObjectOutputStream fout = new ObjectOutputStream(file);
                    fout.writeObject(transplantGraph);
                    fout.close();
                    System.out.println("Writing data to transplant.obj...\nProgram terminating normally...");
                    System.exit(0);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
                System.out.println("Invalid Input");


        }
    }
}