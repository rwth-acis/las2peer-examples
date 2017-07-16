package i5.las2peer.services.databaseService;

import java.util.logging.Level;

import i5.las2peer.api.ManualDeployment;
import i5.las2peer.api.ServiceException;
import i5.las2peer.restMapper.RESTService;
import i5.las2peer.restMapper.annotations.ServicePath;
import i5.las2peer.services.databaseService.database.DatabaseManager;

/**
 * las2peer Service
 * 
 * This is a example for a very basic las2peer service that uses the las2peer Web-Connector for RESTful access to it.
 * 
 * 
 */
@ServicePath("/database")
@ManualDeployment
public class DatabaseService extends RESTService {

	// database configuration
	private String jdbcDriverClassName;
	private String jdbcLogin;
	private String jdbcPass;
	private String jdbcUrl;
	private String jdbcSchema;
	private DatabaseManager dbm;
	
	@Override
	public void onStart() throws ServiceException {
		// read and set properties values
		// IF THE SERVICE CLASS NAME IS CHANGED, THE PROPERTIES FILE NAME NEED TO BE CHANGED TOO!
		setFieldValues();

		// instantiate a database manager to handle database connection pooling and credentials
		dbm = new DatabaseManager(jdbcDriverClassName, jdbcLogin, jdbcPass, jdbcUrl, jdbcSchema);
		
		getLogger().log(Level.INFO, "Service started");
	}

	@Override
	protected void initResources() {
		// register resources here
		getResourceConfig().register(DatabaseResource.class);
	}

	public DatabaseManager getDatabaseManager() {
		return this.dbm;
	}

}
