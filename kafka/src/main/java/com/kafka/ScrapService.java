package com.kafka;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class ScrapService {

    public Document getScrapData(String postId) {
        try {
            return Jsoup.connect("https://jsonplaceholder.typicode.com/posts/" + postId).ignoreContentType(true).get();

        } catch (IOException e) {
            log.error("Failed to scrap data.");
            throw new RuntimeException(e);
        }
    }
}
