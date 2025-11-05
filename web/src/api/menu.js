import request from '@/utils/request'

export function getMenuTree() {
  return request({
    url: '/menu/tree',
    method: 'get'
  })
}

export function getAllMenuTree() {
  return request({
    url: '/menu/tree/all',
    method: 'get'
  })
}

export function getMenuList() {
  return request({
    url: '/menu/list',
    method: 'get'
  })
}

export function getMenuById(id) {
  return request({
    url: `/menu/${id}`,
    method: 'get'
  })
}

export function saveMenu(data) {
  return request({
    url: '/menu',
    method: 'post',
    data
  })
}

export function updateMenu(data) {
  return request({
    url: '/menu',
    method: 'put',
    data
  })
}

export function deleteMenu(id) {
  return request({
    url: `/menu/${id}`,
    method: 'delete'
  })
}

export function assignMenuToRole(data) {
  return request({
    url: '/menu/assign',
    method: 'post',
    data
  })
}

export function getMenuIdsByRole(role) {
  return request({
    url: `/menu/role/${role}`,
    method: 'get'
  })
}
