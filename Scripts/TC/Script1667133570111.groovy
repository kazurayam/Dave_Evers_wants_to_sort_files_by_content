/**
 * @author kazurayam
 */

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.Comparator

import com.kms.katalon.core.configuration.RunConfiguration

import com.kazurayam.study20221030.IPathComparable
import com.kazurayam.study20221030.PathComparableByFileLastModified
import com.kazurayam.study20221030.PathComparableByContentEmailHeaderValue

Path dataDir = Paths.get(RunConfiguration.getProjectDir()).resolve("Include/data")

List<IPathComparable> files =
	Files.list(dataDir)
		.filter({ p -> p.getFileName().toString().endsWith(".eml") })
		// filter files with "To: xyz.com" header
		.map({ p -> new PathComparableByContentEmailHeaderValue(p, "To") })
		.filter({ p -> p.getValue().matches("xyz.com") })
		// to sort by the lastModified timestamp
		.map({ p -> new PathComparableByFileLastModified(p.get()) })
		// sort in descending order
		.sorted(Comparator.reverseOrder())
		// turn the Stream into a List
		.collect(Collectors.toList())
		
// print path of all eml files
files.eachWithIndex  { p, index ->
	println((index + 1) + "\t" + p.getValue() + "\t" + dataDir.relativize(p.get()))
}

