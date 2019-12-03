import Vue from 'vue';
import { format } from 'date-fns';
import { zhCN } from 'date-fns/locale'
import { utcToZonedTime } from 'date-fns-tz'

// 挂载管道
Vue.filter('date', date)

const timeZone = process.env.VUE_APP_I18N_TIMEZONE || 'UCT+8'

export function date(value, formatter = 'yyyy-MM-dd HH:mm:ss') {
  if (value) {
    return format(utcToZonedTime(value, timeZone), formatter, { locale: zhCN })
  }
}