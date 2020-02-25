package com.dongnaoedu.mall.pay.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dongnaoedu.mall.pay.bean.Pay;
import com.dongnaoedu.mall.pay.common.utils.StringUtils;
import com.dongnaoedu.mall.pay.dao.PayDao;
import com.dongnaoedu.mall.pay.service.PayService;

/**
 * 支付servier实现类
 * 
 * @author allen
 */
@Service
public class PayServiceImpl implements PayService {
	private static final Logger log = LoggerFactory.getLogger(PayServiceImpl.class);

	@Autowired
	private PayDao payDao;

	@Override
	public Page<Pay> getPayListByPage(Integer state, String key, Pageable pageable) {
		return payDao.findAll(new Specification<Pay>() {
			@Override
			public Predicate toPredicate(Root<Pay> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

				Path<String> nickNameField = root.get("nickName");
				Path<String> infoField = root.get("info");
				Path<String> payTypeField = root.get("payType");
				Path<Integer> stateField = root.get("state");

				List<Predicate> list = new ArrayList<Predicate>();

				// 模糊搜素
				if (StringUtils.isNotBlank(key)) {
					Predicate p1 = cb.like(nickNameField, '%' + key + '%');
					Predicate p3 = cb.like(infoField, '%' + key + '%');
					Predicate p4 = cb.like(payTypeField, '%' + key + '%');
					list.add(cb.or(p1, p3, p4));
				}

				// 状态
				if (state != null) {
					list.add(cb.equal(stateField, state));
				}

				Predicate[] arr = new Predicate[list.size()];
				cq.where(list.toArray(arr));
				return null;
			}
		}, pageable);
	}

	@Override
	public List<Pay> getPayList(Integer state) {

		List<Pay> list = payDao.getByStateIs(state);
		for (Pay pay : list) {
			// 屏蔽隐私数据
			pay.setId("");
			pay.setEmail("");
			pay.setTestEmail("");
			pay.setPayNum(null);
			pay.setMobile(null);
			pay.setCustom(null);
			pay.setDevice(null);
		}
		return list;
	}

	@Override
	public List<Pay> getNotPayList() {

		List<Pay> list = payDao.getByStateIsNotAndStateIsNot(0, 1);
		for (Pay pay : list) {
			// 屏蔽隐私数据
			pay.setId("");
			pay.setEmail("");
			pay.setTestEmail("");
			pay.setPayNum("");
			pay.setMobile(null);
			pay.setCustom(null);
			pay.setDevice(null);
			pay.setTime(StringUtils.getTimeStamp(pay.getCreateTime()));
		}
		return list;
	}

	@Override
	public Pay getPay(String id) {

		// Optional<Pay> pay = payDao.findById(id);
		Pay pay = payDao.getOne(id);
		log.info("根据id查询支付信息：" + pay.toString());
		pay.setTime(StringUtils.getTimeStamp(pay.getCreateTime()));
		return pay;
	}

	@Override
	public int addPay(Pay pay) {

		pay.setId(UUID.randomUUID().toString());
		pay.setCreateTime(new Date());
		pay.setState(0);
		payDao.save(pay);
		return 1;
	}

	@Override
	public int updatePay(Pay pay) {

		pay.setUpdateTime(new Date());
		payDao.saveAndFlush(pay);
		return 1;
	}

	@Override
	public int changePayState(String id, Integer state) {

		Pay pay = getPay(id);
		pay.setState(state);
		pay.setUpdateTime(new Date());
		payDao.saveAndFlush(pay);
		return 1;
	}

	@Override
	public int delPay(String id) {

		payDao.deleteById(id);
		return 1;
	}

}
