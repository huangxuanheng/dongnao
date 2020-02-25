package edu.dongnao.rental.house.provider;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dongnao.rental.house.api.IAddressService;
import edu.dongnao.rental.house.domain.BaiduMapLocation;
import edu.dongnao.rental.lang.ServiceResult;

@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
@ActiveProfiles("dev")
public class SuportAddressTests {
	@Autowired
	IAddressService addressService;
	
	@Test
	public void test() throws JsonProcessingException {
		String city = "北京";
		String address = "北京市紫禁城乾清宫";
		ServiceResult<BaiduMapLocation> result = addressService.getBaiduMapLocation(city, address);
		System.out.println(new ObjectMapper().writeValueAsString(result.getResult()));
		
		String title = "两室两厅";
		long houseId = 32;
		addressService.lbsUpload(result.getResult(), title, address, houseId, 3000, 60);
		
		addressService.removeLbs(houseId);
	}

}
