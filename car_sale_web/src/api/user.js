import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export default {
  register(data) {
    return request({
      url: '/user/register',
      method: 'post',
      data
    })
  },

  // 检验用户名
  getUserByUsername(username) {
    return request({
      url: '/user/getUserByUsername',
      method: 'get',
      params: {
        username: username
      }
    });
  },
  
  // 修改用户密码
  updateUserPassById(user) {
    return request({
      url: '/user/updateUserPassById',
      method: 'put',
      data: user
    });
  },

  // 修改用户头像
  updateUserAvatarById(user) {
    return request({
      url: '/user/updateUserAvatarById',
      method: 'put',
      data: user
    });
  }
}

export function getInfo(token) {
  return request({
    url: '/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}
