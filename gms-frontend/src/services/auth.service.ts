import { BaseResponse, createInstance } from '@/services/index';
import axios from 'axios';

interface AuthResponse {
  acccessToken: string;
  user_id: string;
}

interface AuthRequest {
  email: string;
  password: string;
}

export const login = async (
  authRequest: AuthRequest
): Promise<BaseResponse<AuthResponse>> => {
  try {
    return await createInstance().post('/auth', authRequest);
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw error;
    } else {
      throw new Error('different error than axios');
    }
  }
};
