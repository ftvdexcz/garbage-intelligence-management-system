import { Bin } from '@/models/bin';
import { BaseResponse, createInstance } from '@/services/index';

export const listBins = async (): Promise<BaseResponse<Bin[]>> => {
  return await createInstance().get('/garbage-service/bins');
};
