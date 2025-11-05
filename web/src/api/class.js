import request from '@/utils/request'

export function getClassList() {
  return request({
    url: '/class/list',
    method: 'get'
  })
}

export function getClassById(id) {
  return request({
    url: `/class/${id}`,
    method: 'get'
  })
}

export function saveClass(data) {
  return request({
    url: '/class',
    method: 'post',
    data
  })
}

export function updateClass(data) {
  return request({
    url: '/class',
    method: 'put',
    data
  })
}

export function deleteClass(id) {
  return request({
    url: `/class/${id}`,
    method: 'delete'
  })
}
