package com.study.mike.mybatis.session;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.study.mike.mybatis.config.Configuration;
import com.study.mike.mybatis.config.MappedStatement;
import com.study.mike.mybatis.config.RealSqlAndParamValues;

public class MapperProxy implements InvocationHandler {

	private Class<?> mapper;

	private Configuration configuration;

	public MapperProxy(Class<?> mapper, Configuration configuration) {
		super();
		this.mapper = mapper;
		this.configuration = configuration;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO 这里需要完成哪些事？
		// 1、获得方法对应的SQL语句
		String id = this.mapper.getName() + "." + method.getName();
		MappedStatement ms = this.configuration.getMappedStatement(id);
		// 2、解析SQL参数与方法参数的对应关系，得到真正的SQL与语句参数值
		RealSqlAndParamValues rsp = ms.getRealSqlAndParamValues(args);
		// 3、获得数据库连接
		Connection conn = this.configuration.getDataSource().getConnection();
		// 4、执行语句
		PreparedStatement pst = conn.prepareStatement(rsp.getSql());
		if (rsp.getParamValues() != null) {
			int i = 1;
			for (Object p : rsp.getParamValues()) {
				if (p instanceof Byte) {
					pst.setByte(i++, (Byte) p);
				} else if (p instanceof Integer) {
					pst.setInt(i++, (int) p);
				} else if (p instanceof String) {
					pst.setString(i++, (String) p);
				}
				// pst.setxxx(i++, p); //
			}
		}

		// 5、执行语句并处理结果
		switch (ms.getSqlCommandType()) {
		case INSERT:
		case UPDATE:
		case DELETE:
			int rows = pst.executeUpdate();
			return handleUpdateReturn(rows, ms, method);
		case SELECT:
			ResultSet rs = pst.executeQuery();
			return handleResultSetReturn(rs, ms, args);
		}

		return null;
	}

	private Object handleResultSetReturn(ResultSet rs, MappedStatement ms, Object[] args) throws Throwable {
		return ms.getResultHandler().handle(rs, args);
	}

	private Object handleUpdateReturn(int rows, MappedStatement ms, Method method) {

		Class<?> returnType = method.getReturnType();
		if (returnType == Void.class) {
			try {
				return Void.class.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		} else if (returnType == int.class || returnType == Integer.class) {
			return rows;
		} else if (returnType == long.class || returnType == Long.class) {
			return (long) rows;
		}

		throw new IllegalArgumentException("update类方法的返回值只能是：void/int/Integer/long/Long");
	}

}
