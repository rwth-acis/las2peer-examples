package i5.las2peer.services.databaseService;

import i5.las2peer.execution.L2pThread;
import i5.las2peer.logging.L2pLogger;
import i5.las2peer.logging.NodeObserver.Event;
import i5.las2peer.services.databaseService.database.DatabaseManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;

import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.minidev.json.JSONObject;

@Api
@SwaggerDefinition(
		info = @Info(
				title = "las2peer Example Service",
				version = "0.1",
				description = "A las2peer Example Service for demonstration purposes.",
				termsOfService = "http://your-terms-of-service-url.com",
				contact = @Contact(
						name = "John Doe",
						url = "provider.com",
						email = "john.doe@provider.com"),
				license = @License(
						name = "your software license name",
						url = "http://your-software-license-url.com")))
@Path("/")
public class DatabaseResource {

	// instantiate the logger class
	private final L2pLogger logger = L2pLogger.getInstance(DatabaseService.class.getName());

	/**
	 * Example method that shows how to retrieve a user email address from a database and return an HTTP response
	 * including a JSON object.
	 * 
	 * WARNING: THIS METHOD IS ONLY FOR DEMONSTRATIONAL PURPOSES!!! IT WILL REQUIRE RESPECTIVE DATABASE TABLES IN THE
	 * BACKEND, WHICH DON'T EXIST IN THE EXAMPLE.
	 * 
	 * @param username The username for which the email address should be retrieved.
	 * @return Returns an HTTPresponse with status code and message.
	 */
	@GET
	@Path("/userEmail/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponses(
			value = { @ApiResponse(
					code = HttpURLConnection.HTTP_OK,
					message = "User Email"), @ApiResponse(
					code = HttpURLConnection.HTTP_UNAUTHORIZED,
					message = "Unauthorized"), @ApiResponse(
					code = HttpURLConnection.HTTP_NOT_FOUND,
					message = "User not found"), @ApiResponse(
					code = HttpURLConnection.HTTP_INTERNAL_ERROR,
					message = "Internal Server Error") })
	@ApiOperation(
			value = "Email Address Administration",
			notes = "Example method that retrieves a user email address from a database."
					+ " WARNING: THIS METHOD IS ONLY FOR DEMONSTRATIONAL PURPOSES!!! "
					+ "IT WILL REQUIRE RESPECTIVE DATABASE TABLES IN THE BACKEND, WHICH DON'T EXIST IN THE EXAMPLE.")
	public Response getUserEmail(@PathParam("username") String username) {
		DatabaseManager dbm = ((DatabaseService) L2pThread.getCurrent().getServiceAgent().getServiceInstance())
				.getDatabaseManager();
		String result = "";
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		try {
			// get connection from connection pool
			conn = dbm.getConnection();

			// prepare statement
			stmnt = conn.prepareStatement("SELECT email FROM users WHERE username = ?;");
			stmnt.setString(1, username);

			// retrieve result set
			rs = stmnt.executeQuery();

			// process result set
			if (rs.next()) {
				result = rs.getString(1);

				// setup resulting JSON Object
				JSONObject ro = new JSONObject();
				ro.put("email", result);

				// return HTTP Response on success
				return Response.ok().entity(ro.toJSONString()).build();
			} else {
				result = "No result for username " + username;

				// return HTTP Response on error
				return Response.status(404).entity(result).build();
			}
		} catch (Exception e) {
			// return HTTP Response on error
			return Response.status(500).entity("Internal error: " + e.getMessage()).build();
		} finally {
			// free resources
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					// write error to logfile and console
					logger.log(Level.SEVERE, e.toString(), e);
					// create and publish a monitoring message
					L2pLogger.logEvent(this, Event.SERVICE_ERROR, e.toString());

					// return HTTP Response on error
					return Response.status(500).entity("Internal error: " + e.getMessage()).build();
				}
			}
			if (stmnt != null) {
				try {
					stmnt.close();
				} catch (Exception e) {
					// write error to logfile and console
					logger.log(Level.SEVERE, e.toString(), e);
					// create and publish a monitoring message
					L2pLogger.logEvent(this, Event.SERVICE_ERROR, e.toString());

					// return HTTP Response on error
					return Response.status(500).entity("Internal error: " + e.getMessage()).build();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					// write error to logfile and console
					logger.log(Level.SEVERE, e.toString(), e);
					// create and publish a monitoring message
					L2pLogger.logEvent(this, Event.SERVICE_ERROR, e.toString());

					// return HTTP Response on error
					return Response.status(500).entity("Internal error: " + e.getMessage()).build();
				}
			}
		}
	}

	/**
	 * Example method that shows how to change a user email address in a database.
	 * 
	 * WARNING: THIS METHOD IS ONLY FOR DEMONSTRATIONAL PURPOSES!!! IT WILL REQUIRE RESPECTIVE DATABASE TABLES IN THE
	 * BACKEND, WHICH DON'T EXIST IN THE EXAMPLE.
	 * 
	 * @param username The username which email address should be changed.
	 * @param email The new email address that should be set for the given username.
	 * @return Returns an HTTPresponse with status code and message.
	 */
	@POST
	@Path("/userEmail/{username}/{email}")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiResponses(
			value = { @ApiResponse(
					code = HttpURLConnection.HTTP_OK,
					message = "Update Confirmation"), @ApiResponse(
					code = HttpURLConnection.HTTP_UNAUTHORIZED,
					message = "Unauthorized"), @ApiResponse(
					code = HttpURLConnection.HTTP_INTERNAL_ERROR,
					message = "Internal Server Error") })
	@ApiOperation(
			value = "setUserEmail",
			notes = "Example method that changes a user email address in a database."
					+ " WARNING: THIS METHOD IS ONLY FOR DEMONSTRATIONAL PURPOSES!!! "
					+ "IT WILL REQUIRE RESPECTIVE DATABASE TABLES IN THE BACKEND, WHICH DON'T EXIST IN THE EXAMPLE.")
	public Response setUserEmail(@PathParam("username") String username, @PathParam("email") String email) {
		DatabaseManager dbm = ((DatabaseService) L2pThread.getCurrent().getServiceAgent().getServiceInstance())
				.getDatabaseManager();
		String result = "";
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		try {
			conn = dbm.getConnection();
			stmnt = conn.prepareStatement("UPDATE users SET email = ? WHERE username = ?;");
			stmnt.setString(1, email);
			stmnt.setString(2, username);
			int rows = stmnt.executeUpdate(); // same works for insert
			result = "Database updated. " + rows + " rows affected";

			// return
			return Response.ok().entity(result).build();
		} catch (Exception e) {
			// return HTTP Response on error
			return Response.status(500).entity("Internal error: " + e.getMessage()).build();
		} finally {
			// free resources if exception or not
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					// write error to logfile and console
					logger.log(Level.SEVERE, e.toString(), e);
					// create and publish a monitoring message
					L2pLogger.logEvent(this, Event.SERVICE_ERROR, e.toString());

					// return HTTP Response on error
					return Response.status(500).entity("Internal error: " + e.getMessage()).build();
				}
			}
			if (stmnt != null) {
				try {
					stmnt.close();
				} catch (Exception e) {
					// write error to logfile and console
					logger.log(Level.SEVERE, e.toString(), e);
					// create and publish a monitoring message
					L2pLogger.logEvent(this, Event.SERVICE_ERROR, e.toString());

					// return HTTP Response on error
					return Response.status(500).entity("Internal error: " + e.getMessage()).build();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					// write error to logfile and console
					logger.log(Level.SEVERE, e.toString(), e);
					// create and publish a monitoring message
					L2pLogger.logEvent(this, Event.SERVICE_ERROR, e.toString());

					// return HTTP Response on error
					return Response.status(500).entity("Internal error: " + e.getMessage()).build();
				}
			}
		}
	}
}
