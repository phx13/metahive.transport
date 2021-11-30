<template>
  <div class="container-fluid">
    <vxe-toolbar ref="xToolbar" :loading="loading">
      <template #buttons>
        <vxe-button status="primary" content="临时新增" @click="insertEvent"></vxe-button>
        <vxe-button status="warning" content="临时删除" @click="removeSelectEvent"></vxe-button>
        <vxe-button status="danger" content="直接删除" @click="deleteSelectEvent"></vxe-button>
        <vxe-button status="success" content="确定提交" @click="saveEvent"></vxe-button>
      </template>
    </vxe-toolbar>

    <vxe-table
        border
        resizable
        show-overflow
        keep-source
        ref="xTable"
        height="800"
        :loading="loading"
        :data="tableData"
        :edit-rules="validRules"
        :edit-config="{trigger: 'dblclick', mode: 'row', showUpdateStatus: true, showInsertStatus: true}">
      <vxe-column type="checkbox" width="40"></vxe-column>

      <!--基础信息-->
      <vxe-column field="id" title="编号" width="166" :edit-render="{}">
        <template #edit="{ row }">
          <vxe-input v-model="row.id" type="text"></vxe-input>
        </template>
      </vxe-column>
      <vxe-column field="username" title="负责人" width="90" :edit-render="{}">
        <template #edit="{ row }">
          <vxe-input v-model="row.username" type="text"></vxe-input>
        </template>
      </vxe-column>
      <vxe-column field="type" title="车厢类型" width="100" :edit-render="{}">
        <template #edit="{ row }">
          <vxe-input v-model="row.type" type="text"></vxe-input>
        </template>
      </vxe-column>

      <!--位置信息-->
      <vxe-column field="currentCoord" title="当前坐标" :edit-render="{}">
        <template #edit="{ row }">
          <vxe-input v-model="row.currentCoord" type="text"></vxe-input>
        </template>
      </vxe-column>
      <vxe-column field="fromName" title="来自" width="90" :edit-render="{}">
        <template #edit="{ row }">
          <vxe-input v-model="row.fromName" type="text"></vxe-input>
        </template>
      </vxe-column>
      <vxe-column field="fromCoord" title="来自坐标" :edit-render="{}">
        <template #edit="{ row }">
          <vxe-input v-model="row.fromCoord" type="text"></vxe-input>
        </template>
      </vxe-column>
      <vxe-column field="toName" title="去往" width="90" :edit-render="{}">
        <template #edit="{ row }">
          <vxe-input v-model="row.toName" type="text"></vxe-input>
        </template>
      </vxe-column>
      <vxe-column field="toCoord" title="去往坐标" :edit-render="{}">
        <template #edit="{ row }">
          <vxe-input v-model="row.toCoord" type="text"></vxe-input>
        </template>
      </vxe-column>

      <!--传感器信息-->
      <vxe-column field="oil" title="油量" width="90" :edit-render="{}">
        <template #edit="{ row }">
          <vxe-input v-model="row.oil" type="text"></vxe-input>
        </template>
      </vxe-column>
      <vxe-column field="pressure" title="压力" width="90" :edit-render="{}">
        <template #edit="{ row }">
          <vxe-input v-model="row.pressure" type="text"></vxe-input>
        </template>
      </vxe-column>
      <vxe-column field="temperature" title="温度" width="90" :edit-render="{}">
        <template #edit="{ row }">
          <vxe-input v-model="row.temperature" type="text"></vxe-input>
        </template>
      </vxe-column>
      <vxe-column field="humidity" title="湿度" width="90" :edit-render="{}">
        <template #edit="{ row }">
          <vxe-input v-model="row.humidity" type="text"></vxe-input>
        </template>
      </vxe-column>
      <vxe-column field="updateTime" title="更新时间" :edit-render="{}">
        <template #edit="{ row }">
          <vxe-input v-model="row.updateTime" type="date"></vxe-input>
        </template>
      </vxe-column>

      <!--      选项类型模板-->
      <!--      <vxe-column field="sex" title="Sex" :edit-render="{}">-->
      <!--        <template #default="{ row }">-->
      <!--          <span>{{ formatSex(row.sex) }}</span>-->
      <!--        </template>-->
      <!--        <template #edit="{ row }">-->
      <!--          <vxe-select v-model="row.sex" type="text" :options="sexList" transfer></vxe-select>-->
      <!--        </template>-->
      <!--      </vxe-column>-->

      <!--      操作模板-->
      <!--    <vxe-column title="操作" width="240">-->
      <!--      <template #default="{ row }">-->
      <!--        <vxe-button status="warning" content="临时删除" @click="removeRowEvent(row)"></vxe-button>-->
      <!--        <vxe-button status="danger" content="直接删除" @click="deleteRowEvent(row)"></vxe-button>-->
      <!--      </template>-->
      <!--    </vxe-column>-->
    </vxe-table>
  </div>

