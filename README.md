# JASSExtractor
A small command-line tool for extracting and merging functions to and from Warcraft III JASS files.

## Features
- Dump all JASS functions in your file to separate files.
- Extract specific functions into separate files.
- Merge edited functions back into the original file.

## Usage
1) Check the .ini file and set the paths there.

**"workingFile=":** Denotes the filename (and extension) of the file that will be used as the source for extracting and merging.

**"workingFolder=":** The workspace directory. Leave empty when using JASSExtractor from the same folder as the working file resides in.

**"functionFolder=":** The directory where functions should be extracted to. Not tied to the working folder, so you can separate those.

**"outputFolder=":** The output folder for the merged file. If it is equal to the working folder, your working file will get overwritten by the output file (not recommended).

2) Create a .bat file in the same folder as the .jar and pass arguments to the application. It should read something like this:

`java -jar JASSExtractor.jar -optionalmode nameOfFunction1 nameOfFunction2 nameOfFunction3`

Possible modes are:

**"-dump":** Dumps all functions found in the working file to a folder called "dump" in the function folder. Ignores all passed function names, just dumps everything in every case.

**"-merge":** Merges passed functions from the function folder into the working file. By default, it does not overwrite your working file, but creates a new copy with the changed functions in the output folder.

**no mode specification:** Extracts the functions passed to the .jar.

**All function names are case sensitive.**

I recommend putting a `pause` command at the end of the .bat, so the cmd stays open at the end.

3) Run the .bat.

In some protected folders (like Windows' C:\\-root directory or Desktop), it might require admin privileges, in normal folders, it doesn't.

## Release page:
https://github.com/Karyoplasma/JASSExtractor/releases/latest

## License

JassExtractor is licensed under the GNU General Public License (GPL). See the [LICENSE](LICENSE) file for details.