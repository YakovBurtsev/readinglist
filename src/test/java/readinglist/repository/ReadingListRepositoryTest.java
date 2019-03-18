package readinglist.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import readinglist.model.Book;
import readinglist.model.Reader;

import static org.junit.Assert.assertEquals;

@ActiveProfiles("default")
@RunWith(SpringRunner.class)
@SpringBootTest
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

        Book found = repo.getOne(saved.getId());
        assertEquals(saved.getId(), found.getId());
        assertEquals("TITLE", found.getTitle());
        assertEquals("DESCRIPTION", found.getDescription());
        assertEquals("AUTHOR", found.getAuthor());
        assertEquals("1234567890", found.getIsbn());
        assertEquals(reader, found.getReader());
    }

}
