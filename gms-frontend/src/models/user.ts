export interface User {
  id: string;
  username: string;
  phone: string;
  email: string;
  role: {
    roleName: string;
    roleType: number;
  };
  createdDate: string;
  updatedDate: string;
}

export const newUser = (): User => {
  return {
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
};
