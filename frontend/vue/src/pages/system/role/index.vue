<template>
  <div>
    <app-header :breadcrumbs="breadcrumbs" :searchType="'simple'" @onSearch="onSimpleSearch">
      <div slot="action-group">
        <a-button type="link">{{ $t('common.new') }}</a-button>
        <a-divider type="vertical" />
        <a-popconfirm
          :title="$t('common.confirm_delete')"
          :okText="$t('common.confirm_ok')"
          :cancelText="$t('common.confirm_cancel')"
          @confirm="onDeleteConfirmOk()"
          @cancel="onConfirmCancel"
        >
          <a-button type="link" :disabled="selectedRowKeys.length === 0">{{ $t('common.delete') }}</a-button>
        </a-popconfirm>
        <a-divider type="vertical" />
      </div>
    </app-header>
    <div class="table-content-wrapper">
      <a-table
        :columns="columns"
        :rowKey="record => record.username"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        :dataSource="data"
        :pagination="false"
        :loading="loading"
        :scroll="{ y: tableScrollY }"
      >
        <template v-for="prop in ['name']" :slot="prop" slot-scope="text, record">
          <a-input
            v-if="record.editable"
            :key="prop"
            size="small"
            style="width: calc(100% - 30px);"
            :value="text"
            @change="e => handlePropChange(e.target.value, record, prop)"
          />
          <span v-else :key="prop" style="word-break: break-all;">{{text}}</span>
        </template>
        <!-- 角色 -->
        <span slot="role" slot-scope="roles">
          <a-tag :key="role.name" v-for="role in roles" color="geekblue">{{role.dname.value}}</a-tag>
        </span>
        <!-- 状态 -->
        <span slot="status" slot-scope="status, record">{{record.dstatus.value}}</span>
        <!-- 操作 -->
        <span slot="operation" slot-scope="text, record">
          <template v-if="record.editable">
            <a-button
              type="link"
              size="small"
              @click="save($api.AUTH_SAVE, record)"
            >{{ $t('common.save') }}</a-button>
            <a-divider type="vertical" />
            <a-popconfirm
              :title="$t('common.confirm_save_cancel')"
              :okText="$t('common.confirm_ok')"
              :cancelText="$t('common.confirm_cancel')"
              @confirm="editCancel(record)"
            >
              <a-button type="link" size="small">{{ $t('common.cancel') }}</a-button>
            </a-popconfirm>
          </template>
          <template v-else>
            <a-button type="link" size="small" @click="edit(record)">{{ $t('common.edit') }}</a-button>
            <a-divider type="vertical" />
            <a-popconfirm
              :title="$t('common.confirm_delete')"
              :okText="$t('common.confirm_ok')"
              :cancelText="$t('common.confirm_cancel')"
              @confirm="onDeleteConfirmOk(record.id)"
              @cancel="onConfirmCancel"
            >
              <a-button type="link" size="small">{{ $t('common.delete') }}</a-button>
            </a-popconfirm>
          </template>
        </span>
      </a-table>
    </div>
    <app-footer :total="total" @onPageChange="onPageChange" @onPageSizeChange="onPageSizeChange">
      <div slot="action-group">
        <a-menu mode="horizontal">
          <a-menu-item>
            <a-upload
              name="file"
              :multiple="false"
              :showUploadList="false"
              :withCredentials="true"
              :action="$http.baseURL + $api.ROLE_UPLOAD"
              @change="uploadChange"
            >{{ $t('common.import') }}</a-upload>
          </a-menu-item>
          <a-dropdown :trigger="['click']" :placement="'topLeft'">
            <li class="ant-menu-item">{{ $t('common.export') }}</li>
            <a-menu slot="overlay">
              <a-menu-item>
                <a
                  target="_blank"
                  :href="$http.baseURL + $api.ROLE_TEMPLATE"
                >{{ $t('common.export_template') }}</a>
              </a-menu-item>
              <a-menu-item>
                <a
                  href="javascript:void(0)"
                  @click="download($api.ROLE_DOWNLOAD, selectedRowKeys)"
                >{{ $t('common.export_data') }}</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </a-menu>
      </div>
    </app-footer>
  </div>
</template>

<script>
import BaseComponent from '@/components/base.component'
import AppHeader from '@/components/header'
import AppFooter from '@/components/footer'
import {
  Table,
  Divider,
  Button,
  Menu,
  Dropdown,
  Tag,
  Popconfirm,
  Upload,
  Input
} from 'ant-design-vue'
import { date } from '@/pipes'

const columns = [
  {
    title: '序号',
    width: '10%',
    align: 'center',
    customRender: (text, record, index) => {
      return `${index + 1}`
    }
  },
  {
    title: '角色名',
    dataIndex: 'name',
    width: '15%',
    align: 'center',
    scopedSlots: { customRender: 'name' }
  },
  {
    title: '角色名',
    dataIndex: 'dname',
    width: '10%',
    align: 'center',
    customRender: (text, record) => {
      return `${(record.dname && record.dname.value) || text}`
    }
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: '10%',
    align: 'center',
    scopedSlots: { customRender: 'status' }
  },
  {
    title: '修改人',
    dataIndex: 'modifier',
    width: '15%',
    align: 'center'
  },
  {
    title: '修改时间',
    dataIndex: 'modifiedDate',
    width: '15%',
    align: 'center',
    customRender: value => {
      return date(value)
    }
  },
  {
    title: '操作',
    dataIndex: 'operation',
    width: '20%',
    align: 'center',
    scopedSlots: { customRender: 'operation' }
  }
]

export default {
  name: 'role',
  extends: BaseComponent,
  components: {
    ATable: Table,
    ADivider: Divider,
    AButton: Button,
    AMenu: Menu,
    AMenuItem: Menu.Item,
    ADropdown: Dropdown,
    ATag: Tag,
    APopconfirm: Popconfirm,
    AUpload: Upload,
    AInput: Input,
    AppHeader: AppHeader,
    AppFooter: AppFooter
  },
  data() {
    return {
      columns,
      data: []
    }
  },
  created() {
    this.getData()
  },
  mounted() {},
  methods: {
    getData() {
      this.loading = true
      this.$http
        .post(this.$api.ROLE_PAGE, {
          page: this.page - 1,
          size: this.pageSize,
          object: { ...this.body }
        })
        .then(({ data }) => {
          this.loading = false
          this.data = data.content
          this.total = data.totalElements
          this.cacheData = this.data.map(item => ({ ...item }))
          this.resize()
        })
        .catch(err => {
          console.error(err)
          this.loading = false
        })
    },
    // 删除
    onDeleteConfirmOk(id) {
      let ids = this.selectedRowKeys
      if (id) {
        ids = [id]
      }
      this.delete(this.$api.ROLE_DELETE, ids)
    }
  }
}
</script>

<style lang="scss" scoped>
</style>