package readinglist.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import readinglist.ReadingListApplication;
import readinglist.model.Book;
import readinglist.model.Reader;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(
        classes = ReadingListApplication.class
)
@WebAppConfiguration
public class ReadingListRepositoryTest {

    @Autowired
    private ReadingListRepository repo;

    @Test
    @Transactional
    public void saveBook() {
        assertEquals(0, repo.findAll().size());

        Reader reader = new Reader();
        reader.setUsername("yakov");
        reader.setPassword("yakov");
        reader.setFullname("Yakov Burtsev");

        Book book = new Book();
        book.setTitle("TITLE");
        book.setDescription("DESCRIPTION");
        book.setAuthor("AUTHOR");
        book.setIsbn("1234567890");
        book.setReader(reader);
        Book saved = repo.save(book);

        Book found = repo.findOne(saved.getId());
        assertEquals(saved.getId(), found.getId());
        assertEquals("TITLE", found.getTitle());
        assertEquals("DESCRIPTION", found.getDescription());
        assertEquals("AUTHOR", found.getAuthor());
        assertEquals("1234567890", found.getIsbn());
        assertEquals(reader, found.getReader());
    }

}
