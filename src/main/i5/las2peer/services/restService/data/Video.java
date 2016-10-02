package i5.las2peer.services.restService.data;

/**
 * This object is serialized and deserialized by Jackson.
 * 
 * Please note that each field that should be serialized must be accessible either directly or by a getter / setter.
 *
 */
public class Video {

	public String title;
	public String description;
	public String uri;

	public Video() {
		// always need a default contructor for JSON deserialization to work
	}

	public Video(String title, String description, String uri) {
		this.title = title;
		this.description = description;
		this.uri = uri;
	}

}
