package de.m3y3r.vtcolors;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * converts to setvtrgb file format
 * 
 */
public class Convert implements Runnable {

	public static void main(String[] args) {
		new Convert().run();
	}

	@Override
	public void run() {
		char[][] colorMap = new char[3][16];

		Pattern p = Pattern.compile("0x(\\w{2})");
		int color = 0;
		try {
			List<String> lines = Files.readAllLines(Paths.get("tango.txt"));
			for(String line: lines) {
				line = line.trim();
				if(line.startsWith("#")) continue;
				if(line.isEmpty()) continue;

				Matcher m = p.matcher(line);
				int rgb = 0;
				while(m.find()) {
					String v = m.group(1);
					int cv = Integer.parseInt(v, 16);
					colorMap[rgb++][color] = (char) cv;
				}
				color++;
			}
			writeColorMap(colorMap);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {}
	}

	private void writeColorMap(char[][] colorMap) throws FileNotFoundException {
		try(PrintWriter w = new PrintWriter("vt-color-tango.csv")) {
			for(int rgb = 0, n = colorMap.length; rgb < n; rgb++) {
				for(int color = 0, m = colorMap[rgb].length; color < m; color++) {
					w.print((int)colorMap[rgb][color]);
					if(color + 1  < m) w.append(',');
				}
				w.println();
			}
		}
	}

}
