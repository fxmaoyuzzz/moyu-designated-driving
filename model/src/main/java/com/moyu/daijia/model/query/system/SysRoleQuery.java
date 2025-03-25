//
//
package com.moyu.daijia.model.query.system;

import java.io.Serializable;

/**
 * <p>
 * 角色查询实体
 * </p>
 *
 * @author qy
 * @since 2019-11-08
 */
public class SysRoleQuery implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String roleName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}

