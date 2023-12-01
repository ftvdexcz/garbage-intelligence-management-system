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
