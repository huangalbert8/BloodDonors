/**
 * * @author Albert Huang ID#:112786492 Rec:R01
 * This class represents the BloodTypeComparator and implements comparator so the transplantGraph's table can
 * be sorted by blood type.
 */
import java.util.Comparator;
public class BloodTypeComparator implements Comparator{
    public int compare(Object o1, Object o2){
        Patient p1 = (Patient)o1;
        Patient p2 = (Patient)o2;
        return (p1.getBloodType1().getBloodType().toLowerCase().compareTo(p2.getBloodType1().getBloodType().toLowerCase()));

    }
}
