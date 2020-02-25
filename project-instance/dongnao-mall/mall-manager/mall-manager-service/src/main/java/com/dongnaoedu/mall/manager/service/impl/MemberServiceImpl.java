package com.dongnaoedu.mall.manager.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.dongnaoedu.mall.common.exception.MallException;
import com.dongnaoedu.mall.common.pojo.DataTablesResult;
import com.dongnaoedu.mall.manager.dto.DtoUtil;
import com.dongnaoedu.mall.manager.dto.MemberDto;
import com.dongnaoedu.mall.manager.mapper.TbMemberMapper;
import com.dongnaoedu.mall.manager.pojo.TbMember;
import com.dongnaoedu.mall.manager.pojo.TbMemberExample;
import com.dongnaoedu.mall.manager.service.MemberService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @author allen
 */
@Service("memberService")
public class MemberServiceImpl implements MemberService {
	private final static Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

	@Autowired
	private TbMemberMapper tbMemberMapper;

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TbMember getMemberById(long memberId) {

		TbMember tbMember;
		try {
			tbMember = tbMemberMapper.selectByPrimaryKey(memberId);
		} catch (Exception e) {
			throw new MallException("ID获取会员信息失败");
		}
		tbMember.setPassword("");
		return tbMember;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public DataTablesResult getMemberList(int draw, int start, int length, String search, String minDate,
			String maxDate, String orderCol, String orderDir) {

		DataTablesResult result = new DataTablesResult();

		try {
			// 分页
			PageHelper.startPage(start / length + 1, length);
			List<TbMember> list = tbMemberMapper.selectByMemberInfo("%" + search + "%", minDate, maxDate, orderCol,
					orderDir);
			PageInfo<TbMember> pageInfo = new PageInfo<>(list);

			for (TbMember tbMember : list) {
				tbMember.setPassword("");
			}

			result.setRecordsFiltered((int) pageInfo.getTotal());
			result.setRecordsTotal(getMemberCount().getRecordsTotal());

			result.setDraw(draw);
			result.setData(list);
		} catch (Exception e) {
			throw new MallException("加载用户列表失败");
		}

		return result;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public DataTablesResult getRemoveMemberList(int draw, int start, int length, String search, String minDate,
			String maxDate, String orderCol, String orderDir) {

		DataTablesResult result = new DataTablesResult();

		try {
			// 分页执行查询返回结果
			PageHelper.startPage(start / length + 1, length);
			List<TbMember> list = tbMemberMapper.selectByRemoveMemberInfo("%" + search + "%", minDate, maxDate,
					orderCol, orderDir);
			PageInfo<TbMember> pageInfo = new PageInfo<>(list);

			for (TbMember tbMember : list) {
				tbMember.setPassword("");
			}

			result.setRecordsFiltered((int) pageInfo.getTotal());
			result.setRecordsTotal(getRemoveMemberCount().getRecordsTotal());

			result.setDraw(draw);
			result.setData(list);
		} catch (Exception e) {
			throw new MallException("加载删除用户列表失败");
		}

		return result;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TbMember getMemberByUsername(String username) {

		List<TbMember> list;
		TbMemberExample example = new TbMemberExample();
		TbMemberExample.Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		try {
			list = tbMemberMapper.selectByExample(example);
		} catch (Exception e) {
			throw new MallException("ID获取会员信息失败");
		}
		if (!list.isEmpty()) {
			list.get(0).setPassword("");
			return list.get(0);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TbMember getMemberByPhone(String phone) {

		List<TbMember> list;
		TbMemberExample example = new TbMemberExample();
		TbMemberExample.Criteria criteria = example.createCriteria();
		criteria.andPhoneEqualTo(phone);
		try {
			list = tbMemberMapper.selectByExample(example);
		} catch (Exception e) {
			throw new MallException("Phone获取会员信息失败");
		}
		if (!list.isEmpty()) {
			list.get(0).setPassword("");
			return list.get(0);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TbMember getMemberByEmail(String email) {

		List<TbMember> list;
		TbMemberExample example = new TbMemberExample();
		TbMemberExample.Criteria criteria = example.createCriteria();
		criteria.andEmailEqualTo(email);
		try {
			list = tbMemberMapper.selectByExample(example);
		} catch (Exception e) {
			throw new MallException("Email获取会员信息失败");
		}
		if (!list.isEmpty()) {
			list.get(0).setPassword("");
			return list.get(0);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public DataTablesResult getMemberCount() {

		DataTablesResult result = new DataTablesResult();
		TbMemberExample example = new TbMemberExample();
		TbMemberExample.Criteria criteria = example.createCriteria();
		criteria.andStateNotEqualTo(2);
		try {
			result.setRecordsTotal((int) tbMemberMapper.countByExample(example));
		} catch (Exception e) {
			throw new MallException("统计会员数失败");
		}

		return result;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public DataTablesResult getRemoveMemberCount() {

		DataTablesResult result = new DataTablesResult();
		TbMemberExample example = new TbMemberExample();
		TbMemberExample.Criteria criteria = example.createCriteria();
		criteria.andStateEqualTo(2);
		try {
			result.setRecordsTotal((int) tbMemberMapper.countByExample(example));
		} catch (Exception e) {
			throw new MallException("统计移除会员数失败");
		}

		return result;
	}

	@Override
	@Transactional
	public TbMember addMember(MemberDto memberDto) {

		TbMember tbMember = DtoUtil.MemberDto2Member(memberDto);

		if (getMemberByUsername(tbMember.getUsername()) != null) {
			throw new MallException("用户名已被注册");
		}
		if (getMemberByPhone(tbMember.getPhone()) != null) {
			throw new MallException("手机号已被注册");
		}
		if (getMemberByEmail(tbMember.getEmail()) != null) {
			throw new MallException("邮箱已被注册");
		}

		tbMember.setState(1);
		tbMember.setCreated(new Date());
		tbMember.setUpdated(new Date());
		String md5Pass = DigestUtils.md5DigestAsHex(tbMember.getPassword().getBytes());
		tbMember.setPassword(md5Pass);

		if (tbMemberMapper.insert(tbMember) != 1) {
			throw new MallException("添加用户失败");
		}
		return getMemberByPhone(tbMember.getPhone());
	}

	@Override
	@Transactional
	public TbMember updateMember(Long id, MemberDto memberDto) {

		TbMember tbMember = DtoUtil.MemberDto2Member(memberDto);
		tbMember.setId(id);
		tbMember.setUpdated(new Date());
		TbMember oldMember = getMemberById(id);
		tbMember.setState(oldMember.getState());
		tbMember.setCreated(oldMember.getCreated());
		if (tbMember.getPassword() == null || tbMember.getPassword() == "") {
			tbMember.setPassword(oldMember.getPassword());
		} else {
			String md5Pass = DigestUtils.md5DigestAsHex(tbMember.getPassword().getBytes());
			tbMember.setPassword(md5Pass);
		}

		if (tbMemberMapper.updateByPrimaryKey(tbMember) != 1) {
			throw new MallException("更新会员信息失败");
		}
		return getMemberById(id);
	}

	@Override
	public TbMember changePassMember(Long id, MemberDto memberDto) {

		TbMember tbMember = tbMemberMapper.selectByPrimaryKey(id);

		String md5Pass = DigestUtils.md5DigestAsHex(memberDto.getPassword().getBytes());
		tbMember.setPassword(md5Pass);
		tbMember.setUpdated(new Date());

		if (tbMemberMapper.updateByPrimaryKey(tbMember) != 1) {
			throw new MallException("修改会员密码失败");
		}
		return getMemberById(id);
	}

	@Override
	public TbMember alertMemberState(Long id, Integer state) {

		TbMember tbMember = tbMemberMapper.selectByPrimaryKey(id);
		tbMember.setState(state);
		tbMember.setUpdated(new Date());

		if (tbMemberMapper.updateByPrimaryKey(tbMember) != 1) {
			throw new MallException("修改会员状态失败");
		}
		return getMemberById(id);
	}

	@Override
	@Transactional
	public int deleteMember(Long id) {

		if (tbMemberMapper.deleteByPrimaryKey(id) != 1) {
			throw new MallException("删除会员失败");
		}
		return 0;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TbMember getMemberByEditEmail(Long id, String email) {

		TbMember tbMember = getMemberById(id);
		//TbMember newTbMember = null;
		if (tbMember.getEmail() == null || !tbMember.getEmail().equals(email)) {
			//newTbMember = getMemberByEmail(email);
			tbMember = getMemberByEmail(email);
		}
		//newTbMember.setPassword("");
		//return newTbMember;
		return tbMember;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TbMember getMemberByEditPhone(Long id, String phone) {

		TbMember tbMember = getMemberById(id);
		//TbMember newTbMember = null;
		if (tbMember.getPhone() == null || !tbMember.getPhone().equals(phone)) {
			//newTbMember = getMemberByPhone(phone);
			tbMember = getMemberByPhone(phone);
		}
		//newTbMember.setPassword("");
		//return newTbMember;
		return tbMember;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TbMember getMemberByEditUsername(Long id, String username) {

		TbMember tbMember = getMemberById(id);
		//TbMember newTbMember = null;
		if (tbMember.getUsername() == null || !tbMember.getUsername().equals(username)) {
			//newTbMember = getMemberByUsername(username);
			tbMember = getMemberByUsername(username);
		}
		//newTbMember.setPassword("");
		//return newTbMember;
		return tbMember;
	}
}
