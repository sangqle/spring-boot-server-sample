import dayjs from 'dayjs';

export interface IUserSubscription {
  id?: number;
  userId?: number | null;
  startDate?: string | null;
  endDate?: string | null;
}

export const defaultValue: Readonly<IUserSubscription> = {};
