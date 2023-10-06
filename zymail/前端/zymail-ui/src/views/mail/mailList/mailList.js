import mailApi from '@/api/mail/mailApl'

export default {
  created() {
    this.getList()
  },
  methods: {
    closeDetailDialog() {
      // 关闭模态框
      this.showDetailDialog = false;
    },
    getMailById(mail) {
      mailApi.getMailById(mail.mailId).then(res => {
        this.selectedMail = res.data;
        // 打开模态框
        this.showDetailDialog = true;
      })
    },
    // region 分页查询
    handleSizeChange(val) {
      this.mailPage.size = val;
      this.getList();

    },
    handleCurrentChange(val) {
      this.mailPage.currentPage = val;
      this.getList();
    },
    getList() {
      mailApi.mailList(this.mailPage).then(res => {
        this.mailList = res.data.rows;
        this.total = res.data.total
      })
      // endregion

    },
  },
  data() {
    return {
      tableHeight: 'calc(80vh - 50px)', // 50px 用于容纳分页组件
      formLabelWidth:'120px',
      showDetailDialog: false, // 控制模态框显示状态
      selectedMail: {
        subject: '',
        toUser: '',
        sendTime: '',
        content: '',
        source: ''
      }, // 存储选中的邮件数据
      total: 0,
      // 默认第一页

      mailPage: {
        currentPage: 1,
        size: 10
      },
      mailList: [/*{
        source: '系统测试',
        toUser: '3296137356@qq.com',
        subject:'测试邮件',
        sendTime:'2023.8.23 12:23:20',
        content: ' asdsadasdsadasdsadas'
      }*/]
    }
  }
}
