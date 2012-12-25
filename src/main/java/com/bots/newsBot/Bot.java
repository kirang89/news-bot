package com.bots.newsBot;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * My news bot
 * 
 */
public class Bot {
	/**
	 * @param args
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ParserConfigurationException,
											SAXException, IOException {
		final DocumentBuilderFactory dbFactory = DocumentBuilderFactory
				.newInstance();
		final DocumentBuilder db = dbFactory.newDocumentBuilder();
		final Document doc = db
				.parse("http://weather.yahooapis.com/forecastrss?w=2295424&u=c");

		final StringBuilder message = new StringBuilder();
		String subject = "Daily news just for you :)";

		Element location = (Element) doc
				.getElementsByTagName("yweather:location").item(0);
		Element units = (Element) doc.getElementsByTagName("yweather:units")
				.item(0);
		Element wind = (Element) doc.getElementsByTagName("yweather:wind")
				.item(0);
		Element atmosphere = (Element) doc
				.getElementsByTagName("yweather:atmosphere").item(0);
		Element astronomy = (Element) doc
				.getElementsByTagName("yweather:astronomy").item(0);

		final String city = location.getAttribute("city");
		final String region = location.getAttribute("region");
		final String country = location.getAttribute("country");
		final String tempUnit = units.getAttribute("temperature");
		final String temperature = wind.getAttribute("chill");
		final String humidity = atmosphere.getAttribute("humidity");
		final String sunrise = astronomy.getAttribute("sunrise");
		final String sunset = astronomy.getAttribute("sunset");

		message.append("Good Morning Sire ! Hope that today be an awesome day for you.");
		message.append("\n\n");
		message.append("Here's your news for " + city + "," + region + ","
				+ country);
		message.append("\n\n");
		message.append("Weather: ");
		message.append("\n\n");
		message.append("	Temperature:  " + temperature + " " + tempUnit);
		message.append("\n");
		message.append("	Humidity:  " + humidity);
		message.append("\n");
		message.append("	Sunrise:  " + sunrise);
		message.append("\n");
		message.append("	Sunset:  " + sunset);
		message.append("\n");
		message.append("=================================================");
		message.append("\n\n");
		message.append("Gold Rates: ");
		message.append("\n\n");

		/** Gold Rate **/
		org.jsoup.nodes.Document document1 = Jsoup
				.connect("http://www.goldenchennai.com/").get();
		Elements elements1 = document1
				.select("span[title=Chennai 22 Carat Gold Rate]");
		Elements elements2 = document1
				.select("span[title=Chennai 24 Carat Gold Rate]");
		Elements elements3 = document1
				.select("span[title=Chennai 1 US Dollar Rate]");

		message.append("	22 Carat Gold: " + elements1.text());
		message.append("\n");
		message.append("	24 Carat Gold: " + elements2.text());
		message.append("\n");
		message.append("=================================================");
		message.append("\n\n");
		message.append("Exchange Rates: ");
		message.append("\n\n");
		message.append("	1 US $ = " + elements3.text());
		message.append("\n");
		message.append("=================================================");
		message.append("\n\n");
		message.append("What's hot:");
		message.append("\n\n");

		/** Wikipedia News **/
		org.jsoup.nodes.Document document = Jsoup
				.connect("http://en.wikipedia.org/").get();
		Elements newsHeadlines = document.select("#mp-itn ul li");

		for (org.jsoup.nodes.Element headline : newsHeadlines) {
			message.append("	* " + headline.text());
			message.append("\n");
		}

		message.append("\n\n\n");
		message.append("That's all for now Sire. I'll keep you updated. Have a great day :)");

		System.out.println("Completed Scraping");

		final String projectDir = System.getProperty("user.dir");
		Mailer mailer = new Mailer(projectDir
				+ "/src/main/resources/mailer.properties");
		mailer.sendMail(subject, message.toString());

	}
}
