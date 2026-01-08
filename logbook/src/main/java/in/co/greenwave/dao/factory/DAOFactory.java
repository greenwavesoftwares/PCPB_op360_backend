package in.co.greenwave.dao.factory; // Package declaration for the DAO Factory class

// Importing the necessary DAO interfaces for use in the factory
import in.co.greenwave.dao.LogbookConfigureService;
import in.co.greenwave.dao.LogbookReportService; // Importing service for logbook report handling
import in.co.greenwave.dao.LogbookService; // Importing service for logbook operations

// Abstract class DAOFactory serves as a blueprint for creating instances of various DAO services
public abstract class DAOFactory {

    // Abstract method to obtain an instance of LogbookDAO. 
    // Concrete subclasses must implement this method to provide the actual service.
    public abstract LogbookService getLogbookService();

    // Abstract method to obtain an instance of AssetConfigurationDAO.
    // Subclasses must define the implementation for providing the actual service.
    public abstract LogbookReportService getLogbookReportService();

    // Abstract method to obtain an instance of RestDAO.
    // Concrete subclasses are responsible for implementing this method.
    public abstract LogbookConfigureService getLogbookConfigureService();
}
