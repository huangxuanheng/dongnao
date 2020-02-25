package es.mine.controller;

import es.mine.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {
    @GetMapping(value = "/{index}/_search")
    public String searchDocument(@PathVariable("index") String index,
                                 @RequestParam(value = "q", required = true) String queryStr){

        SearchService searcher = new SearchService();
        String result = searcher.query(index, queryStr);
        return result;
    }

}
