import { User } from '@/models/user';
import { BaseResponse, createInstance } from '@/services/index';
import { UserAuth } from '@/stores/auth';

interface AuthRequest {
  email: string;
  password: string;
}

export const login = async (
  authRequest: AuthRequest
): Promise<BaseResponse<UserAuth>> => {
  return await createInstance().post('/auth', authRequest);
};

export const getMe = async (): Promise<BaseResponse<User>> => {
  return await createInstance().get('/me');
};
