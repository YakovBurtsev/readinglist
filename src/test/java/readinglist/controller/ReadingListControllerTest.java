package readinglist.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import readinglist.ReadingListApplication;
import readinglist.model.Book;
import readinglist.model.Reader;
import readinglist.repository.ReadingListRepository;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(
        classes = ReadingListApplication.class
)
@WebAppConfiguration
public class ReadingListControllerTest {

    @Autowired
    private WebApplicationContext webContext;

    @Autowired
    private ReadingListRepository readingListRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webContext)
                .apply(springSecurity())
                .build();

        readingListRepository.deleteAll();
    }

    @Test
    public void homePage_unauthenticatedUser() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location",
                        "http://localhost/login"));
    }

    @WithUserDetails("yakov")
    @Test
    public void homePage_authenticatedUser() throws Exception {
        Reader expectedReader = new Reader();
        expectedReader.setUsername("yakov");
        expectedReader.setPassword("yakov");
        expectedReader.setFullname("Yakov Burtsev");

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("readingList"))
                .andExpect(model().attribute("reader", equalTo(expectedReader)))
                .andExpect(model().attribute("amazonID", "habuma-20"))
                .andExpect(model().attribute("books", is(empty())));
    }

    @WithUserDetails("yakov")
    @Test
    public void postBook() throws Exception {
        mockMvc.perform(post("/")
                .contentType(APPLICATION_FORM_URLENCODED)
                .param("title", "BOOK TITLE")
                .param("author", "BOOK AUTHOR")
                .param("isbn", "1234567890")
                .param("description", "DESCRIPTION")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/"));

        Reader expectedReader = new Reader();
        expectedReader.setUsername("yakov");
        expectedReader.setPassword("yakov");
        expectedReader.setFullname("Yakov Burtsev");

        Book expectedBook = new Book();
        expectedBook.setId(1L);
        expectedBook.setReader(expectedReader);
        expectedBook.setTitle("BOOK TITLE");
        expectedBook.setAuthor("BOOK AUTHOR");
        expectedBook.setIsbn("1234567890");
        expectedBook.setDescription("DESCRIPTION");

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("readingList"))
                .andExpect(model().attribute("amazonID", "habuma-20"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", hasSize(1)))
                .andExpect(model().attribute("books", contains(equalTo(expectedBook))));
    }
}