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
    baseURL: 'https://localhost:8081',
    timeout: 15000,
  });

  return setupInterceptors(instance);
};
