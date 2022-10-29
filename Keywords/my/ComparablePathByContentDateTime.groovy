package my

import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * A wrapper for Path, that enables comparison by the "Date" property contained in each files.
 * For example,
 * 
 ```
 X-Sender: "Do Not Reply" donotreply@anywhere.com
 X-Receiver: xyz.com
 MIME-Version: 1.0
 From: "Do Not Reply" donotreply@anywhere.com
 To: xyz.com
 Date: 26 Oct 2022 14:43:15 -0700
 Subject: Hello world, what is my URL?
 Content-Type: text/html; charset=utf-8
 Content-Transfer-Encoding: base64
 aHR0cHM6Ly93d3cuZ29vZ2xlLmNvbQ==
 ```
 * 
 * If the file does not contain a "Date" property, it will be regarded to have
 * midnight, January 1, 1970 of your local time zone.
 *
 */
class ComparablePathByContentDateTime extends ComparablePath
		implements Comparable<ComparablePathByContentDateTime> {

	private static final Logger logger = LoggerFactory.getLogger(ComparablePathByContentDateTime.class);

	public static final DateTimeFormatter DATETIMEFORMATTER = DateTimeFormatter.ofPattern("dd LLL yyyy hh:mm:ss Z")

	protected LocalDateTime contentDateTime

	private Map<String, String> headers

	public ComparablePathByContentDateTime(Path p) {
		super(p)
		this.headers = parseContentForHeaders(p)
		this.contentDateTime = getContentDate(headers)
	}

	protected static Map<String, String> parseContentForHeaders(Path p) {
		Map<String, String> m = new LinkedHashMap<>()
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
					new FileInputStream(p.toFile()), "utf-8"))
			String line
			while ((line = br.readLine()) != null && line.length() != 0) {
				String key = line.substring(0, line.indexOf(":")).trim()
				String val = line.substring(line.indexOf(":") + 1).trim()
				m.put(key, val)
			}
		} catch (IOException e) {
			logger.warn(e)
		}
		return m
	}

	protected static LocalDateTime getContentDate(Map<String, String> headers) {
		if (headers.containsKey("Date")) {
			return DATETIMEFORMATTER.parse(headers.get("Date"))
		} else {
			return LocalDateTime.of(1970, 1, 1, 0, 0, 0)
		}
	}

	@Override
	public int compareTo(ComparablePathByContentDateTime other) {
		if (contentDateTime < other.contentDateTime) {
			return -1
		} else if (contentDateTime == other.contentDateTime) {
			return 0
		} else {
			return 1
		}
	}
}