import { createInstance } from '@/services/index';
import axios from 'axios';

interface CatDataType {
  id: string;
  height: number;
  url: string;
  width: number;
}

const getCatImg = async (): Promise<CatDataType> => {
  try {
    return await createInstance().get('/v1/images/search');
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw error;
    } else {
      throw new Error('different error than axios');
    }
  }
};
