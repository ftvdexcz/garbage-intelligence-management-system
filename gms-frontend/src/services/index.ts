import axios, { AxiosInstance } from 'axios';
import { setupInterceptors } from './interceptors';

export interface BaseResponse<T> {
  code: number;
  status: string;
  message: string;
  data: T;
}

export const createInstance = (): AxiosInstance => {
  const instance = axios.create({
    baseURL: process.env.VUE_APP_BASE_URL,
    timeout: 15000,
  });

  return setupInterceptors(instance);
};
