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
    </div>
    <div id="body">
      <el-table
        id="table"
        ref="filterTable"
        :data="logs"
        style="width: 100%">
        <el-table-column
          prop="createTime"
          label="时间"
          sortable
          width="180">
        </el-table-column>
        <el-table-column
          prop="level"
          label="级别"
          width="100"
          :filters="[{ text: '通知', value: 0 }, { text: '警告', value: 1 }, { text: '危险', value: 2 }]"
          :filter-method="filterState"
          filter-placement="bottom-end">
          <template slot-scope="scope">
            <el-tag
              :type="getStatusTagType(scope.row.level)"
              disable-transitions>{{ getStatusDescription(scope.row.level) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column
          prop="source"
          label="调度源"
          width="180"
        >
        </el-table-column>
        <el-table-column
          label="内容"
        >
          <template slot-scope="scope">
            <div v-html="scope.row.content"></div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="mailPage.currentPage"
        :page-sizes="[3, 5, 10]"
        :page-size="100"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script src="./log.js" />
<link rel="stylesheet" type="text/css" href="./log.css">


