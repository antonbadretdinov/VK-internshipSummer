# VK-internship summer
## File explorer for Android devices
### Task:
Develop an application that displays a list of files on the device.
Information to be displayed:
- File name
- Size
- Date of creation
- File icon (for files with common extensions jpeg, png, txt, etc.)

The list of files should be sorted by name by default.

It should be possible to sort files in ascending and descending order by size, creation date, extension.

In the background, hash codes of files should be stored in the database on the device every time the application starts.

With the help of previously saved hash codes, it should be possible to display files that have been changed since the last launch of the application.

It is desirable to implement:
- Opening files
- Ability to share a file
- Quick work with the list without unnecessary actions

## Preview
### List of folders:
<img src="https://github.com/antonbadretdinov/VK-internshipSummer/blob/master/Screenshot_1.png" width="200">

---

### List of files:
<img src="https://github.com/antonbadretdinov/VK-internshipSummer/blob/master/Screenshot_2.png" width="200">

Special icons have been added for such popular file types:
- .jpg, .jpeg
- .png
- .txt
- .doc, .docx
- .mp4
- .gif
- .pdf

For other data types, the default icon is used.

Opening of such file types is supported:
```Kotlin
".doc" -> intent.setDataAndType(uri, "application/msword")
".docx" -> intent.setDataAndType(uri, "application/msword")
".pdf" -> intent.setDataAndType(uri, "application/pdf")
".ppt" -> intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
".pptx" -> intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
".xls" -> intent.setDataAndType(uri, "application/vnd.ms-excel")
".xlsx" -> intent.setDataAndType(uri, "application/vnd.ms-excel")
".rtf" -> intent.setDataAndType(uri, "application/rtf")
".wav" -> intent.setDataAndType(uri, "audio/x-wav")
".mp3" -> intent.setDataAndType(uri, "audio/x-wav")
".gif" -> intent.setDataAndType(uri, "image/gif")
".jpg" -> intent.setDataAndType(uri, "image/jpg")
".jpeg" -> intent.setDataAndType(uri, "image/jpeg")
".png" -> intent.setDataAndType(uri, "image/png")
".txt" -> intent.setDataAndType(uri, "text/plain")
".3gp" -> intent.setDataAndType(uri, "video/*")
".mpg" -> intent.setDataAndType(uri, "video/*")
".mpeg" -> intent.setDataAndType(uri, "video/*")
".mpe" -> intent.setDataAndType(uri, "video/*")
".mp4" -> intent.setDataAndType(uri, "video/*")
".avi" -> intent.setDataAndType(uri, "video/*")
else -> intent.setDataAndType(uri, "*/*")
```

---

### File and folder sorting options:
<img src="https://github.com/antonbadretdinov/VK-internshipSummer/blob/master/Screenshot_3.png" width="200">

By default, the list is sorted by name:
```Kotlin
binding.recyclerFiles.adapter = FilesAdapter(filesAndFolders?.sortedWith(naturalOrder()),this)
```

The following sorts are also implemented:
- by date:
```Kotlin
val sortedByDate = filesAndFolders?.sortedByDescending { it.lastModified() }?.toList()
binding.recyclerFiles.adapter = FilesAdapter(sortedByDate, this)
```
- by type:
```Kotlin
val sortedByType = filesAndFolders?.sortedBy { it.extension }?.toList()
binding.recyclerFiles.adapter = FilesAdapter(sortedByType, this)
```
- ascending:
```Kotlin
val sortByAsc = filesAndFolders?.sortedByDescending { it.length() }?.asReversed()
binding.recyclerFiles.adapter = FilesAdapter(sortByAsc, this)
```
- descending:
```Kotlin
val sortByDesc = filesAndFolders?.sortedByDescending { it.length() }
binding.recyclerFiles.adapter = FilesAdapter(sortByDesc, this)
```

---

### Saving files to a database
I use Room as a database. When the application is launched, files are read and saved inside the onCreate function using the saveFilesToDatabase() function, which is located in FileSave.kt. 
This function writes a file to FileModel, generates a hash code for the file. 
Then loads the FileModel instance into the database.

---

### Generating a hash code for a file
HashFunctions.kt is used to generate hash codes for files. 
The SHA-256 algorithm was used. 
For files whose size is 0 kb, the generateHashCodeForEmptyFile() function is used. 
For other files, generateHashCodeForFile() is used.
