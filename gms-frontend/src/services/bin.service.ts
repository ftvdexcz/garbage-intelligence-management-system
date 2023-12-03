import { Bin } from '@/models/bin';
import { Pagination } from '@/models/pagination';
import { BaseResponse, createInstance } from '@/services/index';

export const listBins = async (): Promise<BaseResponse<Bin[]>> => {
  return await createInstance().get('/garbage-service/bins');
};

export const listBinsPagination = async ({
  page,
  size,
  sortBy,
}: any): Promise<BaseResponse<Pagination<Bin>>> => {
  return await createInstance().get('/garbage-service/bins/listPagination', {
    params: {
      page,
      size,
      sortBy,
    },
  });
};
