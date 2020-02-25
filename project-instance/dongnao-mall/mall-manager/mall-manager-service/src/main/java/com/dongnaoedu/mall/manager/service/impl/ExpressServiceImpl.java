package com.dongnaoedu.mall.manager.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dongnaoedu.mall.manager.mapper.TbExpressMapper;
import com.dongnaoedu.mall.manager.pojo.TbExpress;
import com.dongnaoedu.mall.manager.pojo.TbExpressExample;
import com.dongnaoedu.mall.manager.service.ExpressService;

/**
 * @author allen
 */
@Service("expressService")
public class ExpressServiceImpl implements ExpressService {

	@Autowired
	private TbExpressMapper tbExpressMapper;

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<TbExpress> getExpressList() {

		TbExpressExample example = new TbExpressExample();
		example.setOrderByClause("sort_order asc");
		return tbExpressMapper.selectByExample(example);
	}

	@Override
	@Transactional
	public int addExpress(TbExpress tbExpress) {

		tbExpress.setCreated(new Date());
		tbExpressMapper.insert(tbExpress);
		return 1;
	}

	@Override
	@Transactional
	public int updateExpress(TbExpress tbExpress) {

		tbExpress.setUpdated(new Date());
		tbExpressMapper.updateByPrimaryKey(tbExpress);
		return 1;
	}

	@Override
	@Transactional
	public int delExpress(int id) {

		tbExpressMapper.deleteByPrimaryKey(id);
		return 1;
	}
}
