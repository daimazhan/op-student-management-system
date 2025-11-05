import request from '@/utils/request'

export function getRoleList() {
  return request({
    url: '/role/list',
    method: 'get'
  })
}

export function getRoleById(id) {
  return request({
    url: `/role/${id}`,
    method: 'get'
  })
}

export function saveRole(data) {
  return request({
    url: '/role',
    method: 'post',
    data
  })
}

export function updateRole(data) {
  return request({
    url: '/role',
    method: 'put',
    data
  })
}

export function deleteRole(id) {
  return request({
    url: `/role/${id}`,
    method: 'delete'
  })
}

export function assignMenuToRole(data) {
  return request({
    url: '/role/assign-menu',
    method: 'post',
    data
  })
}

export function getMenuIdsByRole(roleId) {
  return request({
    url: `/role/${roleId}/menus`,
    method: 'get'
  })
}
