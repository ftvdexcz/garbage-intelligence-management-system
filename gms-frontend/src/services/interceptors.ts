import {
  AxiosError,
  AxiosInstance,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from 'axios';
import { useCommonStore } from '@/stores/common';

interface BaseResponse {
  code: number;
  status: string;
  message: string;
  data: any;
}

const onLoading = async (type: string) => {
  const commonStore = useCommonStore();
  const { startLoading, endLoading, cancelLoading } = commonStore;

  switch (type) {
    case 'start': {
      startLoading();
      break;
    }
    case 'end': {
      endLoading();
      break;
    }
    case 'cancelLoading': {
      cancelLoading();
      break;
    }
    default: {
      break;
    }
  }
};

const onRequest = (
  config: InternalAxiosRequestConfig
): InternalAxiosRequestConfig => {
  onLoading('start');
  return config;
};

const onRequestError = (error: Error | AxiosError): Promise<AxiosError> => {
  onLoading('cancel');
  return Promise.reject(error);
};

const onResponse = (response: AxiosResponse): AxiosResponse<BaseResponse> => {
  onLoading('end');
  return response;
};

const onResponseError = (error: AxiosError | Error): Promise<AxiosError> => {
  onLoading('cancel');
  return Promise.reject(error);
};

export const setupInterceptors = (
  axiosInstance: AxiosInstance
): AxiosInstance => {
  axiosInstance.interceptors.request.use(onRequest, onRequestError);
  axiosInstance.interceptors.response.use(onResponse, onResponseError);
  return axiosInstance;
};