</template>

<script>

import XEAjax from 'xe-ajax'

export default {
  name: "ManagementTable",
  data() {
    return {
      loading: false,
      tableData: [],
      validRules: {
        oil: [
          {required: true, message: '油量必须填写'}
        ]
      }
    }
  },
  created() {
    this.$nextTick(() => {
      // 将表格和工具栏进行关联
      const $table = this.$refs.xTable;
      $table.connect(this.$refs.xToolbar);
    })
    this.loadList();
  },
  methods: {
    async loadList() {
      this.loading = true;
      try {
        const res = await fetch('/api/server/coach/all').then(response => response.json());
        console.log(res);
        this.tableData = res;
      } catch (e) {
        this.tableData = [];
      }
      this.loading = false;
    },
    formatSex(value) {
      if (value === '1') {
        return '男';
      }
      if (value === '0') {
        return '女';
      }
      return '';
    },
    async insertEvent() {
      const $table = this.$refs.xTable;
      const newRecord = {};
      const {row: newRow} = await $table.insert(newRecord);
      await $table.setActiveRow(newRow);
    },
    async removeSelectEvent() {
      const $table = this.$refs.xTable;
      await $table.removeCheckboxRow();
    },
    async deleteSelectEvent() {
      const type = await this.$XModal.confirm('您确定要删除选中的数据?');
      if (type !== 'confirm') {
        return;
      }
      const $table = this.$refs.xTable;
      const checkboxRecords = $table.getCheckboxRecords();
      this.loading = true;
      try {
        const body = {removeRecords: checkboxRecords};
        await XEAjax.post('/api/server/coach/save', body);
        await this.loadList();
      } catch (e) {
      }
      this.loading = false;
    },
    async removeRowEvent(row) {
      const $table = this.$refs.xTable;
      await $table.remove(row);
    },
    async deleteRowEvent(row) {
      const type = await this.$XModal.confirm('您确定要删除该数据?');
      if (type !== 'confirm') {
        return;
      }
      this.loading = true;
      try {
        const body = {removeRecords: [row]};
        await XEAjax.post('/api/server/coach/save', body);
        await this.loadList();
      } catch (e) {
      }
    },
    async saveEvent() {
      const $table = this.$refs.xTable;
      const {insertRecords, removeRecords, updateRecords} = $table.getRecordset();
      if (insertRecords.length <= 0 && removeRecords.length <= 0 && updateRecords.length <= 0) {
        await this.$XModal.message({content: '数据未改动！', status: 'warning'});
        return;
      }
      const errMap = await $table.validate().catch(errMap => errMap);
      if (errMap) {
        return;
      }
      this.loading = true;
      try {
        const body = {insertRecords, removeRecords, updateRecords};
        await XEAjax.post('/api/server/coach/save', body);
        await this.loadList();
        await this.$XModal.message({
          content: `操作成功，新增 ${insertRecords.length} 条，更新 ${updateRecords.length} 条，删除 ${removeRecords.length} 条`,
          status: 'success'
        });
      } catch (e) {
        if (e && e.message) {
          await this.$XModal.message({content: e.message, status: 'error'});
        }
      }
      this.loading = false;
    }
  }
}

</script>

<style scoped>

</style>