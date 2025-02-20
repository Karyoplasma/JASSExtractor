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
import java.util.HashMap;

public class IniHandler {
	private HashMap<String, String> settings;

	public IniHandler() {
		settings = new HashMap<String, String>();
		this.readSettings();
	}

	public String getWorkingFile() {
		return settings.getOrDefault("workingFile", "war3map.j");
	}

	public String getWorkingFolder() {
		return settings.getOrDefault("workingFolder", "workspace/");
	}

	public String getFunctionFolder() {
		return settings.getOrDefault("functionFolder", "workspace/extracted/");
	}

	public String getOutputFolder() {
		return settings.getOrDefault("outputFolder", "workspace/merged/");
	}

	private void readSettings() {
		Path iniFile = Paths.get("JASSExtractor.ini");
		if (!Files.exists(iniFile)) {
			this.createNewDefaultIniFile(iniFile);
			settings.put("workingFile", "war3map.j");
			settings.put("workingFolder", "workspace/");
			settings.put("functionFolder", "workspace/extracted/");
			settings.put("outputFolder", "workspace/merged");
			return;
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(iniFile.toFile()))) {
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] lineSplit = line.split("=");
				if (lineSplit.length == 1) {
					settings.put(lineSplit[0], "");
				} else {
					settings.put(lineSplit[0], lineSplit[1]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createNewDefaultIniFile(Path iniFile) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(iniFile.toFile()))) {
			System.out.println("Creating new default ini file...\n");
			writer.write("workingFile=war3map.j");
			writer.newLine();
			writer.write("workingFolder=workspace/");
			writer.newLine();
			writer.write("functionFolder=workspace/extracted/");
			writer.newLine();
			writer.write("outputFolder=workspace/merged/");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
