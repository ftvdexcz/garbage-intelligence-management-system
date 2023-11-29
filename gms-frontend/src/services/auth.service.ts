import { createInstance } from '@/services/index';
import axios from 'axios';

interface authResponse {
  acccessToken: string;
  user_id: string;
}

export const login = async (
  email: string,
  password: string
): Promise<authResponse> => {
  try {
    return await createInstance().get('/auth');
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw error;
    } else {
      throw new Error('different error than axios');
    }
  }
};
