import { AlertType } from '@/models/alert';
import { defineStore } from 'pinia';

interface State {
  loadingCnt: number;
  loading: boolean;
  alert: boolean;
  alertMsg: string;
  alertType: AlertType;
}

export const useCommonStore = defineStore('common', {
  state: (): State => {
    return {
      loadingCnt: 0,
      loading: false,
      alert: false,
      alertMsg: '',
      alertType: AlertType.Info,
    };
  },

  actions: {
    startLoading() {
      this.loadingCnt += 1;
      this.loading = true;
    },
    endLoading() {
      this.loadingCnt -= 1;
      this.loading = false;
    },
    cancelLoading() {
      this.loadingCnt = 0;
      this.loading = false;
    },
    _alert(msg: string, type: AlertType, duration = 3000) {
      this.alert = true;
      this.alertMsg = msg;
      this.alertType = type;

      window.setTimeout(() => {
        this.alert = false;
        this.alertMsg = '';
        this.alertType = AlertType.Info;
      }, duration);
    },
  },
});
