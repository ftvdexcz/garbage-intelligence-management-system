import axios, {
  AxiosError,
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from 'axios';
import { useCommonStore } from '@/stores/common';
import { AlertType } from '@/models/alert';

import { camelizeKeys, decamelizeKeys } from 'humps';
import router from '@/router/index';
import { useAuthStore } from '@/stores/auth';
import { isObjKey } from '@/helpers/utils';

interface ResponseError {
  code: number;
  message: string;
  status: string;
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
  const authStore = useAuthStore();
  onLoading('start');
  const { method, url } = config;
  console.log(`🚀 [API] ${method?.toUpperCase()} ${url} | Request`);

  let accessToken = localStorage.getItem('access_token');
  if (!accessToken) {
    accessToken = authStore.accessToken;
  }

  if (accessToken) {
    config.headers.Authorization = 'Bearer ' + accessToken;
  }

  if (config.headers['Content-Type'] !== 'multipart/form-data') {
    if (config.params) {
      config.params = decamelizeKeys(config.params);
    }

    if (config.data) {
      config.data = decamelizeKeys(config.data);
    }
  }

  return config;
};

const onRequestError = (error: Error | AxiosError) => {
  onLoading('cancel');
  const commonStore = useCommonStore();
  const { _alert } = commonStore;
  _alert(error.message, AlertType.Error);
};

const onResponse = (response: AxiosResponse): AxiosResponse => {
  const { method, url } = response.config;
  const { status } = response;
  console.log(`🚀 [API] ${method?.toUpperCase()} ${url} | Response ${status}`);
  response.data = camelizeKeys(response.data);
  onLoading('end');
  return response.data;
};

const onResponseError = (error: AxiosError | Error): ResponseError => {
  const commonStore = useCommonStore();
  const { _alert } = commonStore;

  if (axios.isAxiosError(error)) {
    const { message } = error;
    const { method, url } = error.config as AxiosRequestConfig;
    const { status } = (error.response as AxiosResponse) ?? {};

    console.log(
      `🚨 [API] ${method?.toUpperCase()} ${url} | Error ${status} ${message}`
    );
    console.log(error);

    let errorData: ResponseError = {
      code: error.status || 500,
      message: error.message || 'Lỗi không xác định',
      status: error.code || 'UnknownError',
    };
    if (error.response?.data) {
      const responseErr: { [key: string]: any } = error.response.data as {
        [key: string]: any;
      };
      errorData = responseErr as ResponseError;

      if (!errorData.code) {
        errorData.code = isObjKey('status', responseErr)
          ? responseErr['status']
          : 500;
      }
      if (!errorData.message) {
        errorData.message = isObjKey('detail', responseErr)
          ? responseErr['detail']
          : 'Lỗi không xác định';
      }

      if (!errorData.status) {
        errorData.status = isObjKey('title', responseErr)
          ? responseErr['title']
          : 'UnknownError';
      }
    }

    if (status != 403) {
      if (errorData) _alert(errorData.message, AlertType.Error);
    }
    console.log(errorData);

    switch (status) {
      case 400: {
        // "Bad request"
        break;
      }
      case 401: {
        // "Permission denied"
        break;
      }
      case 403: {
        // "Login required"
        router.push('/login');
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
    onLoading('end');

    return errorData;
  } else {
    const errorData: ResponseError = {
      code: 500,
      message: 'Lỗi không xác định',
      status: 'UnknownError',
    };
    console.log(`🚨 [API] | Error ${error.message}`);
    errorData.message = error.message;
    console.log(`🚨 [API] | Error statck: ${error.stack}`);
    onLoading('end');

    return errorData;
  }
};

export const setupInterceptors = (
  axiosInstance: AxiosInstance
): AxiosInstance => {
  axiosInstance.interceptors.request.use(onRequest, onRequestError);
  axiosInstance.interceptors.response.use(onResponse, onResponseError);
  return axiosInstance;
};
