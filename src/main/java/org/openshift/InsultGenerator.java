package org.openshift;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

public class InsultGenerator {

    public String generateInsult() {
	String words[][] = {{"Artless", "Bawdy", "Beslubbering"}, {"Base-court", "Bat-fowling", "Beef-witted"}, {"Apple-john", "Baggage", "Barnacle"}};
	String vowels = "AEIOU";
	String article = "an";
	String firstAdjective = words[0][new Random().nextInt(words[0].length)];
	String secondAdjective = words[1][new Random().nextInt(words[1].length)];
	String noun = words[2][new Random().nextInt(words[2].length)];
	if (vowels.indexOf(firstAdjective.charAt(0)) == -1) {
	    article = "a";
	}
	return String.format("Thou art %s %s %s %s!", article, firstAdjective, secondAdjective, noun);
    }


	public String OLDgenerateInsult() {
		String vowels = "AEIOU";
		String article = "an";
		String theInsult = "";
		try {String databaseURL = "jdbc:postgresql://";
			databaseURL += System.getenv("POSTGRESQL_SERVICE_HOST");
			databaseURL += "/" + System.getenv("POSTGRESQL_DATABASE");
			String username = System.getenv("POSTGRESQL_USER");
			String password = System.getenv("PGPASSWORD");
			Connection connection = DriverManager.getConnection(databaseURL, username, password);
			if (connection != null) {
				String SQL = "select a.string AS first, b.string AS second, c.string AS noun " +
					"from short_adjective a , long_adjective b, noun c ORDER BY random() limit 1";
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(SQL);
				while (rs.next()) {
					if (vowels.indexOf(rs.getString("first").charAt(0)) == -1) {
						article = "a";
					}
					theInsult = String.format("Thou art %s %s %s %s!", article,
							rs.getString("first"), rs.getString("second"), rs.getString("noun"));
				}
				rs.close();
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Database connection problem!";
		}
		return theInsult;
	}
}