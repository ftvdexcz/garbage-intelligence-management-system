import axios, {
  AxiosError,
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from 'axios';
import { useCommonStore } from '@/stores/common';

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
  const { method, url } = config;
  console.log(`🚀 [API] ${method?.toUpperCase()} ${url} | Request`);
  onLoading('start');
  return config;
};

const onRequestError = (error: Error | AxiosError): Promise<AxiosError> => {
  onLoading('cancel');
  return Promise.reject(error);
};

const onResponse = (response: AxiosResponse): AxiosResponse => {
  const { method, url } = response.config;
  const { status } = response;
  console.log(`🚀 [API] ${method?.toUpperCase()} ${url} | Response ${status}`);
  onLoading('end');
  return response.data;
};

const onResponseError = (error: AxiosError | Error): Promise<AxiosError> => {
  if (axios.isAxiosError(error)) {
    const { message } = error;
    const { method, url } = error.config as AxiosRequestConfig;
    const { status } = (error.response as AxiosResponse) ?? {};

    console.log(
      `🚨 [API] ${method?.toUpperCase()} ${url} | Error ${status} ${message}`
    );

    switch (status) {
      case 400: {
        // "Bad request"
        break;
      }
      case 401: {
        // "Login required"
        break;
      }
      case 403: {
        // "Permission denied"
        break;
      }
      case 404: {
        // "Invalid request"
        break;
      }
      case 500: {
        // "Server error"
        break;
      }
      default: {
        // "Unknown error occurred"
        break;
      }
    }

    if (status === 401) {
      // Delete Token & Go To Login Page if you required.
      sessionStorage.removeItem('token');
    }
  } else {
    console.log(`🚨 [API] | Error ${error.message}`);
  }

  return Promise.reject(error);
};

export const setupInterceptors = (
  axiosInstance: AxiosInstance
): AxiosInstance => {
  axiosInstance.interceptors.request.use(onRequest, onRequestError);
  axiosInstance.interceptors.response.use(onResponse, onResponseError);
  return axiosInstance;
};
