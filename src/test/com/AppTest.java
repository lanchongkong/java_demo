import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author sunyukun
 * @since 2019/12/5 14:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {

    @Value("${test.url}")
    private String url;

    @Test
    public void test() {
        System.out.println("fuck");
        HtmlParser myurl = new HtmlParser("<body", "/body>");
        myurl.getStartUrl(url);
        myurl.getUrlContent();
        myurl.getContentArea();
        myurl.getStringNotInUrl("google");
        myurl.HtmlParser();
    }
}
