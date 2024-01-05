export interface Weight {
  totalWeight: number;
  updatedTimestamp: string;
}

export const newWeight = (): Weight => {
  return {
    totalWeight: 0,
    updatedTimestamp: '',
  };
};

export interface Owner {
  id: string;
  name: string;
  phone: string;
  email: string;
}

export const newOwner = (): Owner => {
  return {
    id: '',
    name: '',
    phone: '',
    email: '',
  };
};

export interface Bin {
  id: string;
  company: string;
  address: string;
  lat: number;
  lon: number;
  capacity: number;
  createDate: string;
  updatedDate: string;
  createdUser: string;
  imageUrl: string;
  weight: Weight;
  owner: Owner;
  cameraUrl: string;
}

export interface CreateBinRes {
  id: string;
  company: string;
  address: string;
  lat: number;
  lon: number;
  capacity: number;
  createdUser: string;
  imageUrl: string;
  owner: Owner;
  cameraUrl: string;
}

export const newBin = (): Bin => {
  return {
    id: '',
    company: '',
    address: '',
    lat: 0,
    lon: 0,
    capacity: 0,
    createDate: '',
    updatedDate: '',
    createdUser: '',
    imageUrl: '',
    weight: newWeight(),
    owner: newOwner(),
    cameraUrl: '',
  };
};

export interface CreateBin {
  company: string;
  lat: number;
  lon: number;
  capacity: number;
  address: string;
  ownerName: string;
  ownerPhone: string;
  ownerEmail: string;
  cameraUrl: string;
}

export const newCreateBin = () => {
  return {
    company: '',
    lat: 0,
    lon: 0,
    capacity: 0,
    address: '',
    ownerName: '',
    ownerPhone: '',
    ownerEmail: '',
    cameraUrl: '',
  };
};