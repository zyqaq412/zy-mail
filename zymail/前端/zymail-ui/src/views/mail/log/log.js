import sourceApi from "@/api/source/sourceApi";
import logApi from "@/api/log/logApi";

export default {
  data() {
    return {
      total: 0,
      mailPage: {
        currentPage: 1,
        size: 3,
      },
      appId:'',
      logs:[],
      status:undefined,
      appIds: [/*'zymail-server','测试系统'*/],
      // 状态数字到状态描述的映射
      statusMap: {
        0: '通知',
        1: '警告',
        2: '危险',
      },
    }
  },
  mounted() {
    this.getSources();
    this.getPageLogs();
  },
  watch:{
    appId: function(newValue, oldValue) {
      // 在这里执行您的逻辑，比如调用 this.getJobsByAppId(this.appId);
      this.getPageLogs();
    }

  },
  methods: {
    // region 分页查询
    handleSizeChange(val) {
      this.mailPage.size = val;
      this.getPageLogs();
    },
    handleCurrentChange(val) {
      this.mailPage.currentPage = val;
      this.getPageLogs();
    },
    getPageLogs() {
      logApi.getPageLogs(this.mailPage,this.appId).then(res=>{
        this.logs = res.data.rows;
        this.total = res.data.total
      })
    },
    // endregion

    getSources() {
      sourceApi.getSources().then(res => {
        this.appIds = res.data;
      })
    },
    // 根据状态数字获取状态标签的类型（颜色）
    getStatusTagType(state) {
      switch (state) {
        case 0:
          return 'info';
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
    filterState(value, row) {
      return row.level === value;
    },
  }
}
