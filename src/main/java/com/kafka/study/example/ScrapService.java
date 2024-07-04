package com.kafka.study.example;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class ScrapService {

    public String getJsonData() {
        try {
            Document doc = Jsoup.connect("https://jsonplaceholder.typicode.com/posts").ignoreContentType(true).get();
            return doc.body().text();

        } catch (IOException e) {
            log.error("Failed to scrap data.");
            throw new RuntimeException(e);
        }
    }
}
