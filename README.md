# WebImageFetcher
輸入網址, 可顯示圖片內容


🧩 專案功能描述（未來生成可直接依此描述）
專案名稱： ImageScraper
功能目的：
使用者輸入一個網頁網址，後端會抓取該網址的 HTML 內容，從中找出所有圖片連結（不論是 <img src> 或 <img data-src>），並回傳給前端，前端再將圖片顯示出來。

📦 專案技術棧
後端：Java + Spring Boot

HTML 解析工具：Jsoup

前端：純 HTML + JavaScript（使用 fetch 發送請求）

執行方式：Spring Boot 單體應用，HTML 放在 src/main/resources/static/index.html

🔁 系統流程
使用者開啟 index.html，輸入網址並按下送出按鈕。

瀏覽器透過 fetch() 傳送 POST 請求至 /api/extract-images。

後端使用 Jsoup 連線至該網址並解析 HTML。

從 HTML 中取出所有 <img> 標籤的 src 與 data-src 屬性。

回傳所有圖片連結（絕對 URL）給前端。

前端解析回傳的圖片 URL 陣列，並動態新增 <img> 元素顯示圖片。

📁 專案結構範例
swift
Copy
Edit
ImageScraper/
├── src/
│   └── main/
│       ├── java/com/example/imagescraper/
│       │   └── ImageScraperApplication.java
│       │   └── ImageController.java
│       └── resources/
│           ├── application.properties
│           └── static/
│               └── index.html
├── build.gradle 或 pom.xml
🧱 index.html（前端）
放置位置：src/main/resources/static/index.html

html
Copy
Edit
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Image Scraper</title>
</head>
<body>
  <h1>輸入網址來擷取圖片</h1>
  <input type="text" id="urlInput" placeholder="輸入目標網址">
  <button onclick="fetchImages()">送出</button>
  <div id="imageContainer"></div>

  <script>
    function fetchImages() {
      const url = document.getElementById("urlInput").value;
      fetch("/api/extract-images", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ url: url })
      })
      .then(response => response.json())
      .then(data => {
        const container = document.getElementById("imageContainer");
        container.innerHTML = "";
        data.forEach(src => {
          const img = document.createElement("img");
          img.src = src;
          img.style.maxWidth = "300px";
          img.style.margin = "10px";
          container.appendChild(img);
        });
      });
    }
  </script>
</body>
</html>
🧩 Java Spring Boot 後端主程式
ImageScraperApplication.java

java
Copy
Edit
@SpringBootApplication
public class ImageScraperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImageScraperApplication.class, args);
    }
}
📡 後端 Controller
ImageController.java

java
Copy
Edit
@RestController
public class ImageController {

    @PostMapping("/api/extract-images")
    @CrossOrigin(origins = "*")
    public List<String> extractImages(@RequestBody Map<String, String> payload) throws IOException {
        String url = payload.get("url");
        Document doc = Jsoup.connect(url).get();

        Set<String> imageUrls = new LinkedHashSet<>();

        // 擷取 src
        doc.select("img[src]").forEach(img -> {
            String src = img.absUrl("src");
            if (!src.isEmpty()) imageUrls.add(src);
        });

        // 擷取 data-src
        doc.select("img[data-src]").forEach(img -> {
            String src = img.absUrl("data-src");
            if (!src.isEmpty()) imageUrls.add(src);
        });

        return new ArrayList<>(imageUrls);
    }
}
🧪 注意事項與錯誤處理建議
請確認前端用 fetch() 正確傳送 JSON，不要讓特殊字元直接出現在 URL 路徑。

Spring Boot 的嵌入式 Tomcat 預設會拒絕不合法字元，可透過 application.properties 放寬（不建議）：

properties
Copy
Edit
server.tomcat.relaxed-path-chars=|,{,},[,]
server.tomcat.relaxed-query-chars=|,{,},[,]
🔚 結尾
這是一個簡單實用的圖片擷取工具，適合用於學習 HTML 資料解析、跨域 API 呼叫與前後端協作。如果未來你要重新製作，只需要提供這份描述即可快速再生成一模一樣的功能。

是否需要我幫你轉成 README.md 版本？這樣可以直接放在 GitHub 上。
