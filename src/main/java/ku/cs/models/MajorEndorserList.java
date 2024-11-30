package ku.cs.models;

import java.util.ArrayList;

public class MajorEndorserList {
    private ArrayList<MajorEndorser> majorEndorsers;

    public MajorEndorserList() {majorEndorsers = new ArrayList<>();}

    public void addNewMajorEndorser(String name, String position) {
        name = name.trim();
        position = position.trim();
        for (MajorEndorser majorEndorser : majorEndorsers) {
            if (majorEndorser.getName().equalsIgnoreCase(name)){
                return;
            }
        }
        majorEndorsers.add(new MajorEndorser(name, position));
    }

    public ArrayList<MajorEndorser> getMajorEndorsers() {
        return majorEndorsers;
    }
}
