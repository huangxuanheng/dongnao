package com.dongnaoedu.mall.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dongnaoedu.mall.common.constant.DictConstant;
import com.dongnaoedu.mall.manager.mapper.TbDictMapper;
import com.dongnaoedu.mall.manager.pojo.TbDict;
import com.dongnaoedu.mall.manager.pojo.TbDictExample;
import com.dongnaoedu.mall.manager.service.DictService;

/**
 * @author allen
 */
@Service("dictService")
public class DictServiceImpl implements DictService {

	@Autowired
	private TbDictMapper tbDictMapper;

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<TbDict> getDictList() {

		TbDictExample example = new TbDictExample();
		TbDictExample.Criteria criteria = example.createCriteria();
		// 条件查询
		criteria.andTypeEqualTo(DictConstant.DICT_EXT);
		List<TbDict> list = tbDictMapper.selectByExample(example);
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<TbDict> getStopList() {

		TbDictExample example = new TbDictExample();
		TbDictExample.Criteria criteria = example.createCriteria();
		// 条件查询
		criteria.andTypeEqualTo(DictConstant.DICT_STOP);
		List<TbDict> list = tbDictMapper.selectByExample(example);
		return list;
	}

	@Override
	@Transactional
	public int addDict(TbDict tbDict) {

		tbDictMapper.insert(tbDict);
		return 1;
	}

	@Override
	@Transactional
	public int updateDict(TbDict tbDict) {

		tbDictMapper.updateByPrimaryKey(tbDict);
		return 1;
	}

	@Override
	@Transactional
	public int delDict(int id) {

		tbDictMapper.deleteByPrimaryKey(id);
		return 1;
	}
}
