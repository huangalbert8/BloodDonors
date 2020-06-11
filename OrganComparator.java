/**
 * * @author Albert Huang ID#:112786492 Rec:R01
 * This class represents the NumConnectionsComparator and implements comparator so the transplantGraph's table can
 * be sorted by organs.
 */
import java.util.Comparator;

public class OrganComparator implements Comparator {
    public int compare(Object o1, Object o2){
        Patient p1 = (Patient)o1;
        Patient p2 = (Patient)o2;
        return (p1.getOrgan().toLowerCase().compareTo(p2.getOrgan().toLowerCase()));

    }
}
