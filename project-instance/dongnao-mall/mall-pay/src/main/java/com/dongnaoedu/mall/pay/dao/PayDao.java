package com.dongnaoedu.mall.pay.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dongnaoedu.mall.pay.bean.Pay;

/**
 * 支付操作dao
 * 
 * @author allen
 */
public interface PayDao extends JpaRepository<Pay, String>, JpaSpecificationExecutor<Pay> {

	List<Pay> getByStateIs(Integer state);

	List<Pay> getByStateIsNotAndStateIsNot(Integer state1, Integer state2);

}
