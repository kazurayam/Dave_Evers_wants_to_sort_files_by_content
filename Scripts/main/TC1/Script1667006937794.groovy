/**
 * @author kazurayam
 */

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

import com.kms.katalon.core.configuration.RunConfiguration

import my.ComparablePath

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
	println((index + 1) + "\t" + p.getTimestampFormatted() + "\t" + p.toString())
}

// open the 1st eml file, print its contents
//String content = emlFiles.get(0).get().toFile().text
//println("\nContent: " + content)
