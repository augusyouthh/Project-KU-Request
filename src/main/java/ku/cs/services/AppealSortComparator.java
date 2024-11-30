package ku.cs.services;
import ku.cs.models.Appeal;

import java.util.Comparator;

public class AppealSortComparator implements Comparator<Appeal>{
    private String filterType;


    public AppealSortComparator(String filterType) {
        this.filterType = filterType;
    }

    @Override
    public int compare(Appeal o1, Appeal o2) {
        if (filterType != null) {
            int typeComparison = o1.getType().compareTo(o2.getType());
            if (typeComparison != 0) {
                return typeComparison;
            }
        }

        return (int) (o2.getSecond() - o1.getSecond());
    }

}
