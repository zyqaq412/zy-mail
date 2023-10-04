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
          <!-- 默认不用html格式显示内容   不然不太整起-->
          <!--          <template slot-scope="scope">
                      <div v-html="scope.row.content"></div>
                    </template>-->
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


      <el-dialog title="邮件详情" :visible.sync="showDetailDialog" fullscreen="true">
        <el-form-item label="调度源" :label-width="formLabelWidth">
          <el-input v-model="selectedMail.source" autocomplete="off" :readonly="true"></el-input>
        </el-form-item>
        <el-form :model="selectedMail">
          <el-form-item label="邮件主题" :label-width="formLabelWidth">
            <el-input v-model="selectedMail.subject" autocomplete="off" :readonly="true"></el-input>
          </el-form-item>
          <el-form-item label="收件人" :label-width="formLabelWidth">
            <el-input v-model="selectedMail.toUser" autocomplete="off" :readonly="true"></el-input>
          </el-form-item>

          <el-form-item label="发送时间" :label-width="formLabelWidth">
            <el-input v-model="selectedMail.sendTime" autocomplete="off" :readonly="true"></el-input>
          </el-form-item>
          <el-form-item label="邮件内容" :label-width="formLabelWidth">
              <div v-html="selectedMail.content" class="mail-content"></div>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="closeDetailDialog">关闭</el-button>
        </div>
      </el-dialog>




<!--      &lt;!&ndash; 模态框组件 &ndash;&gt;
      <el-dialog id="mailDetails" :visible="showDetailDialog" @close="closeDetailDialog" title="邮件详情"
                 fullscreen="true"
      >
        <div>
          <el-row>
            <el-col :span="24">
              <p><strong>调度源:</strong> {{ this.selectedMail.source }}</p>
              &lt;!&ndash;            <span>调度源:</span>
                          <span>{{ this.selectedMail.source }}</span>&ndash;&gt;
            </el-col>
            <el-col :span="24">
              <p><strong>主题:</strong> {{ this.selectedMail.subject }}</p>
            </el-col>
            <el-col :span="24">
              <p><strong>收件人:</strong> {{ this.selectedMail.toUser }}</p>
            </el-col>
            <el-col :span="24">
              <p><strong>邮件内容:</strong></p>
              &lt;!&ndash; 使用 v-html 指令来渲染 HTML 内容 &ndash;&gt;
              <div v-html="this.selectedMail.content"></div>
            </el-col>
          </el-row>
        </div>

      </el-dialog>-->
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
      api.getMailById(mail.mailId).then(res => {
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
</script>
<style>
#table {
  height: 80vh; /* 设置表格的高度为屏幕高度的100% */
  /* 可以添加其他样式属性，例如滚动条、背景色等 */
  overflow-y: auto; /* 如果表格内容过多，添加垂直滚动条 */
}
/*#mailDetails{
  height: 100%; !* 设置内容容器高度为100% *!
  padding: 20px; !* 添加内边距以增加内容的间距 *!
}*/
/* 在样式表中定义样式类 */
.mail-content {
  border: 1px solid #ccc;
  padding: 10px;
  background-color: #f5f5f5;
  overflow: hidden; /* 控制内容溢出时的显示方式 */
  max-height: 1200px; /* 设置最大高度，根据需要调整 */
}

.mail-content img {
  max-width: 100%; /* 图片最大宽度为父容器的100% */
  height: auto; /* 图片高度自适应，以保持宽高比 */
}
</style>
