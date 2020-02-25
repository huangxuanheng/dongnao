package edu.dongnao.rental.house.provider;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;

import edu.dongnao.rental.house.api.ISearchService;
import edu.dongnao.rental.house.domain.HouseHighlightDTO;
import edu.dongnao.rental.house.form.RentSearch;
import edu.dongnao.rental.lang.ServiceMultiResult;

@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
@ActiveProfiles("dev")
public class SearchTest {
	@Autowired
	private ISearchService searchService;
	
	@Autowired
	Gson gosn;
	
	@Test
	public void test() {
		RentSearch rentSearch = new RentSearch();
		rentSearch.setCityEnName("bj");
		rentSearch.setKeywords("地铁站");
		ServiceMultiResult<HouseHighlightDTO> result = searchService.highlightQuery(rentSearch);
		
		System.out.println(gosn.toJson(result));
	}
	
}
