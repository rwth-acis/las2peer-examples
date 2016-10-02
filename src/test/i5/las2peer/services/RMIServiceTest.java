package i5.las2peer.services;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import i5.las2peer.p2p.PastryNodeImpl;
import i5.las2peer.p2p.ServiceNameVersion;
import i5.las2peer.security.ServiceAgent;
import i5.las2peer.services.rmi.RMIMyService;
import i5.las2peer.services.rmiForeign.RMIForeignService;
import i5.las2peer.testing.TestSuite;
import i5.las2peer.tools.ColoredOutput;

public class RMIServiceTest {

	private static PastryNodeImpl foreignServiceNode;
	private static PastryNodeImpl myServiceNode;
	private static ServiceAgent foreignService;
	private static ServiceAgent myService;

	@BeforeClass
	public static void init() {
		try {
			System.out.println("starting test network...");
			ColoredOutput.allOff();
			ArrayList<PastryNodeImpl> nodes = TestSuite.launchNetwork(2);
			// start foreign service node as standalone network
			foreignServiceNode = nodes.get(0);
			// start foreign service
			foreignService = ServiceAgent.createServiceAgent(
					new ServiceNameVersion(RMIForeignService.class.getName(), "1.0"), "test-service-pass");
			foreignService.unlockPrivateKey("test-service-pass");
			foreignServiceNode.registerReceiver(foreignService);
			// start developer defined service that uses the foreign service for RMI calls
			myServiceNode = nodes.get(1);
			// start developer defined service
			myService = ServiceAgent.createServiceAgent(new ServiceNameVersion(RMIMyService.class.getName(), "1.0"),
					"test-service-pass");
			myService.unlockPrivateKey("test-service-pass");
			myServiceNode.registerReceiver(myService);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@AfterClass
	public static void stopNetwork() {
		System.out.println("stopping test network...");
		foreignServiceNode.shutDown();
		myServiceNode.shutDown();
	}

	@Test
	public void testRMIOne() {
		// trigger method call in developer defined service
		try {
			String result = (String) myServiceNode.invoke(myService,
					new ServiceNameVersion(RMIMyService.class.getName(), "1.0"), "callRMIOne", new Serializable[] {});
			System.out.println("The RMI call returned: " + result);
			assertEquals(result, RMIForeignService.TEST_STRING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRMITwo() {
		// trigger method call in developer defined service
		try {
			int result = (int) myServiceNode.invoke(myService,
					new ServiceNameVersion(RMIMyService.class.getName(), "1.0"), "callRMITwo", new Serializable[] {});
			System.out.println("The RMI call returned: " + result);
			assertEquals(result, RMIMyService.TEST_RESULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
