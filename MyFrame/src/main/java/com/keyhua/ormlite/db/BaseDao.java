package com.keyhua.ormlite.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.List;

public class BaseDao<T> {
	public Context context;
	public Dao<T, Integer> tDao;
	public DatabaseHelper helper;
	public List<T> list;
	public T classtype;

	/**
	 * @param t
	 *            实体类
	 * @param context
	 *            上下文
	 */

	@SuppressWarnings("unchecked")
	public BaseDao(T t, Context context) {
		this.context = context;
		try {
			helper = DatabaseHelper.getHelper(context);
			tDao = helper.getDao(t.getClass());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 增加某一个
	 * 
	 * @param xiangQingItem
	 * @throws SQLException
	 */
	public void add(T t) {
		/*
		 * //事务操作
		 * TransactionManager.callInTransaction(helper.getConnectionSource(),
		 * new Callable<Void>() {
		 * 
		 * @Override public Void call() throws Exception { return null; } });
		 */
		try {
			tDao.create(t);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 获取list传入数据库的长度
	public int getLength() {
		return list.size();
	}

	/** 调用该方法增加所有 */
	public void add(List<T> ts) {
		this.list = ts;
		for (T t : ts) {
			add(t);
		}
	}

	public T get(int id) {
		try {
			return tDao.queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 更新
	 * 
	 * @param xiangQingItem
	 *            待更新的xiangQingItem
	 */
	public void update(T t) {
		try {
			tDao.createOrUpdate(t);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mxiangQingItemDAO.update(xiangQingItem);
	}

	/**
	 * 按照指定的id 与 xiangQingItemname 删除一项
	 * 
	 * @param id
	 * @param xiangQingItemname
	 * @return 删除成功返回true ，失败返回false
	 */
	public int deletebyId(Integer id) {
		try {
			// 删除指定的信息，类似delete xiangQingItem where 'id' = id ;
			DeleteBuilder<T, Integer> deleteBuilder = tDao.deleteBuilder();
			deleteBuilder.where().eq("id", id);
			return deleteBuilder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 按照指定的id 与 xiangQingItemname 删除一项
	 * 
	 * @param id
	 * @param xiangQingItemname
	 * @return 删除成功返回true ，失败返回false
	 */
	public int delete(String id, String name) {
		try {
			// 删除指定的信息，类似delete xiangQingItem where 'id' = id ;
			DeleteBuilder<T, Integer> deleteBuilder = tDao.deleteBuilder();
			deleteBuilder.where().eq(id, name);

			return deleteBuilder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 按照指定的tvl_id 删除一项游记
	 * 
	 * @param id
	 * @param xiangQingItemname
	 * @return 删除成功返回true ，失败返回false
	 */
	public int deletebytvlid(String tvl_id) {
		try {
			// 删除指定的信息，类似delete xiangQingItem where 'id' = id ;
			DeleteBuilder<T, Integer> deleteBuilder = tDao.deleteBuilder();
			deleteBuilder.where().eq("tvl_id", tvl_id);

			return deleteBuilder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 按照id查询xiangQingItem
	 * 
	 * @param id
	 * @return
	 */
	public T search(String phonenum) {
		try {
			// 查询的query 返回值是一个列表
			// 类似 select * from xiangQingItem where 'xiangQingItemname' =
			// xiangQingItemname;
			List<T> t = tDao.queryBuilder().where().eq("phonenum", phonenum).query();
			if (t.size() > 0)
				return t.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 按照id查询已选活动
	 * 
	 * @param id
	 * @return
	 */
	public T searchByTvl_id(String tvl_id) {
		try {
			// 查询的query 返回值是一个列表
			// 类似 select * from xiangQingItem where 'xiangQingItemname' =
			// xiangQingItemname;
			List<T> t = tDao.queryBuilder().where().eq("tvl_id", tvl_id).query();
			if (t.size() > 0)
				return t.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 按照id查询已选游记
	 * 
	 * @param id
	 * @return
	 */
	public T searchByActid(String act_id) {
		try {
			// 查询的query 返回值是一个列表
			// 类似 select * from xiangQingItem where 'xiangQingItemname' =
			// xiangQingItemname;
			List<T> t = tDao.queryBuilder().where().eq("act_id", act_id).query();
			if (t.size() > 0)
				return t.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 按照id查询当前队员
	 * 
	 * @param id
	 * @return
	 */
	public T searchByUid(String u_id) {
		try {
			// 查询的query 返回值是一个列表
			// 类似 select * from xiangQingItem where 'xiangQingItemname' =
			// xiangQingItemname;
			List<T> t = tDao.queryBuilder().where().eq("u_id", u_id).query();
			if (t.size() > 0)
				return t.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 更新队员报到状态 曾金叶
	 * 
	 * @param tps_state
	 *            需要更新的字段
	 * @param u_id
	 *            需要更新的指定开始时间
	 * @return
	 */
	public int updateUState(Integer is_report, String u_id) {
		try {
			UpdateBuilder<T, Integer> updateBuilder = tDao.updateBuilder();
			updateBuilder.updateColumnValue("is_report", is_report).where().eq("u_id", u_id);
			return updateBuilder.update();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 更新队员蓝牙设备 曾金叶
	 * 
	 * @param strDeviceSN
	 *            需要更新的字段
	 * @param u_id
	 *            需要更新的指定开始时间
	 * @return
	 */
	public int updateUSN(String strDeviceSN, String u_id) {
		try {
			UpdateBuilder<T, Integer> updateBuilder = tDao.updateBuilder();
			updateBuilder.updateColumnValue("strDeviceSN", strDeviceSN).where().eq("u_id", u_id);
			return updateBuilder.update();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 删除全部
	 */
	public void deleteAll() {
		try {
			tDao.delete(queryAll());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 查询所有的
	 */
	public List<T> queryAll() {
		List<T> t;
		try {
			t = tDao.queryForAll();
			return t;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 更新指定轨迹 曾金叶
	 * 
	 * @param latItem
	 *            需要更新的字段
	 * @param latItemId
	 *            需要更新的指定开始时间
	 * @return
	 */
	public int updateLatItem(String latItem, String start_time) {
		try {
			UpdateBuilder<T, Integer> updateBuilder = tDao.updateBuilder();
			updateBuilder.updateColumnValue("locationInfo", latItem).where().eq("start_time", start_time);
			return updateBuilder.update();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 更新指定结束时间 曾金叶
	 * 
	 * @param latItem
	 *            需要更新的字段
	 * @param latItemId
	 *            需要更新的指定开始时间
	 * @return
	 */
	public int updateEndtime(String latItem, String start_time) {
		try {
			UpdateBuilder<T, Integer> updateBuilder = tDao.updateBuilder();
			updateBuilder.updateColumnValue("end_time", latItem).where().eq("start_time", start_time);
			return updateBuilder.update();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 更新指定状态 张泰
	 * 
	 * @param latItem
	 *            需要更新的字段
	 * @param latItemId
	 *            需要更新的指定开始时间
	 * @return
	 */
	public int updateState(int latItem, String act_id) {
		try {
			UpdateBuilder<T, Integer> updateBuilder = tDao.updateBuilder();
			updateBuilder.updateColumnValue("act_state", latItem).where().eq("act_id", act_id);
			return updateBuilder.update();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 更新指定状态 张泰
	 * 
	 * @param latItem
	 *            需要更新的字段
	 * @param latItemId
	 *            需要更新的指定开始时间
	 * @return
	 */
	public int updatacollet(int latItem, String tvl_id) {
		try {
			UpdateBuilder<T, Integer> updateBuilder = tDao.updateBuilder();
			updateBuilder.updateColumnValue("is_collect", latItem).where().eq("tvl_id", tvl_id);
			return updateBuilder.update();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 更新指定状态 张泰
	 * 
	 * @param latItem
	 *            需要更新的字段
	 * @param latItemId
	 *            需要更新的指定开始时间
	 * @return
	 */
	public int updateStatehistory(String latItem, String act_id) {
		try {
			UpdateBuilder<T, Integer> updateBuilder = tDao.updateBuilder();
			updateBuilder.updateColumnValue("history", latItem).where().eq("act_id", act_id);
			return updateBuilder.update();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 更新指定轨迹名称 曾金叶
	 * 
	 * @param latItem
	 *            需要更新的字段
	 * @param latItemId
	 *            需要更新的指定开始时间
	 * @return
	 */
	public int updateGpsName(String latItem, String start_time) {
		try {
			UpdateBuilder<T, Integer> updateBuilder = tDao.updateBuilder();
			updateBuilder.updateColumnValue("name", latItem).where().eq("start_time", start_time);
			return updateBuilder.update();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 地图中 按照开始时间查询当前正在记录的轨迹
	 * 
	 * @param id
	 * @return
	 */
	public T searchBylatItemId(String start_time) {
		try {
			// 查询的query 返回值是一个列表
			// 类似 select * from xiangQingItem where 'xiangQingItemname' =
			// xiangQingItemname;
			List<T> t = tDao.queryBuilder().where().eq("start_time", start_time).query();
			if (t.size() > 0)
				return t.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 地图中 按照距离排序
	 * 
	 * @param id
	 * @return
	 */
	public List<T> searchOrderByDistance() {
		try {
			List<T> t = tDao.queryBuilder().orderBy("distance", false).query();
			if (t.size() > 0)
				return t;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 写游记中按照自增id查询当前游记
	 * 
	 * @param id
	 * @return
	 */
	public T searchByYoujiId(int id) {
		try {
			// 查询的query 返回值是一个列表
			// 类似 select * from xiangQingItem where 'xiangQingItemname' =
			// xiangQingItemname;
			List<T> t = tDao.queryBuilder().where().eq("id", id).query();
			if (t.size() > 0)
				return t.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
