package readinglist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import readinglist.model.Book;
import readinglist.model.Reader;
import readinglist.repository.ReadingListRepository;

import java.util.List;

@Controller
@RequestMapping("/")
public class ReadingListController {

    @Autowired
    private ReadingListRepository readingListRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String readersBooks(UsernamePasswordAuthenticationToken authenticationToken, Model model) {
        Reader reader = (Reader) authenticationToken.getPrincipal();
        List<Book> readingList = readingListRepository.findByReader(reader);
        if (readingList != null) {
            model.addAttribute("books", readingList);
            model.addAttribute("reader", reader);
        }
        return "readingList";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addToReadingList(UsernamePasswordAuthenticationToken authenticationToken, Book book) {
        Reader reader = (Reader) authenticationToken.getPrincipal();
        book.setReader(reader);
        readingListRepository.save(book);
        return "redirect:/";
    }

}
