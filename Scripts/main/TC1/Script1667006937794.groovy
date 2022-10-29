/**
 * @author kazurayam
 */

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.FileTime
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.stream.Collectors

import com.kms.katalon.core.configuration.RunConfiguration

Path dataDir = Paths.get(RunConfiguration.getProjectDir()).resolve("Include/data")

List<ComparablePath> emlFiles =
	Files.list(dataDir)
		.filter({ p -> p.toString().endsWith(".eml") })
		.map({ p -> new ComparablePath(p) })
		.collect(Collectors.toList())

// sort the list by descending order
emlFiles.sort(Comparator.reverseOrder());
		
// print path of all eml files
emlFiles.eachWithIndex  { p, index ->
	println((index + 1) + "\t" + p.toString() + "\t" + p.getTimestampFormatted())
}

// open the 1st eml file, print its contents
//String content = emlFiles.get(0).get().toFile().text
//println("\nContent: " + content)


/**
 * A wrapper for Path, that enables comparison by the lastModified propery of File
 */
class ComparablePath implements Comparable<ComparablePath> {
	
	protected Path p
	protected LocalDateTime timestamp
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
	
	public ComparablePath(Path p) {
		this.p = p
		this.timestamp = resolveTimestamp(p)
	}
	
	public Path get() {
		return this.p
	}
	
	public String getTimestampFormatted() {
		return timestamp.format(formatter);
	}
	
	protected LocalDateTime resolveTimestamp(Path p) {
		FileTime fileTime = Files.getLastModifiedTime(p)
		return LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.of("UTC"))
	}
	
	@Override
	public int compareTo(ComparablePath other) {
		if (timestamp < other.timestamp) {
			return -1
		} else if (timestamp == other.timestamp) {
			return 0
		} else {
			return 1
		}
	}
	
	@Override
	public String toString() {
		return p.toString()
	}
}

/**
 * A wrapper for Path, that enables comparison by the "Date" property contained in each files
 * For example,
 * "Date: 26 Oct 2022 14:43:15 -0700"
 * If the file does not contain a "Date" property, it will be regarded to have
 * midnight, January 1, 1970 of your local time zone.
 *
 */
class ComparablePathByContentDate extends ComparablePath
		implements Comparable<ComparablePathByContentDate> {
			
	protected LocalDateTime contentDate = LocalDateTime.of(1970, 1, 1, 0, 0, 0)
	
	public ComparablePathByContentDate(Path p) {
		super(p)
		this.contentDate = resolveTimestamp(p)
	}
	
	@Override
	protected LocalDateTime resolveTimestamp(Path p) {
			
	}
}