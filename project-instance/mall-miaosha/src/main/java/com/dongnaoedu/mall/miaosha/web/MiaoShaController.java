/**
 * 
 */
package com.dongnaoedu.mall.miaosha.web;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongnaoedu.mall.miaosha.service.MiaoshaService;

/**
 * 秒杀入口
 * 
 * @see 专注在职IT人员能力提升，咨询顾问QQ: 2729 772 006
 */
@RestController
public class MiaoShaController {
	@Autowired
	MiaoshaService miaoshaService;

	@Value("${mall.miaosha.failed_redirect_url: '#'}")
	private String failedRedirectUrl; // 秒杀失败后跳转该页面

	@Value("${mall.miaosha.success_redirect_url: '#'}")
	private String successdRedirectUrl; // 秒杀成功后跳转该页面


//	http://localhost:8012/order/miaosha?itemId=屠龙宝刀&userId=admin
	@GetMapping("/order/miaosha")
		public ResponseEntity<String> miaosha(String itemId, String userId) throws Exception {
			String redirectUrl = "#";
			String result = "";
			try {
				// 返回等待 或者 秒杀结果
				result = miaoshaService.miaosha(itemId, userId);
				redirectUrl = successdRedirectUrl;
			} catch (Exception e) {
				redirectUrl = failedRedirectUrl;
				result = e.getMessage();
			}
		return ResponseEntity.status(HttpStatus.FOUND)
				.location(new URI(redirectUrl + "?itemId=" + itemId + "&result=" + result)).build();
	}
}
