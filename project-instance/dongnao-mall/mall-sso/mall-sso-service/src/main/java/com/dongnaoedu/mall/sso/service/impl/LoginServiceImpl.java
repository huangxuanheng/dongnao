package com.dongnaoedu.mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.dongnaoedu.mall.common.jedis.JedisClient;
import com.dongnaoedu.mall.manager.dto.DtoUtil;
import com.dongnaoedu.mall.manager.dto.front.Member;
import com.dongnaoedu.mall.manager.mapper.TbMemberMapper;
import com.dongnaoedu.mall.manager.pojo.TbMember;
import com.dongnaoedu.mall.manager.pojo.TbMemberExample;
import com.dongnaoedu.mall.sso.service.LoginService;
import com.google.gson.Gson;

/**
 * @author allen
 */
@Service("loginService")
public class LoginServiceImpl implements LoginService {

	@Autowired
	private TbMemberMapper tbMemberMapper;
	@Autowired
	private JedisClient jedisClientPool;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	@Override
	public Member userLogin(String username, String password) {

		TbMemberExample example = new TbMemberExample();
		TbMemberExample.Criteria criteria = example.createCriteria();
		criteria.andStateEqualTo(1);
		criteria.andUsernameEqualTo(username);
		List<TbMember> list = tbMemberMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			Member member = new Member();
			member.setState(0);
			member.setMessage("用户名或密码错误");
			return member;
		}
		TbMember tbMember = list.get(0);
		// md5加密
		if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbMember.getPassword())) {
			Member member = new Member();
			member.setState(0);
			member.setMessage("用户名或密码错误");
			return member;
		}
		String token = UUID.randomUUID().toString();
		Member member = DtoUtil.TbMemer2Member(tbMember);
		member.setToken(token);
		member.setState(1);
		// 用户信息写入redis：key："SESSION:token" value："user"
		jedisClientPool.set("SESSION:" + token, new Gson().toJson(member));
		jedisClientPool.expire("SESSION:" + token, SESSION_EXPIRE);

		return member;
	}

	@Override
	public Member getUserByToken(String token) {

		String json = jedisClientPool.get("SESSION:" + token);
		if (json == null) {
			Member member = new Member();
			member.setState(0);
			member.setMessage("用户登录已过期");
			return member;
		}
		// 重置过期时间
		jedisClientPool.expire("SESSION:" + token, SESSION_EXPIRE);
		Member member = new Gson().fromJson(json, Member.class);
		return member;
	}

	@Override
	public int logout(String token) {

		jedisClientPool.del("SESSION:" + token);
		return 1;
	}

}
