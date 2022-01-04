
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import java.util.*;

/**
 * Example program to list links from a URL.
 */
public class ListLinksModified {
	static int connectCounter = 0;
	static long totalConnectionTime=0;

	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		String url = "https://www.smashingmagazine.com";
		//String url = "https://www.milliyet.com.tr";
		Set<String> uniqueImageURL = new HashSet<String>();
		System.out.println("Please provide URL of the page and depth you want to crawl into");
		uniqueImageURL = connectToUrl(args[0], Integer.parseInt(args[1]));
		System.out.println();
		System.out.println("Printing out the images: ");
		System.out.println();
		GsonBuilder GsonBuilder = new GsonBuilder();

		Gson gson = GsonBuilder.create();
		String JSONObject = gson.toJson(uniqueImageURL);

		System.out.println("\nConverted JSONObject —> " + JSONObject);

		Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = prettyGson.toJson(uniqueImageURL);

		System.out.println("\nPretty JSONObject ==> " + prettyJson);

		try (Writer writer = new FileWriter("consoleApplication.json")) {

			prettyGson.toJson(uniqueImageURL, writer);
		}
		long endTime = System.currentTimeMillis();

		System.out.println("That took " + (endTime - startTime) + " milliseconds");
		System.out.println("Total connection is " + connectCounter);
		System.out.println("Total connection time based on Jsoup.connect is "+ totalConnectionTime+" milliseconds");

	}

	private static Set<String> connectToUrl(String inputURL, int depth) {
		print("Fetching %s with depth " + depth + "...", inputURL);
		System.out.println();
		long a, b, c, d, f, g, h, k, l, m, n;
		a = System.currentTimeMillis();
		Set<String> uniqueURL = new HashSet<String>();
		Set<String> uniqueImageURL = new HashSet<String>();
		Set<String> uniqueTargetURL = new HashSet<String>();
		Set<String> testCase = new HashSet<String>();

		ArrayList<HashSet<String>> depthList = new ArrayList<HashSet<String>>();
		HashSet<String> ImageList = new HashSet<String>();
		b = System.currentTimeMillis();

		for (int i = 0; i < depth + 1; i++) {
			HashSet<String> sample = new HashSet<String>();
			depthList.add(sample);
		}

		System.out.println("Creation of variables and lists took " + (b - a) + " milliseconds");
		depthList.get(0).add(inputURL);

		String url = inputURL;
		Document doc;

		try {
			if (depth == 0)
				System.out.println();
			else {
				uniqueURL.add(inputURL);
				c = System.currentTimeMillis();
				for (int i = 0; i < depth; i++) {

					for (String link : depthList.get(i)) {
						long c1 = System.currentTimeMillis();
						doc = Jsoup.connect(link).get();
						long c2 = System.currentTimeMillis();
						totalConnectionTime+=c2-c1;
						System.out.println("It took "+(c2-c1)+" milliseconds to connect "+link);
						connectCounter++;
						Elements links = doc.select("a[href]");
						for (Element InnerLink : links) {
							if (InnerLink.attr("abs:href").startsWith(url)
									&& InnerLink.attr("abs:href").substring(url.length()).contains("/")) {
								int gate = 1;
								for (String linkCheck : uniqueURL) {
									if (linkCheck.equals(InnerLink.attr("abs:href")))
										gate = -1;
								}

								if (gate == 1) {
									uniqueURL.add(InnerLink.attr("abs:href"));
									depthList.get(i + 1).add(InnerLink.attr("abs:href"));

									Elements media = doc.select("[src]");
									for (Element src : media) {
										if (src.normalName().equals("img")
												&& !src.attr("src").substring(0, 1).equals("/")
												&& !src.attr("src").substring(0, 1).equals(".")) {
											uniqueImageURL.add(src.attr("src"));
											ImageList.add(src.attr("src"));
										}
									}
								}

							}
						}
					}

				}
				d = System.currentTimeMillis();
				System.out.println("Populating links based on depth took " + (d - c) + " milliseconds");
			}

			f = System.currentTimeMillis();

			for (String link : depthList.get(depth)) {
				long c1 = System.currentTimeMillis();
				doc = Jsoup.connect(link).get();
				long c2 = System.currentTimeMillis();
				totalConnectionTime+=c2-c1;
				connectCounter++;
				System.out.println("It took "+(c2-c1)+" milliseconds to connect "+link);
				Elements media = doc.select("[src]");
				for (Element src : media) {
					if(!src.attr("src").equals(""))
					if (src.normalName().equals("img") && !src.attr("src").substring(0, 1).equals("/")
							&& !src.attr("src").substring(0, 1).equals(".")) {
						uniqueImageURL.add(src.attr("src"));
						ImageList.add(src.attr("src"));
					}
				}
			}
			g = System.currentTimeMillis();
			//System.out.println("Retrieving images took " + (g - f) + " milliseconds");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();
		return ImageList;

	}

	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}

}