/**
 * @author Albert Huang ID#:112786492 Rec:R01
 * This class represents the TransplantGraph that consists of arraylist of donors and recipients, a
 * two dimensional boolean array of connections, and a static variable of the max patients which is 100.
 * Implents serializable so transplantGraph can be saved.
 */
import java.io.*;
import java.util.ArrayList;

public class TransplantGraph extends Patient implements Serializable{
    private ArrayList<Patient> donors;
    private ArrayList<Patient> recipients;
    private boolean[][] connections;
    public static int MAX_PATIENTS = 100;

    public TransplantGraph(){
        donors = new ArrayList<>();
        recipients = new ArrayList<>();
        connections = new boolean[MAX_PATIENTS][MAX_PATIENTS];
    }

    /**
     * builds the table from the files
     * @param donorFile file to be added into donor table.
     * @param recipientFile file to be added into recipient table.
     * @return a TransplantGraph with the new tables.
     * @throws IOException if there is issue reading files.
     */
    public static TransplantGraph buildFromFiles(String donorFile, String recipientFile) throws IOException {
        FileInputStream donor = new FileInputStream(donorFile);
        FileInputStream recip = new FileInputStream(recipientFile);
        InputStreamReader dInStream = new InputStreamReader(donor);
        InputStreamReader rInStream = new InputStreamReader(recip);
        BufferedReader rReader = new BufferedReader(rInStream);
        BufferedReader dReader = new BufferedReader(dInStream);
        String rData;
        String dData;
        TransplantGraph transplantGraph = new TransplantGraph();
        String[][] rArray = new String[100][100];
        int rRowCount = 0;
        while((rData = rReader.readLine())!=null){
            int rColCount = 0;
            while(rData.indexOf(',')>0){
                rArray[rRowCount][rColCount] = rData.substring(0,rData.indexOf(','));
                rData = rData.substring(rData.indexOf(',')+2);
                rColCount++;
            }
            rArray[rRowCount][rColCount] = rData;
            rRowCount++;
        }
        for(int i = 0; i < rRowCount; i++){
            BloodType bloodType = new BloodType(rArray[i][4]);
            Patient recipient = new Patient(rArray[i][1],rArray[i][3],Integer.parseInt(rArray[i][2]),bloodType,Integer.parseInt(rArray[i][0]),false);
            transplantGraph.addRecipient(recipient);
        }

        String[][] dArray = new String[100][100];
        int dRowCount = 0;
        while((dData = dReader.readLine())!=null){
            int dColCount = 0;
            while(dData.indexOf(',')>0){
                dArray[dRowCount][dColCount] = dData.substring(0,dData.indexOf(','));
                dData = dData.substring(dData.indexOf(',')+2);
                dColCount++;
            }
            dArray[dRowCount][dColCount] = dData;
            dRowCount++;
        }
        for(int j = 0; j < dRowCount;j++){
            BloodType bloodType = new BloodType(dArray[j][4]);
            Patient don = new Patient(dArray[j][1],dArray[j][3],Integer.parseInt(dArray[j][2]),bloodType,Integer.parseInt(dArray[j][0]),true);
            transplantGraph.addDonor(don);
        }
        return transplantGraph;
    }


    public ArrayList<Patient> getDonors() {
        return donors;
    }

    public ArrayList<Patient> getRecipients() {
        return recipients;
    }

    /**
     * counts the number of connections that each patient has and sets them each
     * patient's number of connections.
     */
    public void setNumConnections() {
        for (int i = 0; i < recipients.size(); i++) {
            int count = 0;
            for (int j = 0; j < donors.size(); j++) {
                if (connections[j][i]) {
                    count++;
                }
            }
            recipients.get(i).setNumConnections(count);
        }
        for (int i = 0; i < donors.size(); i++) {
            int count = 0;
            for (int j = 0; j < recipients.size(); j++) {
                if (connections[i][j]) {
                    count++;
                }
            }
            donors.get(i).setNumConnections(count);
        }
    }

    /**
     * After the tableGraph is sorted, the connections are not adjusted so this method resets the
     * two dimensional matrix to each sort.
     */
    public void resetConnections(){
        for (int i = 0; i < donors.size(); i++) {
            for (int j = 0; j < recipients.size(); j++) {
                if (isCompatible(recipients.get(j).getBloodType1(), donors.get(i).getBloodType1()) && donors.get(i).getOrgan().toLowerCase().equals(recipients.get(j).getOrgan().toLowerCase())) {
                    connections[i][j] = true;
                } else connections[i][j] = false;
            }
        }
    }

