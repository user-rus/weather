import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static Document getPage() throws IOException {
        String url = "https://www.pogoda.msk.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");//Создаём регулярку, что нам не обходимо

    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate); // для проверки соответствия объектов определенным условиям
        if (matcher.find()) {// если есть соответствие групирует в строку
            return matcher.group();
        }
        throw new Exception("Date extract date from is string!");
    }

    private static int printPartValues(Elements values, int index) {
        int iterationCount = 4;
        if (index == 0) {
            Element valueLn_1 = values.get(3);
            boolean isMorning_1 = values.get(3).text().contains("Утро");
            boolean isMorning_2 = values.get(2).text().contains("Утро");

            if (isMorning_1) {
                iterationCount = 3;
            }
            if (isMorning_2) {
                iterationCount = 2;
            }
        }
        for (int i = 0; i < iterationCount; i++) {
            Element valueLine = values.get(index++);
            for (Element td : valueLine.select("td")) {
                System.out.print(td.text() + "     ");
            }
            System.out.println();
        }
        return iterationCount;
    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        int index = 0;

        for (Element name : names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + "   Явления                                      Темп         Дав  Влажность     Ветер");
            int iterationCount = printPartValues(values, index);
            index = index + iterationCount;
        }
    }
}
