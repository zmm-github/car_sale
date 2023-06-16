const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  menuList: state => state.user.menuList,
  loginUser: state => state.user.loginUser,

  visitedViews: state => state.tagsView.visitedViews,
  // visitedviews: state => state.tagsview.visitedviews,
  cachedViews: state => state.tagsView.cachedViews,
  // permission_routes: state => state.permission.routes
}
export default getters
