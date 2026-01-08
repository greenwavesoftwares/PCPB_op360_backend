package in.co.greenwave.assetapi.dao.factory; // Package declaration for the DAO Factory class

// Importing the necessary DAO interfaces for use in the factory
import in.co.greenwave.assetapi.dao.AssetConfigurationDAO; // Interface for AssetConfigurationDAO
import in.co.greenwave.assetapi.dao.LogbookDAO; // Interface for LogbookDAO
import in.co.greenwave.assetapi.dao.RestDAO; // Interface for RestDAO

// Abstract class DAOFactory serves as a blueprint for creating instances of various DAO services
public abstract class DAOFactory {

    // Abstract method to obtain an instance of LogbookDAO. 
    // Concrete subclasses must implement this method to provide the actual service.
    public abstract LogbookDAO getLogbookService();

    // Abstract method to obtain an instance of AssetConfigurationDAO.
    // Subclasses must define the implementation for providing the actual service.
    public abstract AssetConfigurationDAO getAssetConfigurationService();

    // Abstract method to obtain an instance of RestDAO.
    // Concrete subclasses are responsible for implementing this method.
    public abstract RestDAO getRestfulService();
}
