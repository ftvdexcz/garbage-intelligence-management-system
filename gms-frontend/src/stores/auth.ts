import { User } from '@/models/user';
import { defineStore } from 'pinia';

export interface UserAuth {
  accessToken: string;
  user: User;
}

export const useAuthStore = defineStore('auth', {
  state: (): UserAuth => {
    return {
      accessToken: '',
      user: {
        id: '',
        username: '',
        phone: '',
        email: '',
        role: {
          roleName: '',
          roleType: 0,
        },
        createdDate: '',
        updatedDate: '',
      },
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
      this.user = {
        id: '',
        username: '',
        phone: '',
        email: '',
        role: {
          roleName: '',
          roleType: 0,
        },
        createdDate: '',
        updatedDate: '',
      };
    },
  },
});
