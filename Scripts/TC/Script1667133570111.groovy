/**
 * @author kazurayam
 */

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

import com.kms.katalon.core.configuration.RunConfiguration

import com.kazurayam.study20221030.PathComparableByDateTime
import com.kazurayam.study20221030.PathComparableByContentEmailDate

Path dataDir = Paths.get(RunConfiguration.getProjectDir()).resolve("Include/data")

List<PathComparableByDateTime> emlFiles =
	Files.list(dataDir)
		.filter({ p -> p.toString().endsWith(".eml") })
		// wrap the Path object by a adapter class
		// to sort the Path objects by the Email Date in the file content
		.map({ p -> new PathComparableByContentEmailDate(p) })
		// in the descending order of the Date value
		.sorted(Comparator.reverseOrder())
		.collect(Collectors.toList())
		
// print path of all eml files
emlFiles.eachWithIndex  { p, index ->
	println((index + 1) + "\t" + p.getTimestampFormatted() + "\t" + dataDir.relativize(p.get()))
}

