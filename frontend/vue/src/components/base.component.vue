<script>
import qs from 'qs'
export default {
  data() {
    return {
      key: 'id', // 表格行ID
      data: [], // 表格数据
      cacheData: [], // 表格数据缓存
      page: 0, // 表格页
      pageSize: 10, // 表格每页数据
      total: 0, // 表格数据总数
      selectedRows: [], // 选中行
      selectedRowKeys: [], // 选中行ID
      loading: false, // 表格数据加载
      tableScrollY: '', // 表格高度
      body: {} // 查询条件
    }
  },
  computed: {
    collapsed: {
      get() {
        return this.$store.state.common.collapsed
      },
      set(val) {
        this.$store.dispatch('common/updateCollapsed', val)
      }
    },
    breadcrumbs: {
      get() {
        return this.$store.state.common.breadcrumbs
      },
      set(val) {
        this.$store.dispatch('common/updateBreadcrumbs', val)
      }
    }
  },
  created() {
    this.breadcrumbs = this.$route.meta.breadcrumbs || []
  },
  mounted() {
    if (this.$options.name != 'home') {
      this.resize()
      window.onresize = () => {
        return (() => {
          this.resize()
        })()
      }
    }
  },
  methods: {
    // -- table --
    // 重置 table 滚动高度
    resize: function() {
      setTimeout(() => {
        const $el = document.querySelector('.table-content-wrapper')
        if ($el) {
          const $antTableEl = $el.querySelector('.ant-table-wrapper')

          const $antTableHeaderEl = $antTableEl.querySelector(
            '.ant-table-header'
          )
          const $antTableTheadEl = $antTableEl.querySelector(
            '.ant-table-body .ant-table-thead'
          )
          this.tableScrollY =
            $antTableEl.clientHeight -
            ($antTableHeaderEl ? $antTableHeaderEl.clientHeight : 0) -
            ($antTableTheadEl ? $antTableTheadEl.clientHeight : 0)

          setTimeout(() => {
            const $antTableBodyEl = document.querySelector('.ant-table-body')
            if (this.data && this.data.length > 0) {
              $antTableBodyEl.style.minHeight = this.tableScrollY + 'px'
            } else {
              $antTableBodyEl.style.minHeight = 'auto'
            }
          }, 0)
        } else {
          this.tableScrollY = ''
        }
      }, 0)
    },
    // -- 查询 --
    onSimpleSearch: function(val) {
      this.body.search = val
      this.page = 1
      this.getData()
    },
    // -- 分页 --
    // 当前页变化
    onPageChange: function(page, pageSize) {
      this.page = page
      this.pageSize = pageSize
      this.getData()
    },
    // 每页显示数目变化
    onPageSizeChange: function(current, size) {
      this.page = current
      this.pageSize = size
      this.getData()
    },
    // 选择行
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    // -- 确认框 --
    onConfirmCancel: function() {},
    onConfirmOk: function() {},
    // 删除
    delete(url, ids) {
      return this.$http
        .delete(url, {
          params: { ids: ids },
          paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
          }
        })
        .then(() => {
          this.getData()
        })
    },
    // 上传
    uploadChange(info) {
      if (info.file.status === 'done') {
        this.$message.success(
          `${info.file.name}` + this.$i18n.t('common.file_upload_success')
        )
        this.getData()
      } else if (info.file.status === 'error') {
        this.$message.error(
          `${info.file.name}` + this.$i18n.t('common.file_upload_failure')
        )
      }
    },
    // 下载
    download(url, body) {
      this.$http.post(url, body, { responseType: 'blob' }).then(res => {
        let filename = new Date().getTime()
        const cd = res.headers['content-disposition']
        if (cd) {
          filename = cd.substr(cd.indexOf('filename') + 9)
        }
        const blob = new Blob([res.data])
        //对于<a>标签，只有 Firefox 和 Chrome（内核） 支持 download 属性
        //IE10以上支持blob但是依然不支持download
        if ('download' in document.createElement('a')) {
          const link = document.createElement('a')
          link.style.display = 'none'
          link.href = URL.createObjectURL(blob)
          link.setAttribute('download', filename)
          document.body.appendChild(link)
          link.click()
          URL.revokeObjectURL(link.href)
          document.body.removeChild(link)
        } else {
          navigator.msSaveBlob(blob, filename)
        }
      })
    },
    // -- 操作 --
    // 编辑字段
    handlePropChange(val, record, prop) {
      const data = [...this.data]
      if (record) {
        record[prop] = val
        this.data = data
      }
    },
    // 编辑按钮
    edit(record) {
      const data = [...this.data]
      if (record) {
        record.editable = true
        this.data = data
      }
    },
    save(url, record) {
      const data = [...this.data]
      if (record) {
        delete record.editable
        this.data = data
        this.cacheData = data.map(item => ({ ...item }))

        return this.$http.put(url, record).then(() => {
          this.$message.success(this.$i18n.t('common.save_success'))
        })
      }
    },
    editCancel(record) {
      const data = [...this.data]
      if (record) {
        Object.assign(
          record,
          this.cacheData.find(item => record[this.key] === item[this.key])
        )
        delete record.editable
        this.data = data
      }
    }
  }
}
</script>
