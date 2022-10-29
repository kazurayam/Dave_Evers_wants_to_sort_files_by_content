package my

import java.nio.file.Path
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.nio.file.attribute.FileTime

import my.ComparablePath

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

