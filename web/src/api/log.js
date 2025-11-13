import request from '@/utils/request'

export function getLogList(params) {
  return request({
    url: '/log/list',
    method: 'get',
    params
  })
}

export function getLogById(id) {
  return request({
    url: `/log/${id}`,
    method: 'get'
  })
}

export function saveLog(data) {
  return request({
    url: '/log',
    method: 'post',
    data
  })
}

export function getStatisticsByType() {
  return request({
    url: '/log/statistics/type',
    method: 'get'
  })
}

export function getStatisticsByModule() {
  return request({
    url: '/log/statistics/module',
    method: 'get'
  })
}

export function getStatisticsByUser() {
  return request({
    url: '/log/statistics/user',
    method: 'get'
  })
}

export function getStatisticsByDate() {
  return request({
    url: '/log/statistics/date',
    method: 'get'
  })
}

