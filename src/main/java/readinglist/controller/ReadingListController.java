package readinglist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import readinglist.config.AmazonProperties;
import readinglist.model.Book;
import readinglist.model.Reader;
import readinglist.repository.ReadingListRepository;

import java.util.List;

@Controller
@RequestMapping("/")
public class ReadingListController {

    private final ReadingListRepository readingListRepository;
    private final AmazonProperties amazonProperties;

    @Autowired
    public ReadingListController(ReadingListRepository readingListRepository, AmazonProperties amazonProperties) {
        this.readingListRepository = readingListRepository;
        this.amazonProperties = amazonProperties;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String readersBooks(@AuthenticationPrincipal Reader reader, Model model) {
        List<Book> readingList = readingListRepository.findByReader(reader);
        if (readingList != null) {
            model.addAttribute("books", readingList);
            model.addAttribute("reader", reader);
            model.addAttribute("amazonID", amazonProperties.getAssociateId());
        }
        return "readingList";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addToReadingList(@AuthenticationPrincipal Reader reader, Book book) {
        book.setReader(reader);
        readingListRepository.save(book);
        return "redirect:/";
    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.POST)
    public String removeFromReadingList(@PathVariable Long id) {
        System.out.println(id);
        readingListRepository.deleteById(id);
        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fail")
    public void fail() {
        throw new RuntimeException();
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(value = HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
    public String error() {
        return "error";
    }


}
