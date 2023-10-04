import request from '@/utils/request'

const api_name = '/jobs'
export default {

  // 获取全部任务
  getAllJobs() {
    return request({
      url: `${api_name}`,
      method: 'get',
    })
  },
  getJobsByAppId(appId) {
    return request({
      url: `${api_name}/${appId}`,
      method: 'get',
    })
  },
  // 暂停任务
  PauseJob(jobName, jobGroupName) {
    return request({
      url: `/jobs/pause/${jobName}/${jobGroupName}`,
      method: 'put'
    })
  },
  // 恢复任务
  ResumeJob(jobName, jobGroupName) {
    return request({
      url: `/jobs/resume/${jobName}/${jobGroupName}`,
      method: 'put'
    })
  },
  // 删除任务
  DelJob(jobName, jobGroupName) {
    return request({
      url: `/jobs/${jobName}/${jobGroupName}`,
      method: 'delete'
    })
  },
  // 获取调度源状态
  getSchedulerStatus(){
    return request({
      url: '/jobs/scheduler/status',
      method: 'get'
    })
  },
  // 暂停调度器
  PauseScheduler () {
    return request({
      url: '/jobs/scheduler/pause',
      method: 'put'
    })
  },
  // 启动调度器
  StartScheduler () {
    return request({
      url: '/jobs/scheduler/start',
      method: 'put'
    })
  }
}
