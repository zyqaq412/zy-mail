<el-dialog title="邮件详情" :visible.sync="showDetailDialog" fullscreen="true">
<el-form-item label="调度源" :label-width="formLabelWidth">
  <el-input v-model="selectedMail.source" autocomplete="off" :readonly="true"></el-input>
</el-form-item>
......
<div slot="footer" class="dialog-footer"> <el-button type="primary" @click="closeDetailDialog">关闭</el-button> </div>
</el-dialog>
