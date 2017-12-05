package com.egrasoft.ds3calc.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class ArmorCollection extends ArrayList<ArmorItem> implements Serializable {

    public static ArmorCollection collectionOf(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        ArmorCollection ac = new ArmorCollection();
        Elements pages = doc.selectFirst("div.category-gallery-paginator ul").children();
        int cur = 1;
        int num = Integer.parseInt(pages.get(pages.size()-2).selectFirst("a").html());
        do {
            Elements elems = doc.select("div.category-gallery-room1 div.category-gallery-item a");
            for (Element elem : elems) {
                ArmorItem ai = ArmorItem.valueOf(elem.attr("href"));
                ac.add(ai);
            }
            if (cur == num) break;
            cur++;
            doc = Jsoup.connect(url+"?page="+cur).get();
        } while (true);
        return ac;
    }

}
