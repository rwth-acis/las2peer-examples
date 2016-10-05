package i5.las2peer.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import i5.las2peer.p2p.LocalNode;
import i5.las2peer.p2p.ServiceNameVersion;
import i5.las2peer.security.ServiceAgent;
import i5.las2peer.security.UserAgent;
import i5.las2peer.services.restService.RESTExampleService;
import i5.las2peer.testing.MockAgentFactory;
import i5.las2peer.webConnector.WebConnector;
import i5.las2peer.webConnector.client.ClientResponse;
import i5.las2peer.webConnector.client.MiniClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Example Test Class demonstrating a basic JUnit test structure.
 *
 */
public class RESTExampleServiceTest {

	private static final String HTTP_ADDRESS = "http://127.0.0.1";
	private static int HTTP_PORT = WebConnector.DEFAULT_HTTP_PORT;

	private static LocalNode node;
	private static WebConnector connector;
	private static ByteArrayOutputStream logStream;

	private static UserAgent testAgent;
	private static final String testPass = "adamspass";

	// during testing, the specified service version does not matter
	private static final ServiceNameVersion testExampleService = new ServiceNameVersion(
			RESTExampleService.class.getCanonicalName(), "1.0");

	private static final String mainPath = "example/";

	private static int getFreePort() {
		int port = HTTP_PORT;
		try {
			ServerSocket socket = new ServerSocket(0);
			port = socket.getLocalPort();
			socket.close();
		} catch (IOException e) {
		}
		return port;
	}

	/**
	 * Called before the tests start.
	 * 
	 * Sets up the node and initializes connector and users that can be used throughout the tests.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void startServer() throws Exception {
		HTTP_PORT = getFreePort();

		// start node
		node = LocalNode.newNode();
		testAgent = MockAgentFactory.getAdam();
		testAgent.unlockPrivateKey(testPass); // agent must be unlocked in order to be stored
		node.storeAgent(testAgent);
		node.launch();

		ServiceAgent testService = ServiceAgent.createServiceAgent(testExampleService, "a pass");
		testService.unlockPrivateKey("a pass");

		node.registerReceiver(testService);

		// start connector
		logStream = new ByteArrayOutputStream();

		connector = new WebConnector(true, HTTP_PORT, false, 1000);
		connector.setLogStream(new PrintStream(logStream));
		connector.start(node);
		Thread.sleep(1000); // wait a second for the connector to become ready
		testAgent = MockAgentFactory.getAdam(); // get a locked agent

	}

	/**
	 * Called after the tests have finished. Shuts down the server and prints out the connector log file for reference.
	 * 
	 * @throws Exception
	 */
	@AfterClass
	public static void shutDownServer() throws Exception {

		connector.stop();
		node.shutDown();

		connector = null;
		node = null;

		LocalNode.reset();

		System.out.println("Connector-Log:");
		System.out.println("--------------");

		System.out.println(logStream.toString());

	}

	// MemberResource

	@Test
	public void testGetUserName() {
		MiniClient c = new MiniClient();
		c.setAddressPort(HTTP_ADDRESS, HTTP_PORT);
		c.setLogin(Long.toString(testAgent.getId()), testPass);

		ClientResponse result = c.sendRequest("GET", mainPath + "login", "");
		assertEquals(200, result.getHttpCode());
		assertTrue(result.getResponse().trim().contains("adam")); // login name is part of response
	}

	@Test
	public void testEcho() {
		MiniClient c = new MiniClient();
		c.setAddressPort(HTTP_ADDRESS, HTTP_PORT);
		String content = "myContent";
		c.setLogin(Long.toString(testAgent.getId()), testPass);

		ClientResponse result = c.sendRequest("POST", mainPath + "echo", content);
		assertEquals(200, result.getHttpCode());
		assertTrue(result.getResponse().trim().equals(content));
	}

	// VideoResource

	@Test
	public void testGetVideo() {
		MiniClient c = new MiniClient();
		c.setAddressPort(HTTP_ADDRESS, HTTP_PORT);
		c.setLogin(Long.toString(testAgent.getId()), testPass);

		ClientResponse result = c.sendRequest("GET", mainPath + "video/1", "");
		assertEquals(200, result.getHttpCode());
		assertTrue(result.getResponse().trim().contains("Gewitter"));
	}

	@Test
	public void testCreateVideo() {
		MiniClient c = new MiniClient();
		c.setAddressPort(HTTP_ADDRESS, HTTP_PORT);
		c.setLogin(Long.toString(testAgent.getId()), testPass);

		String data = "{\"title\": \"Title\", \"description\": \"desc\", \"uri\": \"http://my.video/uri\"}";

		ClientResponse result = c.sendRequest("POST", mainPath + "video", data, "application/json", "*/*",
				new HashMap<>());
		assertEquals(201, result.getHttpCode());
		assertTrue(result.getResponse().trim().contains(mainPath + "video/3"));
	}

	// ActorResource

	@Test
	public void testGetActorList() {
		MiniClient c = new MiniClient();
		c.setAddressPort(HTTP_ADDRESS, HTTP_PORT);
		c.setLogin(Long.toString(testAgent.getId()), testPass);

		ClientResponse result = c.sendRequest("GET", mainPath + "video/1/actor", "");
		assertEquals(200, result.getHttpCode());
		assertTrue(result.getResponse().trim().contains("Florentin"));
	}

	@Test
	public void testGetActor() {
		MiniClient c = new MiniClient();
		c.setAddressPort(HTTP_ADDRESS, HTTP_PORT);
		c.setLogin(Long.toString(testAgent.getId()), testPass);

		ClientResponse result = c.sendRequest("GET", mainPath + "video/1/actor/eljasper", "");
		assertEquals(200, result.getHttpCode());
		assertTrue(result.getResponse().trim().contains("eljasper"));
	}

}
