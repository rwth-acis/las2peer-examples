package i5.las2peer.services.databaseService;

import i5.las2peer.logging.L2pLogger;
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
public class DatabaseService extends RESTService {

	// instantiate the logger class
	private final L2pLogger logger = L2pLogger.getInstance(DatabaseService.class.getName());

	// database configuration
	private String jdbcDriverClassName;
	private String jdbcLogin;
	private String jdbcPass;
	private String jdbcUrl;
	private String jdbcSchema;
	private DatabaseManager dbm;

	public DatabaseService() {
		super();

		// read and set properties values
		// IF THE SERVICE CLASS NAME IS CHANGED, THE PROPERTIES FILE NAME NEED TO BE CHANGED TOO!
		setFieldValues();

		// instantiate a database manager to handle database connection pooling and credentials
		dbm = new DatabaseManager(jdbcDriverClassName, jdbcLogin, jdbcPass, jdbcUrl, jdbcSchema);
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
