import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


/**
 * 네이버 크롤러 메인 클래스
 *
 * @author steel
 * @since 2021.02.24
 */
public class NaverCrawlerMain {

    public static void main(String[] args) {
        XSSFWorkbook workbook = null;
        XSSFSheet sheet = null;
        XSSFRow row = null;
        XSSFCell cell = null;


        String id = "GUaWvVMcEKIocayHZo3l";
        String secret = "209scpyuN0";
        String[] diseaseCode_14 = {"copd", "기관지염", "만성기관지", "만성폐쇄성", "패색성폐질환", "폐기종", "폐쇄성질환", "폐쇄성폐질환"};

        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("블로그");

        try {
            NaverCrawler crawler = new NaverCrawler();
            String url = null;
            for (int i = 0; i < diseaseCode_14.length; i++) {
                url = URLEncoder.encode(diseaseCode_14[i], "UTF-8");
                String response = crawler.search(id, secret, url);
                String[] fields = {"title", "link", "description"};
                Map<String, Object> result = crawler.getResult(response, fields);
                if (result.size() > 0) System.out.println("total -> " + result.get("total"));
                List<Map<String, Object>> items = (List<Map<String, Object>>) result.get("result");
                for (Map<String, Object> item : items) {
                    System.out.println("====================================================");
                    for (String field : fields){
                        System.out.println(field + "->" + item.get(field));
                    }
                }



            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}