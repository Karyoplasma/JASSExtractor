/*
 * JassExtractor
 * Copyright (C) [2025] [Karyoplasma]
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JASSExtractor {
	private static final String NEWLINE = System.getProperty("line.separator");
	private IniHandler settings;

	public JASSExtractor() {
		settings = new IniHandler();
	}

	private boolean isInList(List<String> list, String target) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(target)) {
				return true;
			}
		}
		return false;
	}

	private void deduplicate(List<String> functions) {
		String line = "";
		Pattern function = Pattern.compile("function\\s+(\\S+)\\s+takes");
		StringBuilder builder = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(
				new FileReader(settings.getWorkingFolder() + settings.getWorkingFile()))) {
			while ((line = reader.readLine()) != null) {
				Matcher matcher = function.matcher(line);

				if (matcher.find()) {
					String functionName = matcher.group(1);
					if (!this.isInList(functions, functionName)) {
						builder.append(line).append(NEWLINE);
						continue;
					}
					Path functionFilepath = Paths.get(settings.getFunctionFolder() + functionName + ".j");
					if (!Files.exists(functionFilepath)) {
						builder.append(line).append(NEWLINE);
						System.out.println("No such file (will be skipped): " + functionFilepath.getFileName());
						continue;
					}
					String content = Files.readString(functionFilepath);
					while ((line = reader.readLine()) != null) {
						if (line.contains("endfunction")) {
							builder.append(content).append(NEWLINE);
							System.out.println("Merged: " + functionFilepath.getFileName());
							break;
						}
					}
				} else {
					builder.append(line).append(NEWLINE);
				}
			}
			this.writeFileToDisk(settings.getOutputFolder() + settings.getWorkingFile(), builder.toString());
		} catch (

		FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void extractMultiple(List<String> functions) {
		String line = "";
		Pattern function = Pattern.compile("function\\s+(\\S+)\\s+takes");

		try (BufferedReader reader = new BufferedReader(
				new FileReader(settings.getWorkingFolder() + settings.getWorkingFile()))) {
			while ((line = reader.readLine()) != null) {
				Matcher matcher = function.matcher(line);
				if (matcher.find()) {
					String functionName = matcher.group(1);
					if (!this.isInList(functions, functionName)) {
						continue;
					}
					StringBuilder builder = new StringBuilder();
					builder.append(line).append(NEWLINE);
					while ((line = reader.readLine()) != null) {
						if (line.contains("endfunction")) {
							builder.append(line);
							this.writeFileToDisk(settings.getFunctionFolder() + functionName + ".j",
									builder.toString());
							System.out.println("Extracted function: " + functionName);
							break;
						}
						builder.append(line).append(NEWLINE);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void replaceFunction(String filename) {
		Path functionFilepath = Paths.get(settings.getFunctionFolder() + filename + ".j");
		if (!Files.exists(functionFilepath)) {
			System.out.println("Function does not exist: " + filename + ".j");
			return;
		}
		String line = "";
		Pattern function = Pattern.compile("function\\s+(\\S+)\\s+takes");
		StringBuilder builder = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(
				new FileReader(settings.getWorkingFolder() + settings.getWorkingFile()))) {
			while ((line = reader.readLine()) != null) {
				Matcher matcher = function.matcher(line);

				if (matcher.find()) {
					String functionName = matcher.group(1);
					if (functionName.equals(filename)) {
						String content = Files.readString(functionFilepath);
						while ((line = reader.readLine()) != null) {
							if (line.contains("endfunction")) {
								builder.append(content).append(NEWLINE);
								System.out.println("Merged: " + functionFilepath.getFileName());
								line = reader.readLine();
								break;
							}
						}
					}
				}
				builder.append(line).append(NEWLINE);
			}
			this.writeFileToDisk(settings.getOutputFolder() + settings.getWorkingFile(), builder.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void extractFunction(String name) {
		String line = "";
		Pattern function = Pattern.compile("function\\s+(\\S+)\\s+takes");

		try (BufferedReader reader = new BufferedReader(
				new FileReader(settings.getWorkingFolder() + settings.getWorkingFile()))) {
			while ((line = reader.readLine()) != null) {
				Matcher matcher = function.matcher(line);
				if (matcher.find()) {
					String functionName = matcher.group(1);
					if (functionName.equals(name)) {
						StringBuilder builder = new StringBuilder();
						builder.append(line).append(NEWLINE);
						while ((line = reader.readLine()) != null) {
							if (line.contains("endfunction")) {
								builder.append(line);
								this.writeFileToDisk(settings.getFunctionFolder() + functionName + ".j",
										builder.toString());
								System.out.println("Extracted function: " + functionName);
								return;
							}
							builder.append(line).append(NEWLINE);
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void dumpFunctions() {
		String line = "";
		Pattern function = Pattern.compile("function\\s+(\\S+)\\s+takes");
		try (BufferedReader reader = new BufferedReader(
				new FileReader(settings.getWorkingFolder() + settings.getWorkingFile()))) {
			while ((line = reader.readLine()) != null) {
				Matcher matcher = function.matcher(line);
				if (matcher.find()) {
					StringBuilder builder = new StringBuilder();
					String functionName = matcher.group(1);
					System.out.println("Current function: " + functionName);
					boolean stillInFunction = true;
					builder.append(line).append(NEWLINE);
					while (stillInFunction) {
						if ((line = reader.readLine()).contains("endfunction")) {
							stillInFunction = false;
						}
						builder.append(line).append(NEWLINE);
					}
					this.writeFileToDisk(settings.getFunctionFolder() + "dump/" + functionName + ".j",
							builder.toString());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeFileToDisk(String filename, String content) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args == null || args.length < 1) {
			System.out.println("Please pass valid arguments!");
			System.exit(0);
		}
		JASSExtractor extractor = new JASSExtractor();
		List<String> functions = new ArrayList<String>();
		int mode = -1;

		if (args[0].equals("-dump")) {
			mode = 0;
		} else if (args[0].equals("-merge")) {
			mode = 2;
		} else {
			mode = 1;
		}

		switch (mode) {
		case 0:
			extractor.dumpFunctions();
			System.exit(0);
		case 1:
			for (int i = 0; i < args.length; i++) {
				functions.add(args[i]);
			}
			if (functions.size() == 1) {
				extractor.extractFunction(functions.get(0));
			} else {
				extractor.extractMultiple(functions);
			}
			System.exit(0);
		case 2:
			for (int i = 1; i < args.length; i++) {
				functions.add(args[i]);
			}
			if (functions.isEmpty()) {
				System.out.println("Please pass the functions to merge.");
				System.exit(2);
			}
			if (functions.size() == 1) {
				extractor.replaceFunction(functions.get(0));
			} else {
				extractor.deduplicate(functions);
			}
			System.exit(0);
		default:
			System.exit(2);
		}
	}
}
