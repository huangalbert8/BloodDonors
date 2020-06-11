/**
 * @author Albert Huang ID#:112786492 Rec:R01
        * This class represents the bloodType of the patient that includes the bloodtype as a String.
        * Implents serializable so transplantGraph can be saved.
        */
import java.io.Serializable;

public class BloodType implements Serializable {
    private String bloodType;

    public BloodType(){

    }
    public BloodType(String bloodType){
        this.bloodType = bloodType;
    }

    public String getBloodType() {
        return bloodType;
    }

    /**
     * determines if the blood type of the recipient is compatible with the donor.
     * @param recipient recipient to be checked.
     * @param donor donor to be checked.
     * @return true is bloodtypes are compatible, false if is not.
     */
    public static boolean isCompatible(BloodType recipient, BloodType donor){
        if(donor.getBloodType().toLowerCase().equals("o"))
            return true;
        else if(donor.getBloodType().toLowerCase().equals("a")){
            if(recipient.getBloodType().toLowerCase().equals("o")||recipient.getBloodType().toLowerCase().equals("b"))
                return false;
            else
                return true;
        }else if(donor.getBloodType().toLowerCase().equals("b")){
            if(recipient.getBloodType().toLowerCase().equals("o")||recipient.getBloodType().toLowerCase().equals("a"))
                return false;
            else
                return true;
        }else if(donor.getBloodType().toLowerCase().equals("ab")){
            if(recipient.getBloodType().toLowerCase().equals("o")||recipient.getBloodType().toLowerCase().equals("b")||recipient.getBloodType().toLowerCase().equals("a"))
                return false;
            else
                return true;
        }
        else return false;
    }
}
