# Dave Evers wants to sort files by content

- @date 2022/10/31
- @author kazurayam

This project was developed to propose a solution that was raised by Dave Evers in the Katalon Community forum at

- https://forum.katalon.com/t/how-do-i-decode-a-base64-encoded-url-from-a-test-email-using-katalon/79979/19


# How to try the solution

You need to download a jar from

- https://github.com/kazurayam/SortingFilesByDateTimeInContent/releases/tag/0.1.0

Download "SortingFilesByDateTimeInContent-0.1.0.jar" file, store it into the `Drivers` folder of your local Katalon project.

You want to make a test case code, like [this](./Scripts/TC/Script1667133570111.groovy):

```
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
```

The `com.kazurayam.study20221030.PathComparableByContentEmailDate` class implements everything needed to solve the Dave's problem. If you want to study more, please visit the following GitHub project and read the source.

https://github.com/kazurayam/SortingFilesByDateTimeInContent

Dave's problem is a pure Java/Groovy programming problem. It has nothing to do with Katalon Studio. So the SortingFilesByDateTimeContent project is not a Katalon project. It is a plain Java8 + Gradle + JUnit5 project. I used my favorite IDE (Intelli-J IDEA) to develop this project.
