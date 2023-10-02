<template>
  <div>
    <div id="table">
      <el-table
        :data="mailList"
        border
        style="width: 100%">
        <el-table-column
          prop="source"
          label="调度源"
          width="180">
        </el-table-column>
        <el-table-column
          prop="toUser"
          label="收件人"
          width="180">
        </el-table-column>
        <el-table-column
          prop="subject"
          label="主题"
          width="180">
        </el-table-column>
        <el-table-column
          prop="sendTime"
          label="发送时间"
          width="180">
        </el-table-column>
        <el-table-column
          prop="content"
          label="邮件内容">
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="100">
          <template slot-scope="scope">
            <el-button @click="getMailById(scope.row)" type="text" size="small">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 模态框组件 -->
      <el-dialog :visible="showDetailDialog" @close="closeDetailDialog" title="邮件详情">
        <el-row>
          <el-col :span="24">
            <p><strong>调度源:</strong> {{ this.selectedMail.source }}</p>
<!--            <span>调度源:</span>
            <span>{{ this.selectedMail.source }}</span>-->
          </el-col>
          <el-col :span="24">
            <p><strong>主题:</strong> {{ this.selectedMail.subject }}</p>
          </el-col>
          <el-col :span="24">
            <p><strong>收件人:</strong> {{ this.selectedMail.toUser }}</p>
          </el-col>
          <el-col :span="24">
            <p><strong>邮件内容:</strong></p>
            <!-- 使用 v-html 指令来渲染 HTML 内容 -->
            <div v-html="this.selectedMail.content"></div>
          </el-col>
        </el-row>
      </el-dialog>
    </div>

    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="mailPage.currentPage"
        :page-sizes="[10, 15, 20]"
        :page-size="100"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  </div>

</template>

<script>
import api from '@/api/mail/mail'
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
      api.getMailById(mail.emilId).then(res => {
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
      api.mailList(this.mailPage).then(res => {
        this.mailList = res.data.rows;
        this.total = res.data.total
      })
      // endregion

    },
  },
  data() {
    return {
      showDetailDialog: false, // 控制模态框显示状态
      selectedMail: {
        subject: '',
        toUser: '',
        sendTime: '',
        content: '',
        source:''
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
</script>
<style>
#table {
  height: 80vh; /* 设置表格的高度为屏幕高度的100% */
  /* 可以添加其他样式属性，例如滚动条、背景色等 */
  overflow-y: auto; /* 如果表格内容过多，添加垂直滚动条 */
}
</style>