    /**
     * adds recipient into the arraylist and updates the boolean matrix accordingly.
     * @param patient the patient that is to be added into the recipient list.
     */
    public void addRecipient(Patient patient){
        for(int i = 0;i < donors.size();i++){
        if(isCompatible(patient.getBloodType1(),donors.get(i).getBloodType1())&&donors.get(i).getOrgan().toLowerCase().equals(patient.getOrgan().toLowerCase())){
            connections[i][patient.getID()] = true; }
       else
            connections[i][patient.getID()] = false;}
        recipients.add(patient);
    }

    /**
     * adds donor into the arraylist and updates the boolean matrix accordingly.
     * @param patient the patient that is to be added into the donor list.
     */
    public void addDonor(Patient patient){
        for(int i = 0; i < recipients.size();i++){
            if(isCompatible(recipients.get(i).getBloodType1(),patient.getBloodType1())&&recipients.get(i).getOrgan().toLowerCase().equals(patient.getOrgan().toLowerCase())){
                connections[patient.getID()][i] = true;
            }
            else {
                connections[patient.getID()][i] = false;
            }
        }donors.add(patient);
    }

    /**
     * removes the recipient given a name and updates connections.
     * @param name the name of the recipient that is to be removed.
     */
    public void removeRecipient(String name){
        ArrayList<Patient> tempPatient = new ArrayList<>();
        for(int i = 0; i<recipients.size();i++){
            if(recipients.get(i).getName().toLowerCase().equals(name.toLowerCase())) {
                recipients.remove(i);
                int count = 0;
                for(int j = i; j < recipients.size();){
                    recipients.get(j).setID(i+count);
                    tempPatient.add(recipients.get(j));
                    recipients.remove(j);
                    count++;
                }
                for(int k = 0; k < tempPatient.size();k++)
                    addRecipient(tempPatient.get(k));
                break;
            }
        }
    }

    /**
     * removes the donor given a name and updates connections.
     * @param name the name of the donor to be removed.
     */
    public void removeDonor(String name) {
        ArrayList<Patient> tempPatient = new ArrayList<>();
        for(int i = 0; i<donors.size();i++){
            if(donors.get(i).getName().toLowerCase().equals(name.toLowerCase())) {
                donors.remove(i);
                int count = 0;
                for(int j = i; j < donors.size();){
                    donors.get(j).setID(i+count);
                    tempPatient.add(donors.get(j));
                    donors.remove(j);
                    count++;
                }
                for(int k = 0; k < tempPatient.size();k++)
                    addDonor(tempPatient.get(k));
                break;
            }
        }
    }

    /**
     * prints all of the recipients in a table with index, name, age, organ, bloodtype, and donor connections.
     */
    public void printAllRecipients() {
        setNumConnections();
        String s = "Index | Recipient Name     | Age | Organ Needed | Blood Type | Donor IDs\n" +
                "=========================================================================\n";
        for(int i = 0; i<recipients.size(); i++){
            s+=recipients.get(i).toString();
            ArrayList<Integer> recipientID = new ArrayList<>();
            for(int j = 0; j<donors.size();j++) {
                if (connections[j][i]) {
                    recipientID.add(j);
                }
            }
            if(recipientID.size()==1)
                s+=" "+recipientID.get(0)+"\n";

            else if(recipientID.size()>1){
                s+=" "+recipientID.get(0);
                for(int k = 1; k <= recipientID.size()-1; k++)
                    s+=", "+recipientID.get(k);
                s+="\n";}
            else
                s+="\n";
            }
        System.out.println(s);
            }

    /**
     *      * prints all of the donors in a table with index, name, age, organ, bloodtype, and donor connections.
     */
            public void printAllDonors() {
                String s = "Index | Donor Name         | Age | Organ Donated | Blood Type | Recipient IDs\n" +
                        "=========================================================================\n";
                for(int i = 0; i<donors.size(); i++){
                    s+=donors.get(i).toString();
                    ArrayList<Integer> donorID = new ArrayList<>();
                    for(int j = 0; j<recipients.size();j++) {
                        if (connections[i][j]) {
                            donorID.add(j);
                        }
                    }
                    if(donorID.size()==1)
                        s+=" "+donorID.get(0)+"\n";
                    else if(donorID.size()>1){
                        s+=" "+donorID.get(0);
                        for(int k = 1; k <= donorID.size()-1; k++)
                            s+=", "+donorID.get(k);
                        s+="\n";}
                    else
                        s+="\n";
                    }System.out.println(s);
                }

            }

