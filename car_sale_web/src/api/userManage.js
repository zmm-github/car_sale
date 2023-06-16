import request from '@/utils/request'

export default {
  // 获取所有用户列表
  getUserList(searchModel) {
    return request({
      url: '/user/getUserList',
      method: 'get',
      params: {
        nickName: searchModel.nickName,
        status: searchModel.status,
        currentPage: searchModel.currentPage,
        pageSize: searchModel.pageSize
      }
    });
  },

  // 新增用户
  addUser(user) {
    return request({
      url: '/user/addUser',
      method: 'post',
      data: user
    });
  },

  // 根据id修改用户
  updateUserById(user) {
    return request({
      url: '/user/updateUserById',
      method: 'put',
      data: user
    });
  },

  saveUser(user) {
    if(user.id == null || user.id == undefined) {
      return this.addUser(user);
    } else {
      return this.updateUserById(user);
    }
  },

  // 根据指定Id删除用户
  deleteUserById(id) {
    return request({
      url: `/user/deleteUserById/${id}`,
      method: 'delete'
    });
  },

  // 根据多个id批量删除用户
  deleteUserByIds(ids) {
    return request({
      url: '/user/deleteUserByIds',
      method: 'delete',
      data: ids
    });
  },

  // 根据id查询指定用户
  getUserById(id) {
    return request({
      url: `/user/getUserById/${id}`,
      method: 'get'
    });
  }
}
