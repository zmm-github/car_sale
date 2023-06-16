import request from '@/utils/request'

export default {
  // 获取所有菜单列表列表
  getMenuList() {
    return request({
      url: '/menu/getMenuList',
      method: 'get'
    });
  }
}
