
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
public class ListLinks {

	public static void main(String[] args) throws IOException {
		/*
		 * String url = "https://www.smashingmagazine.com"; print("Fetching %s...",
		 * url); Document doc = Jsoup.connect(url).get(); Elements media =
		 * doc.select("[src]"); Elements links = doc.select("a[href]");
		 */
		long startTime = System.currentTimeMillis();
		String url = "https://www.smashingmagazine.com";
		Set<String> uniqueImageURL = new HashSet<String>();
		uniqueImageURL = connectToUrl(url, 0);
		System.out.println();
		System.out.println("Printing out the images: ");
		System.out.println();
		// for (String str : uniqueImageURL) {
		// System.out.println(str);
		// }
		GsonBuilder GsonBuilder = new GsonBuilder();

		Gson gson = GsonBuilder.create();
		String JSONObject = gson.toJson(uniqueImageURL);

		System.out.println("\nConverted JSONObject —> " + JSONObject);

		Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = prettyGson.toJson(uniqueImageURL);

		System.out.println("\nPretty JSONObject ==> " + prettyJson);
		
		try (Writer writer = new FileWriter("OutputWithDepth2.json")) {
		     
			prettyGson.toJson(uniqueImageURL, writer);
		}
		long endTime = System.currentTimeMillis();

		System.out.println("That took " + (endTime - startTime) + " milliseconds");
		/*
		 * Document doc = Jsoup.connect(url).get(); Elements links =
		 * doc.select("a[href]"); Elements media = doc.select("[src]"); Elements imports
		 * = doc.select("link[href]"); System.out.println(); System.out.println();
		 * System.out.println(); print("\nLinks: (%d)", links.size()); for (Element link
		 * : links) { print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(),
		 * 35)); }
		 */
		

	}

	private static Set<String> connectToUrl(String inputURL, int depth) {
		
		Set<String> uniqueURL = new HashSet<String>();
		Set<String> uniqueImageURL = new HashSet<String>();
		Set<String> uniqueTargetURL = new HashSet<String>();
		Set<String> testCase = new HashSet<String>();

		ArrayList<HashSet<String>> depthList = new ArrayList<HashSet<String>>();
		ArrayList<HashSet<String>> ImageList = new ArrayList<HashSet<String>>();
		
		for (int i = 0; i < depth + 1; i++) {
			HashSet<String> sample = new HashSet<String>();
			depthList.add(sample);
		}
		for (int i = 0; i < depth + 1; i++) {
			HashSet<String> sample = new HashSet<String>();
			ImageList.add(sample);
		}
		depthList.get(0).add(inputURL);

		String url = inputURL;
		Document doc;
		print("Fetching %s with depth " + depth + "...", url);

		try {
			if (depth == 0)
				System.out.println();
			else {
				uniqueURL.add(inputURL);
				for (int i = 0; i < depth; i++) {

					for (String link : depthList.get(i)) {
						doc = Jsoup.connect(link).get();
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
								}

							}
						}
					}

				}
			}

			doc = Jsoup.connect(url).get();
			
			for (int j = 0; j < depth + 1; j++) {
				for (String link : depthList.get(j)) {
					doc = Jsoup.connect(link).get();
					Elements media = doc.select("[src]");
					for (Element src : media) {
						if (src.normalName().equals("img") && !src.attr("src").substring(0, 1).equals("/")
								&& !src.attr("src").substring(0, 1).equals(".")) {
							// System.out.println("src : " + src.attr("src"));
							uniqueImageURL.add(src.attr("src"));
							ImageList.get(j).add(src.attr("src"));
						} else {
							// System.out.println("src : " + src.attr("src"));
							// uniqueImageURL.add(src.attr("src"));
						}
					}
				}
			}

			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return uniqueImageURL;
		return ImageList.get(depth);
		// return testCase;

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