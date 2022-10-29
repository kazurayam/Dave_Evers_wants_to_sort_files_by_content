package my

import java.nio.file.Path
import java.time.LocalDateTime

/**
 * A wrapper for Path, that enables comparison by the "Date" property contained in each files
 * For example,
 * "Date: 26 Oct 2022 14:43:15 -0700"
 * If the file does not contain a "Date" property, it will be regarded to have
 * midnight, January 1, 1970 of your local time zone.
 *
 */
class ComparablePathByContentDateTime extends ComparablePath
		implements Comparable<ComparablePathByContentDateTime> {
			
	protected LocalDateTime contentDate = LocalDateTime.of(1970, 1, 1, 0, 0, 0)
	
	public ComparablePathByContentDateTime(Path p) {
		super(p)
		this.contentDate = resolveTimestamp(p)
	}
	
	@Override
	protected LocalDateTime resolveTimestamp(Path p) {
			
	}
}