# Dave Evers wants to sort files by content

- @date 2022/10/31
- @author kazurayam

This project was developed to propose a solution that was raised by Dave Evers in the Katalon Community forum at

- https://forum.katalon.com/t/how-do-i-decode-a-base64-encoded-url-from-a-test-email-using-katalon/79979/19

## Problem to solve

One day in the "Katalon Community" forum I was asked a question. The original poster asked as follows:

- He has several hundreds of text files in a directory. Every file was named with a postfix ".eml", which stands for "Email message", like 79edddc6-ce98-4eff-b8ea-414e392bce1f.eml.

- The files have similar content like this:

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

- He wants to get a list of file names, which are sorted by descending order of the "Date" header value in the file content, for example: `Date: 26 Oct 2022 14:43:15 -0700`.

## How to try the solution in Katalon Studio

You can try the solution in your local Katalon Studio. I assume you have a Katalon project created already. The project should have [some `.eml` files](https://github.com/kazurayam/Dave_Evers_wants_to_sort_files_by_content/tree/master/Include/data) in the following folder:

- <projectDir>/Include/data

Plus, You need to download an external jar that I made from

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
