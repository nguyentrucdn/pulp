import com.pulp.TextRank;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Created by thainguy on 8/7/2016.
 */
public class App {
    public static void main(String[] args) throws IOException, FeedException, BoilerpipeProcessingException {
        System.out.println("hello");

        URL url = new URL("http://vnexpress.net/rss/tin-moi-nhat.rss");
        //URL url = new URL("http://www.bongda.com.vn/feed.rss");
        HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
        // Reading the feed
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(httpcon));
        List entries = feed.getEntries();
        Iterator itEntries = entries.iterator();

        while (itEntries.hasNext()) {
            SyndEntry entry = (SyndEntry) itEntries.next();
            System.out.println("Title: " + entry.getTitle());
            System.out.println("Link: " + entry.getLink());
            System.out.println("Author: " + entry.getAuthor());
            System.out.println("Publish Date: " + entry.getPublishedDate());
            System.out.println("Description: " + entry.getDescription().getValue());
            //System.out.println(getUrlContent(entry.getLink()));
            System.out.println("Summary: " + getPageContent(entry.getLink()));
            System.out.println();

        }
    }

    private static String getUrlContent(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Element content = doc.select("div.fck_detail").first();
        if (content != null)
            return content.text();
        return "Empty text!!";
    }

    private static String getPageContent(String url) throws MalformedURLException, BoilerpipeProcessingException {
        ArticleExtractor ae = new ArticleExtractor();
        String content = ae.getText(new URL(url));
        TextRank rank = new TextRank();
        return rank.summary(content);
        //return content;
    }
}
