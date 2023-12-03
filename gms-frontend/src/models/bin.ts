export interface Weight {
  totalWeight: number;
  updatedTimestamp: string;
}

export interface Owner {
  id: string;
  name: string;
  phone: string;
  email: string;
}

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
}
