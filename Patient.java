/**
 * @author Albert Huang ID#:112786492 Rec:R01
 * This class represents the patient which consists name, organ, age, bloodtype, ID,
 * if it is a donor, and my own data member that keeps count of the number of connections
 * that each patient has. Implements comparable to be used in compareTo method. Implements
 * serializable so the contents of the transplant graph can be saved.
 */
import java.io.Serializable;

public class Patient extends BloodType implements Comparable, Serializable {
    private String name, organ;
    private int age;
    private BloodType bloodType;
    private int ID;
    private boolean isDonor;
    private int numConnections=0;

    public Patient(){
    }

    public Patient(String name, String organ, int age, BloodType bloodType, int ID, boolean isDonor){
        this.name = name;
        this.organ = organ;
        this.age = age;
        this.bloodType = bloodType;
        this.ID = ID;
        this.isDonor = isDonor;
    }
    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getOrgan() {
        return organ;
    }

    /**
     * Different from the getBloodType variable from the bloodType class because the
     * return type is bloodType.
     * @return patient's bloodType that is not a string.
     */
    public BloodType getBloodType1() {
        return bloodType;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public void setNumConnections(int numConnections){
        this.numConnections = numConnections;
    }

    public int getNumConnections() {
        return numConnections;
    }

    /**
     * compareTo method that compares the IDs of the patients to be used to sort.
     * @param o the other patient to compare with
     * @return 0 if IDs are equal, 1 if ID is greater, -1 if ID is less.
     */
    public int compareTo(Object o){
        Patient otherPat = (Patient)o;
        if (this.ID == otherPat.getID())
            return 0;
        else if (this.ID > otherPat.getID())
            return 1;
        else
            return -1;
    }

    /**
     * toString method to be used in the TransplantGraph class.
     * @return String used in in creating the table.
     */
    public String toString(){
        String s = "";
        if(isDonor){
            s = String.format("   %-3d| %-19s| %-4d| %-14s|     %-7s|",ID,name,age,organ,bloodType.getBloodType());}
        else{
            s = String.format("   %-3d| %-19s| %-4d|  %-13s|    %-8s|",ID,name,age,organ,bloodType.getBloodType());}
        return s;
    }


}
