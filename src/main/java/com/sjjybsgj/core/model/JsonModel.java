package com.sjjybsgj.core.model;

import java.io.Serializable;

/**
 * 名称：JsonModel<br>
 *
 * 描述：普通数据模型<br>
 *
 */
public class JsonModel  implements Serializable {
	
	
	/**
	 * 返回对象<br>
	 */
	private Object res;
	
	
	public JsonModel(Object res)  {
		super();
		this.res = res;
	}

	/**
	 * 获取结果对象<br>
	 *
	 * @return Object 结果对象
	 */
	public Object getRes() {
		return res;
	}

	/**
	 * 设置结果对象<br>
	 *
	 * @param res 结果对象
	 */
	public void setRes(Object res) {
		this.res = res;
	}

}
