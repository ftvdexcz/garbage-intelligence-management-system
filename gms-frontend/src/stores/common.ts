import { defineStore } from 'pinia';

interface State {
  loadingCnt: number;
  loading: boolean;
}

export const useCommonStore = defineStore('common', {
  state: (): State => {
    return {
      loadingCnt: 0,
      loading: false,
    };
  },

  actions: {
    startLoading() {
      this.loadingCnt += 1;
      this.loading = true;
    },
    endLoading() {
      this.loadingCnt -= 1;
    },
    cancelLoading() {
      this.loadingCnt = 0;
      this.loading = false;
    },
  },
});
