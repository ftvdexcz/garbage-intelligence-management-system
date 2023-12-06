import { Pagination } from '@/models/pagination';
import { Truck } from '@/models/truck';
import { BaseResponse, createInstance } from '@/services/index';

export interface TruckRequest {
  plate: string;
  company: string;
  capacity: number;
}

export const createTruck = async (
  truckRequest: TruckRequest
): Promise<BaseResponse<Truck>> => {
  return await createInstance().post('/garbage-service/trucks', truckRequest);
};

export const listBinsPagination = async ({
  page,
  size,
  sortOptions,
}: any): Promise<BaseResponse<Pagination<Truck>>> => {
  return await createInstance().get('/garbage-service/trucks/listPagination', {
    params: {
      page,
      size,
      sort: sortOptions,
    },
  });
};

export const getTruckByPlate = async (
  plate: string
): Promise<BaseResponse<Truck>> => {
  return await createInstance().get(`/garbage-service/trucks/${plate}`);
};

export const deleteTruck = async (
  plate: string
): Promise<BaseResponse<null>> => {
  return await createInstance().delete(`/garbage-service/trucks/${plate}`);
};
