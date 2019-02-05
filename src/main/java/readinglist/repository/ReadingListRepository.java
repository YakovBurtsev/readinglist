package readinglist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import readinglist.model.Book;
import readinglist.model.Reader;

import java.util.List;

public interface ReadingListRepository extends JpaRepository<Book, Long> {
	
	List<Book> findByReader(Reader reader);

}
