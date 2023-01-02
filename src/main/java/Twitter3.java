package Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class Twitter3 {
    public static String ConsumerKey = "fn6COOtYtGl9rDkVw6sAwG9Zd";
    public static String ConsumerSecret = "ZH9TBhlKikp9L6slybUjUrxP6RewQLfozU4tsaPKepCF7mdbFG";
    public static String AccessToken = "1597508900982398978-c66cQHr54TgdOs0oBYHMjDbQYhpBIN";
    public static String AccessTokenSecret = "tMOf3fOrsGIQZJC8v6tIkOQHT5mn5eYKj9nMc4h3XZefr";

    String[] diseaseCodes_1 = {"sars", "감기", "감염성비염", "급성기관염", "급성기관지염", "급성바이러스형비인두염", "급성부비동염", "급성비염"
            , "급성비인두염", "급성상기도염", "급성상인두염", "급성인두염", "급성중이염", "급성축농증", "급성코인두염", "급성편도선염"
            , "급성편도염", "급성후두염", "독감", "모세기관지염", "몸살", "몸살기", "몸으슬으슬하다", "바이러스성기관지염", "상기도감염", "신종인플루엔자", "신종플루"
            , "열감기", "인플루엔자", "인후두염", "인후염", "인후통", "중증급성호흡기증후군", "코막히다", "편도붓다", "편도선붓다", "편도선아프다", "편도아프다", "폐렴"
    };

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    Calendar time = Calendar.getInstance();
    String datetime = sdf.format(time.getTime());


    static Twitter getT() {

        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(ConsumerKey)
                .setOAuthConsumerSecret(ConsumerSecret)
                .setOAuthAccessToken(AccessToken)
                .setOAuthAccessTokenSecret(AccessTokenSecret);

        TwitterFactory fac = new TwitterFactory(cb.build());
        Twitter twitter = fac.getInstance();

        return twitter;
    }

    public static void main(String[] args) {

        String[] diseaseCodes_1 = {"sars", "감기", "감염성비염", "급성기관염", "급성기관지염", "급성바이러스형비인두염", "급성부비동염", "급성비염"
                , "급성비인두염", "급성상기도염", "급성상인두염", "급성인두염", "급성중이염", "급성축농증", "급성코인두염", "급성편도선염"
                , "급성편도염", "급성후두염", "독감", "모세기관지염", "몸살", "몸살기", "몸으슬으슬하다", "바이러스성기관지염", "상기도감염", "신종인플루엔자", "신종플루"
                , "열감기", "인플루엔자", "인후두염", "인후염", "인후통", "중증급성호흡기증후군", "코막히다", "편도붓다", "편도선붓다", "편도선아프다", "편도아프다", "폐렴"
        };

        Twitter tw = getT();
        BufferedWriter bw;

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatt = DateTimeFormatter.ofPattern("yyyyMMdd");
        now.format(formatt);
        try {
            bw = new BufferedWriter(new FileWriter("/Users/misonaru/Desktop/" + diseaseCodes_1[0]+ ".txt"));
            //바탕화면에 test.txt라는 파일에 결과를 저장하도록 했습니다.
            for (int i = 0; i < diseaseCodes_1.length; i++) {
                Query query = new Query(diseaseCodes_1[i]);
                QueryResult result = null;
                result = tw.search(query);
                for (Status status : result.getTweets()) {
                    String twitterUserName = status.getUser().getScreenName();
                    String twitterText = status.getText();
                    String twitterDate = String.valueOf(status.getCreatedAt());

                    //특수기호는 본인이 알아보기 편한것 쓰세요
                    bw.write("@" + status.getUser().getScreenName() + "\n"
                            + ":" + status.getText() + "\n"
                            + "=" + status.getSource() + "\n"
                            + "=" + status.getCreatedAt()
                            + "#" + "\r\n");
                    System.out.println("status.getUser()==>"+status.getUser());
                    System.out.println("status.getUser().getScreenName()==>"+status.getUser().getScreenName());
                    System.out.println("status.getText()==>"+status.getText());
                    System.out.println("status.getSource()==>"+status.getSource());
                    System.out.println("status.getCreatedAt()==>"+status.getCreatedAt());
                }
                bw.write("===============================================R====");
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}

