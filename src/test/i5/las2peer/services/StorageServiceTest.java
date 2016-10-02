package i5.las2peer.services;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import i5.las2peer.p2p.PastryNodeImpl;
import i5.las2peer.p2p.ServiceNameVersion;
import i5.las2peer.security.ServiceAgent;
import i5.las2peer.services.storageService.MyStorageObject;
import i5.las2peer.services.storageService.StorageService;
import i5.las2peer.testing.TestSuite;
import i5.las2peer.tools.ColoredOutput;

/**
 * This class checks the functionality of the StorageService example.
 *
 */
public class StorageServiceTest {

	private static final String TEST_STORAGE_ID = "test-storage";

	private static PastryNodeImpl storageServiceNode;
	private static ServiceAgent storageService;

	@BeforeClass
	public static void init() {
		try {
			System.out.println("starting test network...");
			ColoredOutput.allOff();
			// start storage service node as standalone network
			storageServiceNode = TestSuite.launchNetwork(1).get(0);
			// start storage service
			storageService = ServiceAgent.createServiceAgent(
					new ServiceNameVersion(StorageService.class.getName(), "1.0"), "test-service-pass");
			storageService.unlockPrivateKey("test-service-pass");
			storageServiceNode.registerReceiver(storageService);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@AfterClass
	public static void stopNetwork() {
		System.out.println("stopping test network...");
		storageServiceNode.shutDown();
	}

	@Test
	public void testStorage() throws Exception {
		// generate an unique identifier, use a better/safer algorithm for your own service!
		String identifier = TEST_STORAGE_ID + new Random().nextInt();
		// this is the test object that will be persisted
		MyStorageObject exampleObj = new MyStorageObject("Hello world!");
		storageServiceNode.invoke(storageService,
				new ServiceNameVersion(StorageService.class.getCanonicalName(), "1.0"), "persistObject",
				new Serializable[] { identifier, exampleObj });
		// retrieve test object again from network
		MyStorageObject result = (MyStorageObject) storageServiceNode.invoke(storageService,
				new ServiceNameVersion(StorageService.class.getCanonicalName(), "1.0"), "fetchObject",
				new Serializable[] { identifier });
		System.out.println("Success! Received test object with message: " + result.getMsg());
		assertEquals(exampleObj.getMsg(), result.getMsg());
	}

}
