<template>
  <div>
    <app-header :breadcrumbs="breadcrumbs"></app-header>
    <div class="scroll-wrapper no-footer">
      <div class="content-wrapper">
        <div id="line"></div>
      </div>
    </div>
  </div>
</template>

<script>
import * as G2 from '@antv/g2'
import { format, subMinutes } from 'date-fns'
import BaseComponent from '@/components/base.component'
import AppHeader from '@/components/header'

export default {
  extends: BaseComponent,
  components: {
    AppHeader: AppHeader
  },
  data() {
    return {
      interval: null,
      chart: null
    }
  },
  mounted() {
    this.renderLine()
  },
  destroyed() {
    if (this.interval) {
      clearInterval(this.interval)
    }
  },
  methods: {
    renderLine() {
      const wrapper = document.querySelector('.content-wrapper')
      this.chart = new G2.Chart({
        container: 'line',
        forceFit: true,
        width: wrapper.clientWidth,
        animate: false
      })
      let time = new Date().getTime()
      let lastTime = subMinutes(time, 1).getTime()
      const data = []
      while (time > lastTime) {
        const second = format(lastTime, 'mm:ss')
        data.push({
          second,
          value: Math.random()
        })
        lastTime += 1000
      }
      this.chart.source(data)
      this.chart.tooltip({
        crosshairs: {
          type: 'cross'
        }
      })
      this.chart.scale('value', {
        alias: '销售额(万)'
      })
      this.chart.line().position('second*value')
      this.chart
        .point()
        .position('second*value')
        .size(4)
        .shape('circle')
        .style({
          stroke: '#fff',
          lineWidth: 1
        })
      this.chart.render()
      this.interval = setInterval(() => {
        const _data = data
        const second = format(time, 'mm:ss')
        _data.shift()
        _data.push({
          second,
          value: Math.random()
        })
        this.chart.changeData(_data)
        time += 1000
      }, 1000)
    }
  }
}
</script>

<style scoped>
</style>
