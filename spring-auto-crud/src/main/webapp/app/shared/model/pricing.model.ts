import dayjs from 'dayjs';

export interface IPricing {
  id?: number;
  title?: string | null;
  desc?: string | null;
  price?: number | null;
  updatedAt?: string | null;
  isDeleted?: boolean | null;
}

export const defaultValue: Readonly<IPricing> = {
  isDeleted: false,
};
