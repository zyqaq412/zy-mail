<template>
  <div>
    <div id="table">
      <el-table
        :data="mailList"
        border
        :height="tableHeight"
        :header-fixed="true"
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

<script src="./mailList.js" />
<link rel="stylesheet" type="text/css" href="mailList.css">
