package my;

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime

@RunWith(JUnit4.class)
public class ComparablePathByContentDateTimeTest {

	private static Path dataDir
	private List<Path> paths

	@BeforeClass
	public static void beforeClass() {
		Path cwd = Paths.get(System.getProperty("user.dir"))
		dataDir = cwd.resolve("Include/data")
	}

	@Before
	public void setup() {
		paths = new ArrayList<>();
		for (Path p : Files.list(dataDir)) {
			paths.add(p)
		}
	}

	@Test
	public void test_parseContentForHeaders() {
		Map<String, String> headers = ComparablePathByContentDateTime.parseContentForHeaders(paths.get(0))
		headers.forEach({ k,v ->
			println k + ": " + v
		})
		assertTrue(headers.keySet().contains("Date"))
	}

	@Test
	public void test_DATETIMEFORMATTER() {
		LocalDateTime ldt = ComparablePathByContentDateTime.DATETIMEFORMATTER.parse("26 Oct 2022 14:43:15 -0700")
		println ldt
	}
	
	@Ignore
	@Test
	public void test_getContentDate() {
		Map<String, String> headers = ComparablePathByContentDateTime.parseContentForHeaders(paths.get(0));
		LocalDateTime timestamp = ComparablePathByContentDateTime.getContentDate(headers)
		println timestamp.format(ComparablePathByContentDateTime.dtf)
		assertNotNull(timestamp)
	}

	@Test
	public void compareTo() {
	}
}