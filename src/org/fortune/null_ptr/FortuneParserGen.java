package org.fortune.null_ptr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FortuneParserGen {

	private final List<String> myList;
	Integer randomIndex;

	FortuneParserGen() {

		// init list
		this.myList = new ArrayList<String>();

		// get text file;
		final InputStream in = FortuneMain.class.getResourceAsStream("/txt/dataBase.txt");
		final BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

		// seed list

		int c;
		final StringBuilder sb = new StringBuilder();

		try {
			while ((c = r.read()) != -1) {

				final char character = (char) c;

				if (character != '%') {
					;
					sb.append(character);
				} else {
					this.myList.add(sb.toString());
					sb.setLength(0);
				}

			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	public String getRandomFortune() {
		final int random_index = new Random().nextInt(this.myList.size());
		final String fortune = this.myList.get(random_index);
		return fortune;
	}

}
