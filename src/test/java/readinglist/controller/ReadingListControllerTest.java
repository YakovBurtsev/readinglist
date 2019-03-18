package readinglist.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import readinglist.model.Book;
import readinglist.model.Reader;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReadingListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void homePage_unauthenticatedUser() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location",
                        "http://localhost/login"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @WithUserDetails("yakov")
    @Test
    public void homePage_authenticatedUser() throws Exception {
        Reader expectedReader = new Reader();
        expectedReader.setUsername("yakov");
        expectedReader.setPassword("$2a$10$3mMiJUC9RYW8vPXZqdltIevnYhieC9iGUhE.Xd1ClOoSkyR/6xsBi");
        expectedReader.setFullname("Yakov Burtsev");

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("readingList"))
                .andExpect(model().attribute("reader", equalTo(expectedReader)))
                .andExpect(model().attribute("amazonID", "yaburtsev"))
                .andExpect(model().attribute("books", is(empty())));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
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
        expectedReader.setPassword("$2a$10$3mMiJUC9RYW8vPXZqdltIevnYhieC9iGUhE.Xd1ClOoSkyR/6xsBi");
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
                .andExpect(model().attribute("amazonID", "yaburtsev"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", hasSize(1)))
                .andExpect(model().attribute("books", contains(equalTo(expectedBook))));
    }
}