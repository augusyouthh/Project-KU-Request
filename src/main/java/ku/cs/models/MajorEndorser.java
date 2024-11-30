package ku.cs.models;

public class MajorEndorser {
    String name;
    String position;

    public MajorEndorser(String name, String position) {
        this.name = name;
        this.position = position;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }


}
