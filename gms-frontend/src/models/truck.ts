export interface Truck {
  plate: string;
  company: string;
  capacity: number;
  createdDate: string;
  updatedDate: string;
}

export const newTruck = (): Truck => {
  return {
    plate: '',
    company: '',
    capacity: 0,
    createdDate: '',
    updatedDate: '',
  };
};
