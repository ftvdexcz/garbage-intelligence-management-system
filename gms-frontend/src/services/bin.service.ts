import { Bin, CreateBinRes } from '@/models/bin';
import { Pagination } from '@/models/pagination';
import { BaseResponse, createInstance } from '@/services/index';

export const listBins = async (): Promise<BaseResponse<Bin[]>> => {
  return await createInstance().get('/garbage-service/bins');
};

export const listBinsPagination = async ({
  page,
  size,
  sortOptions,
}: any): Promise<BaseResponse<Pagination<Bin>>> => {
  return await createInstance().get('/garbage-service/bins/listPagination', {
    params: {
      page,
      size,
      sort: sortOptions,
    },
  });
};

export const getBinById = async (id: string): Promise<BaseResponse<Bin>> => {
  return await createInstance().get(`/garbage-service/bins/${id}`);
};

export const createBin = async (
  data: FormData
): Promise<BaseResponse<CreateBinRes>> => {
  return await createInstance().post(`/garbage-service/bins`, data, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

export const deleteBin = async (id: string): Promise<BaseResponse<null>> => {
  return await createInstance().delete(`/garbage-service/bins/${id}`);
};

export const updateBin = async (
  data: FormData,
  id: string
): Promise<BaseResponse<CreateBinRes>> => {
  return await createInstance().post(
    `/garbage-service/bins/updateBin/${id}`,
    data,
    {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    }
  );
};
