import axios, { AxiosInstance } from 'axios';
import { setupInterceptors } from './interceptors';

export const createInstance = (): AxiosInstance => {
  const instance = axios.create({
    baseURL: 'https://ftvdexcz.io.vn:8081',
    timeout: 15000,
  });

  return setupInterceptors(instance);
};
