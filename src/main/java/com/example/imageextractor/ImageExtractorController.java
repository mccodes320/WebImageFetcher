package com.example.imageextractor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;

//http://localhost:8080/
@RestController
@RequestMapping("/api")
@CrossOrigin // 允許跨域請求
public class ImageExtractorController {

    private static final Logger logger = Logger.getLogger(ImageExtractorController.class.getName());

    // https://hentaiporns.net/r/tag/asou-asabu202/
    // https://www.wikipedia.org
    @PostMapping("/extract-images")
    public List<String> extractImages(@RequestBody UrlRequest request) {
        List<String> imageUrls = new ArrayList<>();

        logger.info("[extractImages] Start: " + request.getUrl());
        try {
            Document doc = Jsoup.connect(request.getUrl()).get();
            // 抓 src
            doc.select("img[src]").forEach(img -> {
                String src = img.absUrl("src");
                if (!src.isEmpty()) imageUrls.add(src);
            });

            // 抓 data-src（補上這段）
            doc.select("img[data-src]").forEach(img -> {
                String src = img.absUrl("data-src"); // Jsoup 可解析相對路徑
                if (!src.isEmpty()) {
                    imageUrls.add(src);
                    System.out.println("imageUrls: " + src);
                }
            });
        } catch (Exception e) {
            logger.info("[extractImages]" + e);
        }
        return imageUrls;
    }
}