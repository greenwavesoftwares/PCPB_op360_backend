package in.co.greenwave.assetapi.model; // This line tells where the class belongs in the project

// This class represents information about a logbook
public class LogbookInfo {

    // Variables to store the logbook's name and version number
    private String lookbookname; // The name of the logbook
    private int versionNumber;    // The version number of the logbook

    // This is a special method called a constructor. It helps create a new LogbookInfo object with a name and version number.
    public LogbookInfo(String lookbookname, int versionNumber) {
        super(); // Calls the parent class constructor (not usually needed here)
        this.lookbookname = lookbookname; // Sets the name of the logbook
        this.versionNumber = versionNumber; // Sets the version number of the logbook
    }

    // This method gets the name of the logbook
    public String getLookbookname() {
        return lookbookname; // Returns the logbook's name
    }

    // This method allows you to change the name of the logbook
    public void setLookbookname(String lookbookname) {
        this.lookbookname = lookbookname; // Updates the logbook's name
    }

    // This method gets the version number of the logbook
    public int getVersionNumber() {
        return versionNumber; // Returns the version number
    }

    // This method allows you to change the version number of the logbook
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber; // Updates the version number
    }
}
