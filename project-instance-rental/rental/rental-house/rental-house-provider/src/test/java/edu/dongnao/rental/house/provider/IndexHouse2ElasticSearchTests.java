package edu.dongnao.rental.house.provider;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import edu.dongnao.rental.house.api.IHouseService;
import edu.dongnao.rental.house.api.ISearchService;
import edu.dongnao.rental.house.domain.HouseDTO;
import edu.dongnao.rental.house.form.DatatableSearch;
import edu.dongnao.rental.lang.ServiceMultiResult;

@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
@ActiveProfiles("dev")
public class IndexHouse2ElasticSearchTests {
	@Autowired
	private ISearchService searchService;
	
	@Autowired
	private IHouseService houseService;
	
	@Test
	public void indexDataTests() {
		DatatableSearch searchBody = new DatatableSearch();
		searchBody.setStatus(1);
		searchBody.setOrderBy("id");
		searchBody.setDirection("desc");
		searchBody.setStart(0);
		searchBody.setLength(200);
		ServiceMultiResult<HouseDTO> results = houseService.adminQuery(searchBody, 2L);
		if(results.getTotal() > 0) {
			results.getResult().forEach(h->{
				searchService.index(h.getId());
			});
		}
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
