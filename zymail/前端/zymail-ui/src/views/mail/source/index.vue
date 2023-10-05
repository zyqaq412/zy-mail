<template>
  <div>
    <div id="head">
      <el-select v-model="appId" placeholder="请选择调度源">
        <el-option
          v-for="id in appIds"
          :key="id"
          :label="id"
          :value="id">
        </el-option>
      </el-select>
      <span style="margin-left: 25px">调度器状态：
                <el-tag   :type="getStatusType_source(status)"
                           disable-transitions>{{ getStatusDescription_source(status) }}
                    </el-tag>
            </span>
      <span style="margin-left: 50px">操作：</span>
      <el-button type="success" plain @click="StartScheduler">开启调度</el-button>
      <el-button type="warning" plain @click="PauseScheduler">暂停调度</el-button>
    </div>
    <div id="body">
      <el-table
        ref="filterTable"
        :data="jobs"
        id="table"
        style="width: 100%">
        <el-table-column
          prop="jobName"
          label="工作名"
          width="180"
        >
        </el-table-column>
        <el-table-column
          prop="state"
          label="状态"
          width="100"
          :filters="[{ text: '不存在', value: 0 }, { text: '运行', value: 1 }, { text: '暂停', value: 2 }
                    , { text: '完成', value: 3 }, { text: '错误', value: 4 }, { text: '阻塞', value: 5 } ]"
          :filter-method="filterState"
          filter-placement="bottom-end">
          <template slot-scope="scope">
            <el-tag
              :type="getStatusTagType(scope.row.state)"
              disable-transitions>{{ getStatusDescription(scope.row.state) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column
          prop="startTime"
          label="开始时间"
          sortable
          width="180"
        >
        </el-table-column>
        <el-table-column
          prop="endTime"
          label="结束时间"
          sortable
          width="180"
        >
        </el-table-column>
        <el-table-column
          prop="previousFireTime"
          label="上次调度时间"
          sortable
          width="180"
        >
        </el-table-column>
        <el-table-column
          prop="nextFireTime"
          label="下次调度时间"
          sortable
          width="180"
        >
        </el-table-column>
        <el-table-column
          prop="mail.subject"
          label="主题"
          width="180"
        >
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="100">
          <template slot-scope="scope">
            <el-button type="warning" @click="PauseJob(scope.row.jobName,scope.row.jobGroupName,scope.row.state)" size="small" plain>暂停</el-button>
            <el-button type="success" @click="ResumeJob(scope.row.jobName,scope.row.jobGroupName,scope.row.state)" size="small" plain>恢复</el-button>
            <el-button type="danger"  @click="DelJob(scope.row.jobName,scope.row.jobGroupName,scope.row.state)" size="small" plain>移除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    </div>
</template>

<script>
import source from "@/api/source/source";
import job from "@/api/job/job";

export default {
  data() {
    return {
      jobs: [{
        /*        mail:{subject:'测试'},
                jobGroupName:'测试系统',
                :state5,
                jobName:'send',
                startTime:'2023-12-12 12:12:12',
                endTime:'2023-12-12 12:12:12',
                previousFireTime:'2023-12-12 12:12:12',
                nextFireTime:'2023-12-12 12:12:12',*/
      }],
      status:undefined,
      appId:'',
      appIds: [/*'zymail-server','测试系统'*/],
      // 状态数字到状态描述的映射
      statusMap: {
        0: '未启动',
        1: '运行',
        2: '暂停',
        3: '完成',
        4: '错误',
        5: '阻塞'
      },
      statusMap_source: {
        0: '启动',
        1: '暂停',
        2: '永久关闭',
        3: '未知',
      },
    }
  },
  mounted() {
    this.getSources();
    this.getJobsByAppId(this.appId);
    this.getSchedulerStatus();
  },
  created() {

  },
  watch:{
    appId: function(newValue, oldValue) {
      // 在这里执行您的逻辑，比如调用 this.getJobsByAppId(this.appId);
      this.getJobsByAppId(newValue);
    }

  },
  methods: {
    StartScheduler () {
      if (this.status === 0){
        this.$notify({
          title: '警告',
          message: '调度器运行中，无需启动!',
          type: 'warning'
        });
        return;
      }
      job.StartScheduler().then(res=>{
        this.getSchedulerStatus();
        this.$message({
          message: '启动成功',
          type: 'success'
        });
      })
    },
    PauseScheduler () {
      if (this.status===1){
        this.$notify({
          title: '警告',
          message: '调度器处于暂停状态，请勿重复操作!',
          type: 'warning'
        });
        return;
      }
      job.PauseScheduler().then(res=>{
        this.getSchedulerStatus();
        this.$message({
          message: '暂停成功',
          type: 'success'
        });
      })
    },
    getSchedulerStatus(){
     job.getSchedulerStatus().then(res=>{
       this.status = res.data;
     })
    },
    PauseJob (jobName, jobGroupName,state) {
      if (state === 2){
        this.$notify({
          title: '警告',
          message: '该任务处于暂停状态，请勿重复操作!',
          type: 'warning'
        });
        return;
      }
      job.PauseJob(jobName,jobGroupName).then(res=>{
        this.getJobsByAppId(this.appId);
        this.$message({
          message: '暂停成功',
          type: 'success'
        });
      })
    },
   ResumeJob (jobName, jobGroupName,state) {
     if (state === 1){
       this.$notify({
         title: '警告',
         message: '该任务正在运行，无需恢复!',
         type: 'warning'
       });
       return;
     }
      job.ResumeJob(jobName,jobGroupName).then(res=>{
        this.getJobsByAppId(this.appId);
        this.$message({
          message: '恢复成功',
          type: 'success'
        });
      })
    },
    DelJob (jobName, jobGroupName,state) {
      job.DelJob(jobName,jobGroupName).then(res=>{
        this.getJobsByAppId(this.appId);
        this.$message({
          message: '移除成功',
          type: 'success'
        });
    })
    },
    getAllJobs() {
      job.getAllJobs().then(res => {
        this.jobs = res.data;
      })
    },
    getJobsByAppId(appId) {
      job.getJobsByAppId(appId).then(res => {
        this.jobs = res.data;
      })
    },
    getSources() {
      source.getSources().then(res => {
        this.appIds = res.data;
      })
    },
    // 根据状态数字获取状态标签的类型（颜色）
    getStatusTagType(state) {
      switch (state) {
        case 0:
          return 'info'; // 未启动，可以根据实际需要设置不同的颜色
        case 1:
          return 'success'; // 运行
        case 2:
          return 'warning'; // 暂停
        case 3:
          return 'info'; // 完成
        case 4:
          return 'danger'; // 错误
        case 5:
          return 'danger'; // 阻塞
        default:
          return 'info'; // 未知状态
      }
    },
    getStatusType_source(state) {
      switch (state) {
        case 0:
          return 'success';
        case 1:
          return 'warning';
        case 2:
          return 'danger';
        default:
          return 'info'; // 未知状态
      }
    },
    // 根据状态数字获取状态描述
    getStatusDescription(state) {
      return this.statusMap[state] || '未知状态';
    },
    getStatusDescription_source(state) {
      return this.statusMap_source[state] || '未知状态';
    },

    filterState(value, row) {
      return row.state === value;
    },
  }
}
</script>
<style>
#table {
  height: 75vh; /* 设置表格的高度为屏幕高度的100% */
  /* 可以添加其他样式属性，例如滚动条、背景色等 */
  overflow-y: auto; /* 如果表格内容过多，添加垂直滚动条 */
}
#head {
  background-color: #f0f0f0; /* 设置表头背景颜色 */
  border-bottom: 1px solid #ddd; /* 添加底部边框 */
  padding: 10px; /* 添加内边距 */
}

/* 表体样式 */
#body {
  /* 如果需要添加背景色或其他样式，可以在这里添加 */
  padding: 10px; /* 添加内边距 */
}
</style>
