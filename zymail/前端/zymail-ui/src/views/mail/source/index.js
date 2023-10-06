import source from "@/api/source/source";
import job from "@/api/job/job";

export default {
  data() {
    return {
      radio:0,
      modifyDto:{
        jobGroupName:'',
        jobName:'',
        startTime:'',
        endTime:'',
        cron:''
      },
      type:{
        s: true,
        e: true,
        c: true
      },
      dialogFormVisible:false,
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
      status: undefined,
      appId: '',
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
  watch: {
    appId: function (newValue, oldValue) {
      // 在这里执行您的逻辑，比如调用 this.getJobsByAppId(this.appId);
      this.getJobsByAppId(newValue);
    },
    radio: function (newValue, oldValue){
          if (newValue == 1){
            this.type.s = false;
            this.type.e = true;
            this.type.c = true;
          }else if (newValue == 2){
            this.type.s = true;
            this.type.e = false;
            this.type.c = true;
          }else if (newValue == 3){
            this.type.s = true;
            this.type.e = true;
            this.type.c = false;
          }
    }

  },
  methods: {
    closeModifyView(){
      this.type.s = true;
      this.type.e = true;
      this.type.c = true;
      this.modifyDto = '';
      this.dialogFormVisible = false;
    },
    showModifyView(job){
      this.radio = 0;
      this.modifyDto = job;
      this.dialogFormVisible = true;
    },
    modify() {
      this.dialogFormVisible = false;
        this.modifyJob(this.radio,this.modifyDto)
    },
    modifyJob(radio,jobDto){
        job.modifyJob(radio,jobDto).then(res =>{
          this.$message({
            message: '修改成功',
            type: 'success'
          });
        })
    },
    StartScheduler() {
      if (this.status === 0) {
        this.$notify({
          title: '警告',
          message: '调度器运行中，无需启动!',
          type: 'warning'
        });
        return;
      }
      job.StartScheduler().then(res => {
        this.getSchedulerStatus();
        this.$message({
          message: '启动成功',
          type: 'success'
        });
      })
    },
    PauseScheduler() {
      if (this.status === 1) {
        this.$notify({
          title: '警告',
          message: '调度器处于暂停状态，请勿重复操作!',
          type: 'warning'
        });
        return;
      }
      job.PauseScheduler().then(res => {
        this.getSchedulerStatus();
        this.$message({
          message: '暂停成功',
          type: 'success'
        });
      })
    },
    getSchedulerStatus() {
      job.getSchedulerStatus().then(res => {
        this.status = res.data;
      })
    },
    PauseJob(jobName, jobGroupName, state) {
      if (state === 2) {
        this.$notify({
          title: '警告',
          message: '该任务处于暂停状态，请勿重复操作!',
          type: 'warning'
        });
        return;
      }
      job.PauseJob(jobName, jobGroupName).then(res => {
        this.getJobsByAppId(this.appId);
        this.$message({
          message: '暂停成功',
          type: 'success'
        });
      })
    },
    ResumeJob(jobName, jobGroupName, state) {
      if (state === 1) {
        this.$notify({
          title: '警告',
          message: '该任务正在运行，无需恢复!',
          type: 'warning'
        });
        return;
      }
      job.ResumeJob(jobName, jobGroupName).then(res => {
        this.getJobsByAppId(this.appId);
        this.$message({
          message: '恢复成功',
          type: 'success'
        });
      })
    },
    DelJob(jobName, jobGroupName, state) {
      job.DelJob(jobName, jobGroupName).then(res => {
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
