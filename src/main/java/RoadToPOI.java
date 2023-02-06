import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;
import org.json.simple.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;


import java.io.*;
import java.net.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static Test.Twitter3.*;

public class RoadToPOI {
    public static void main(String[] args) throws Exception {
        XSSFWorkbook workbook = null;
        XSSFSheet sheet = null;
        XSSFRow row = null;
        XSSFCell cell = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar time = Calendar.getInstance();
        String datetime = sdf.format(time.getTime());

        String clientId = "GUaWvVMcEKIocayHZo3l"; //애플리케이션 클라이언트 아이디
        String clientSecret = "209scpyuN0"; //애플리케이션 클라이언트 시크릿

        /* 헤더 ----------------------------------------------------------------------------------------------------------------------------------------------------------- */
        String[] blogHeaders = {"SNS 구분", "SNS ID", "blog id 의 끝부분", "blog id 의 끝부분", "blog name", "사용자 이미지 URL", "title + description", "SNS URL", "생성일자", "생성일시", "사용여부", "구분코드"};
        String[] newsHeaders = {"SNS 구분", "SNS ID", "사용자 ID", "사용자명", "표시명", "이미지 URL", "title + description", "SNS URL", "생성일자", "생성일시", "사용여부", "구분코드"};
        String[] twitterHeaders = {"SNS 구분", "SNS ID", "사용자 ID", "사용자명", "표시명", "이미지 URL", "title + description", "SNS URL", "생성일자", "생성일시", "사용여부"};
        /* --------------------------------------------------------------------------------------------------------------------------------------------------------------- */

        /* 질병코드 -------------------------------------------------------------------------------------------------------------------------------------------------------- */
        String[] diseaseCodes_1 = {"sars", "감기", "감염성비염", "급성기관염", "급성기관지염", "급성바이러스형비인두염", "급성부비동염", "급성비염", "급성비인두염", "급성상기도염", "급성상인두염", "급성인두염", "급성중이염", "급성축농증", "급성코인두염", "급성편도선염", "급성편도염", "급성후두염", "독감", "모세기관지염", "몸살", "몸살기", "몸으슬으슬하다", "바이러스성기관지염", "상기도감염", "신종인플루엔자", "신종플루", "열감기", "인플루엔자", "인후두염", "인후염", "인후통", "중증급성호흡기증후군", "코막히다", "편도붓다", "편도선붓다", "편도선아프다", "편도아프다", "폐렴"};
        String[] diseaseCodes_2 = {"눈병", "각결막염", "각막궤양", "각막염", "결막부종", "결막염", "결막하출혈", "눈다래끼", "눈다래키", "눈다레끼", "다래끼", "다래키", "다레끼", "맥립종", "산립종", "아폴로눈병", "콩다래끼", "포도막염"};
        String[] diseaseCodes_3 = {"급성설사", "노로걸리다", "노로바이러스", "대장균감염", "로타바이러스", "바이러스성장감염", "배탈", "배탈나다", "복통심하다", "살모넬라감염", "살모넬라증", "살모넬로시스", "설사계속하다", "설사동반하다", "설사병", "설사병나다", "설사증", "세균성장감염", "시겔라균", "시겔라균감염", "시겔라증", "식중독", "아메바이질", "아메바종", "아메바증", "아베마증", "유행성바이러스설사", "장결장염", "장염", "장출혈성대장균감염증", "장티푸스", "파라티푸스감염"};
        String[] diseaseCodes_4 = {"감염성천식", "기관지성천식", "내인성천식", "복합성천식", "심장성천식", "아토피성천식", "알레르기성천식", "외인성천식", "직업성천식", "천명음", "천식", "혼합성천식"};
        String[] diseaseCodes_5 = {"피부염", "건선", "습진", "아토피", "아토피성피부염", "알레르기성접촉피부염", "알레르기성피부염", "알레르기접촉피부염", "알레르기피부염", "장미색비강진", "콜린성두드러기", "태열", "피부그림증", "피부묘기증", "한랭두드러기"};
        String[] diseaseCodes_6 = {"뇌혈관질환", "거미막출혈", "거미막하출혈", "경뇌막", "경동맥협착", "경동맥협착증", "경막하출혈", "뇌경막하출혈", "뇌경색", "뇌꽈리", "뇌내출혈", "뇌동맥", "뇌동맥경화", "뇌동맥경화증", "뇌동맥류", "뇌동정맥기형", "뇌동정맥루", "뇌색전증", "뇌실질내출혈", "뇌실질출혈", "뇌연화증", "뇌일혈", "뇌정맥", "뇌정맥류", "뇌졸중", "뇌중풍", "뇌출혈", "뇌허혈", "뇌혈관", "뇌혈류검사", "뇌혈류장애", "뇌혈전", "뇌혈전증", "모야모야병", "모야모야질병", "목동맥협착", "목동맥협착증", "중풍", "지주막출혈", "지주막하출혈"};
        String[] diseaseCodes_7 = {"영유아폐렴", "폐포염증", "포도상구균폐렴", "포도알균폐렴", "호산구성폐렴", "호흡기분비물전파되다", "호흡기세포융합바이러스", "흉곽함몰", "흉부통증", "흡인성폐렴", "흡인폐렴", "rs바이러스", "간질성폐렴", "간질폐렴", "감염성폐렴", "객담", "결핵성폐렴", "경기관지폐생검", "곰팡이전염폐렴", "과민성폐렴", "그람음성간균", "급성간질성폐렴", "급성폐렴", "기관지폐렴", "기관지확장증", "대엽성폐렴", "독감바이러스성폐렴", "레지오넬라증", "레지오넬라폐렴", "리케차성폐렴", "림프구간질성폐렴", "만성폐렴", "모세기관지염", "무기폐", "미생물감염성페렴", "미생물감염성폐렴", "바이러스성폐렴", "방사선폐렴", "병조성폐렴", "보행폐렴", "분비물전파되다", "비감염성폐렴", "빈호흡", "상기도증후군", "색가래", "세균성폐렴", "세기관지염", "소아폐렴", "소엽 폐렴", "소엽폐렴", "신종인플루엔자폐렴", "알레르기성폐렴", "약제유발성폐렴", "연쇄상구균폐렴", "염증성호흡기질환", "원발성이형폐렴", "이형폐렴", "중증폐렴", "지역사회획득폐렴", "진균성폐렴", "진균증에서의폐렴", "침강폐렴", "크라미디아폐렴", "클레브지엘라폐렴", "태변흡인증후군", "폐기종", "폐농양", "폐렴", "폐렴간균", "폐렴구균", "폐렴사슬알균폐렴", "폐렴혐기성세균", "폐염증", "폐침윤", "폐포성폐렴"};
        String[] diseaseCodes_8 = {"영유아수족구", "71수족구병", "바이러스71수족구병", "발수포성발진", "소수포", "소수포구내염", "손발입병", "손수포성발진", "수족구", "수족구병", "수포구내염", "수포성발진", "에코바이러스", "엔테로바이러스", "입안궤양", "입안물집", "장바이러스", "콕사키바이러스", "통증성피부병변", "호흡기분비물"};
        String[] diseaseCodes_9 = {"copd", "기관지염", "만성기관지", "만성폐쇄성", "패색성폐질환", "폐기종", "폐쇄성질환", "폐쇄성폐질환"};

        /* --------------------------------------------------------------------------------------------------------------------------------------------------------------- */

        Map<String, String> itemMap = new HashMap<>();
        Map<String, String> newsItemMap = new HashMap<>();

        List<Map<String, String>> itemList = new ArrayList<>();
        List<Map<String, String>> newsItemList = new ArrayList<>();

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatt = DateTimeFormatter.ofPattern("yyyyMMdd");
        now.format(formatt);

        /*
            질병 코드 설정
         */
        String currentCode = diseaseCodes_9[0];


        int row_num = -1;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("블로그");
        for (String s : diseaseCodes_9) {
            int i = 0;
            String text = null;
            try {
                text = URLEncoder.encode(s, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("검색어 인코딩 실패", e);
            }
            String texts = URLDecoder.decode(text, "UTF-8"); // 이렇게 해야 tesxts = 감기

            String apiURLBlog = "https://openapi.naver.com/v1/search/blog.json?query=" + text + "&sort=date&display=100";

            Map<String, String> requestHeaders = new HashMap<>();

            /***********************************************************************************************************
             -------------------------------------------- 블로그 정보 ------------------------------------------------------
             ***********************************************************************************************************/
            requestHeaders.put("X-Naver-Client-Id", clientId);
            requestHeaders.put("X-Naver-Client-Secret", clientSecret);
            String responseBodyBlog = get(apiURLBlog, requestHeaders);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBodyBlog);        // 블로그

            JSONArray blogItems = (JSONArray) jsonObject.get("items");

//            System.out.println(blogItems);

            // 블로그 값 엑셀 생성
            for (int j = 0; j < blogItems.size(); j++) {
                JSONObject itemsObject = (JSONObject) blogItems.get(j);

                String SnsId = itemsObject.get("link").toString().substring(itemsObject.get("link").toString().lastIndexOf("/") + 1);
                String blogEnd = itemsObject.get("bloggerlink").toString().substring(itemsObject.get("bloggerlink").toString().lastIndexOf("/") + 1);
                String bloggerName = itemsObject.get("bloggername").toString();
                String titleAndDescription = itemsObject.get("title").toString() + " " + itemsObject.get("description").toString();
                String link = itemsObject.get("link").toString();
                String postdate = itemsObject.get("postdate").toString();
                String postDateTime = itemsObject.get("postdate").toString() + "0000";

                if (postdate.equals(datetime)
                        && SnsId != null && !SnsId.equals("")
                        && blogEnd != null && !blogEnd.equals("")
                        && bloggerName != null && !bloggerName.equals("")
                        && titleAndDescription != null && !titleAndDescription.equals("")
                        && link != null && !link.equals("")
                        && postdate != null && !postdate.equals("")
                        && postDateTime != null && !postDateTime.equals("")) {
//                if (postdate.equals("20230204") && SnsId != null && blogEnd != null && bloggerName != null
//                        && titleAndDescription != null && link != null && postdate != null && postDateTime != null ) {
                    itemMap.put("SnsID", SnsId);
                    itemMap.put("blogEnd", blogEnd);
                    itemMap.put("bloggerName", bloggerName);
                    itemMap.put("titleAndDescription", titleAndDescription);
                    itemMap.put("link", link);
                    itemMap.put("postdate", postdate);
                    itemMap.put("postDateTime", postDateTime);
                } else continue;

                row_num++;
                row = sheet.createRow(row_num);

                for (int k = 0; k < blogHeaders.length; k++) {
                    cell = row.createCell(k);
                    if (row_num == 0) {
                        cell.setCellValue(new XSSFRichTextString(blogHeaders[k]));
                    } else {
                        if (row.getCell(k) != null || row.getCell(k).getCellType() != CellType.BLANK) {
                            if (k == 0) {
                                cell.setCellValue("B");
                            } else if (k == 1) {
                                cell.setCellValue(itemMap.get("SnsID"));
                            } else if (k == 2 || k == 3) {
                                cell.setCellValue(itemMap.get("blogEnd"));
                            } else if (k == 4) {
                                cell.setCellValue(itemMap.get("bloggerName"));
                            } else if (k == 5) {
                                cell.setCellValue("");
                            } else if (k == 6) {
                                cell.setCellValue(itemMap.get("titleAndDescription"));
                            } else if (k == 7) {
                                cell.setCellValue(itemMap.get("link"));
                            } else if (k == 8) {
                                cell.setCellValue(itemMap.get("postdate"));
                            } else if (k == 9) {
                                cell.setCellValue(itemMap.get("postDateTime"));
                            } else if (k == 10) {
                                cell.setCellValue("Y");
                            } else if (k == 11){
                                if (currentCode == "sars"){
                                    cell.setCellValue("01");
                                } else if (currentCode == "눈병"){
                                    cell.setCellValue("02");
                                } else if (currentCode == "급성설사"){
                                    cell.setCellValue("03");
                                } else if (currentCode == "감염성천식"){
                                    cell.setCellValue("04");
                                } else if (currentCode == "피부염"){
                                    cell.setCellValue("05");
                                } else if (currentCode == "뇌혈관질환"){
                                    cell.setCellValue("11");
                                } else if (currentCode == "영유아폐렴"){
                                    cell.setCellValue("12");
                                } else if (currentCode == "영유아수족구"){
                                    cell.setCellValue("13");
                                } else if (currentCode == "copd"){
                                    cell.setCellValue("14");
                                }
                            }
                        } else continue;
                    }
                }
            }
        }

        sheet = workbook.createSheet("뉴스");

        /***********************************************************************************************************
         ------------------------------------------------ 뉴스 ------------------------------------------------------
         ***********************************************************************************************************/
        row_num = -1;

        for (String s : diseaseCodes_9) {
            String text = null;
            try {
                text = URLEncoder.encode(s, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("검색어 인코딩 실패", e);
            }
            String texts = URLDecoder.decode(text, "UTF-8"); // 이렇게 해야 tesxts = 감기

            String apiURLNews = "https://openapi.naver.com/v1/search/news.json?query=" + text + "&sort=date&display=100";

            Map<String, String> requestHeadersNews = new HashMap<>();

            // 뉴스 정보 MAP
            requestHeadersNews.put("X-Naver-Client-Id", clientId);
            requestHeadersNews.put("X-Naver-Client-Secret", clientSecret);
            String responseBodyNews = get(apiURLNews, requestHeadersNews);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObjectNews = (JSONObject) jsonParser.parse(responseBodyNews);    // 뉴스

            JSONArray newsItmes = (JSONArray) jsonObjectNews.get("items");

            // 뉴스 엑셀 생성
            for (int j = 0; j < newsItmes.size(); j++) {
                JSONObject newsObject = (JSONObject) newsItmes.get(j);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                String cd = newsObject.get("pubDate").toString();

                LocalDate dateTime = LocalDate.parse(cd, formatter);
                LocalDateTime localDateTime = LocalDateTime.parse(cd, formatter);
                DateTimeFormatter myPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String localDateTimeToString = localDateTime.format(myPattern);

                String SnsId = newsObject.get("link").toString().substring(newsObject.get("link").toString().lastIndexOf("/") + 1);
                String titleAndDescription = newsObject.get("title").toString() + " " + newsObject.get("description").toString();
                String link = newsObject.get("link").toString();
                String postdate = localDateTimeToString.substring(0, 8);
                String postDateTime = localDateTimeToString;

                if (postdate.equals(datetime)
                        && SnsId != null && !SnsId.equals("")
                        && titleAndDescription != null && !titleAndDescription.equals("")
                        && link != null && !link.equals("")
                        && postdate != null && !postdate.equals("")
                        && postDateTime != null && !postDateTime.equals("")) {
//                if (postdate.equals("20230204") && SnsId != null && titleAndDescription != null && link != null
//                        && postdate != null && postDateTime != null ) {
                    newsItemMap.put("SnsID", SnsId);
                    newsItemMap.put("titleAndDescription", titleAndDescription);
                    newsItemMap.put("link", link);
                    newsItemMap.put("postdate", postdate);
                    newsItemMap.put("postDateTime", postDateTime);
                } else continue;

                row_num++;
                row = sheet.createRow(row_num);

                for (int k = 0; k < newsHeaders.length; k++) {
                    cell = row.createCell(k);
                    if (row_num == 0) {
                        cell.setCellValue(new XSSFRichTextString(newsHeaders[k]));
                    } else {
                        if (row.getCell(k) != null || row.getCell(k).getCellType() != CellType.BLANK) {
                            if (k == 0) {
                                cell.setCellValue("N");
                            } else if (k == 1) {
                                cell.setCellValue(newsItemMap.get("SnsID"));
                            } else if (k >= 2 && k <= 5) {
                                cell.setCellValue("");
                            } else if (k == 6) {
                                cell.setCellValue(newsItemMap.get("titleAndDescription"));
                            } else if (k == 7) {
                                cell.setCellValue(newsItemMap.get("link"));
                            } else if (k == 8) {
                                cell.setCellValue(newsItemMap.get("postdate"));
                            } else if (k == 9) {
                                cell.setCellValue(newsItemMap.get("postDateTime"));
                            } else if (k == 10) {
                                cell.setCellValue("Y");
                            } else if (k == 11){
                                if (currentCode == "sars"){
                                    cell.setCellValue("01");
                                } else if (currentCode == "눈병"){
                                    cell.setCellValue("02");
                                } else if (currentCode == "급성설사"){
                                    cell.setCellValue("03");
                                } else if (currentCode == "감염성천식"){
                                    cell.setCellValue("04");
                                } else if (currentCode == "피부염"){
                                    cell.setCellValue("05");
                                } else if (currentCode == "뇌혈관질환"){
                                    cell.setCellValue("11");
                                } else if (currentCode == "영유아폐렴"){
                                    cell.setCellValue("12");
                                } else if (currentCode == "영유아수족구"){
                                    cell.setCellValue("13");
                                } else if (currentCode == "copd"){
                                    cell.setCellValue("14");
                                }
                            }
                        } else continue;
                    }
                }
            }
        }

        try {
            File xlsFile = new File("/Users/misonaru/Desktop/" + diseaseCodes_9[0] + "_" + datetime + ".xlsx");
//            File xlsFile = new File("/Users/misonaru/Desktop/" + diseaseCodes_9[0] + "_" + "20231027" + ".xlsx");
            FileOutputStream fileOut = new FileOutputStream(xlsFile);
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            workbook.close();
        }

    }

    private static String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }

    static Twitter getT() {

        String ConsumerKey = "fn6COOtYtGl9rDkVw6sAwG9Zd";
        String ConsumerSecret = "ZH9TBhlKikp9L6slybUjUrxP6RewQLfozU4tsaPKepCF7mdbFG";
        String AccessToken = "1597508900982398978-c66cQHr54TgdOs0oBYHMjDbQYhpBIN";
        String AccessTokenSecret = "tMOf3fOrsGIQZJC8v6tIkOQHT5mn5eYKj9nMc4h3XZefr";

        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true).setOAuthConsumerKey(ConsumerKey).setOAuthConsumerSecret(ConsumerSecret).setOAuthAccessToken(AccessToken).setOAuthAccessTokenSecret(AccessTokenSecret);

        TwitterFactory fac = new TwitterFactory(cb.build());
        Twitter twitter = fac.getInstance();

        return twitter;
    }

}