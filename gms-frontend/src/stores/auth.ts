import { User, newUser } from '@/models/user';
import { defineStore } from 'pinia';

export interface UserAuth {
  accessToken: string;
  user: User;
}

export const useAuthStore = defineStore('auth', {
  state: (): UserAuth => {
    return {
      accessToken: '',
      user: newUser(),
    };
  },

  actions: {
    setUserAuth(userAuth: UserAuth) {
      this.accessToken = userAuth.accessToken;
      this.user = userAuth.user;
    },
    setUser(user: User) {
      this.user = user;
    },
    removeUserAuth() {
      this.accessToken = '';
      this.user = newUser();
    },
  },
});
