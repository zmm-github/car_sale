import request from '@/utils/request'

export default {
  // 获取符合条件的所有角色列表
  getRoleList(searchModel) {
    return request({
      url: '/role/getRoleList',
      method: 'get',
      params: {
        roleName: searchModel.roleName,
        currentPage: searchModel.currentPage,
        pageSize: searchModel.pageSize
      }
    });
  },

  // 新增角色
  addRole(role) {
    return request({
      url: '/role/addRole',
      method: 'post',
      data: role
    });
  },

  // 根据id修改角色
  updateRoleById(role) {
    return request({
      url: '/role/updateRoleById',
      method: 'put',
      data: role
    });
  },

  saveRole(role) {
    if(role.roleId == null || role.roleId == undefined) {
      return this.addRole(role);
    } else {
      return this.updateRoleById(role);
    }
  },

  // 根据指定Id删除角色
  deleteRoleById(id) {
    return request({
      url: `/role/deleteRoleById/${id}`,
      method: 'delete'
    });
  },

  // 根据多个id批量删除角色
  deleteRoleByIds(ids) {
    return request({
      url: '/role/deleteRoleByIds',
      method: 'delete',
      data: ids
    });
  },

  // 根据id查询指定角色
  getRoleById(id) {
    return request({
      url: `/role/getRoleById/${id}`,
      method: 'get'
    });
  },

  // 获取所有角色列表
  getAllRoleList() {
    return request({
      url: "/role/getAllRoleList",
      method: 'get'
    });
  }
}
