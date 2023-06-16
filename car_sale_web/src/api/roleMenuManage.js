import request from '@/utils/request'

export default {
  // 根据角色ID获取对应的菜单ID列表
  getMenuIdsByRoleId(roleId) {
    return request({
      url: `/roleMenu/getMenuIdsByRoleId/${roleId}`,
      method: 'get'
    });
  }
}
