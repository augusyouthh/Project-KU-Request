package ku.cs.services;

import ku.cs.models.AppealList;
import ku.cs.models.Appeal;

public class AppealSharedData {
    private static AppealList appealList = new AppealList();
    private static Appeal selectedAppeal;

    public static AppealList getNormalAppealList(){
        return appealList;
    }

    public static void setNormalAppealList(AppealList list) {
        appealList = list;
    }

    public static void setSelectedAppeal(Appeal appeal) {
        selectedAppeal = appeal;
    }
    public static Appeal getSelectedAppeal() {
        return selectedAppeal;
    }

}
