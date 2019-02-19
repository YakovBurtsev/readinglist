package readinglist.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReadingListControllerITTest {

    @Value("${local.server.port}")
    private int port;

    @Test
    public void homePage_unauthenticatedUser() {
        RestTemplate restTemplate = new RestTemplate();
        String object = restTemplate.getForObject("http://localhost:{port}/", String.class, port);
        System.out.println(object);
    }

}