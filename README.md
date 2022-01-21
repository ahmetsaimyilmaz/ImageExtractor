# ImageExtractor

Image Extractor is a console application written in java.

Image Extractor requires a target url and depth parameter. The application will crawl into target URL and traverse through 
all the links at given depth and extract the images stored in the web page. For instance if depth 2 is chosen, Image Extractor 
will populate links consisted in target url and extract images in those URLs. Give output of the image links as json file.

To run the application first run;

java -cp "jsoup-1.14.3.jar;gson-2.8.2.jar" ListLinksModified.java

Then after executable is created, run this code with adding necessary parameter.

java -cp "jsoup-1.14.3.jar;gson-2.8.2.jar" ListLinksModified.java https://www.smashingmagazine.com 2



