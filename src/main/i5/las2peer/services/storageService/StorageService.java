package i5.las2peer.services.storageService;

import java.util.logging.Level;

import i5.las2peer.api.Context;
import i5.las2peer.api.Service;
import i5.las2peer.api.logging.MonitoringEvent;
import i5.las2peer.api.persistency.Envelope;

/**
 * This class is a LAS2peer service example. It shows how a service should store and fetch objects in network storage.
 *
 */
public class StorageService extends Service {

	/**
	 * This method stores an object inside the LAS2peer network storage. The type of the object is not limited, any
	 * class that implements the {@link java.io.Serializable} interface can be used.
	 * 
	 * @param identifier This identifier is used to identify the stored object inside the network.
	 * @param object The object that should actually be stored in network storage.
	 */
	public void persistObject(String identifier, MyStorageObject object) {
		try {
			Envelope env = null;
			try {
				// fetch existing container object from network storage
				env = Context.get().requestEnvelope(identifier);
				// place the new object inside container
				env.setContent(object);
			} catch (Exception e) {
				// write info message to logfile and console
				Context.get().getLogger(this.getClass()).log(Level.INFO, "Network storage container not found. Creating new one. " + e.toString());
				// create new container object with current ServiceAgent as owner
				env = Context.get().createEnvelope(identifier);
				env.setContent(object);
			}
			// upload the updated storage container back to the network
			Context.get().storeEnvelope(env);
		} catch (Exception e) {
			// write error to logfile and console
			Context.get().getLogger(this.getClass()).log(Level.SEVERE, "Can't persist to network storage!", e);
			// create and publish a monitoring message
			Context.get().monitorEvent(this, MonitoringEvent.SERVICE_ERROR, e.toString());
		}
	}

	/**
	 * This method fetches an object from the LAS2peer network storage. The return type is not limited, any class that
	 * implements the {@link java.io.Serializable} interface can be used.
	 * 
	 * @param identifier This identifier is used to identify the storage object inside the network.
	 * @return Returns the fetched object or null if an error occurred.
	 */
	public MyStorageObject fetchObject(String identifier) {
		try {
			// fetch existing container object from network storage
			Envelope env = Context.get().requestEnvelope(identifier);
			// deserialize content from envelope
			MyStorageObject retrieved = (MyStorageObject) env.getContent();
			return retrieved;
		} catch (Exception e) {
			// write error to logfile and console
			Context.get().getLogger(this.getClass()).log(Level.SEVERE, "Can't fetch from network storage!", e);
			// create and publish a monitoring message
			Context.get().monitorEvent(this, MonitoringEvent.SERVICE_ERROR, e.toString());
		}
		return null;
	}

}
