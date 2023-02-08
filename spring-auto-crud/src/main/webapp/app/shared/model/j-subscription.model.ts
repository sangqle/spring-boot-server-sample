import dayjs from 'dayjs';

export interface IJSubscription {
  id?: number;
  userId?: number | null;
  startDate?: string | null;
  endDate?: string | null;
}

export const defaultValue: Readonly<IJSubscription> = {};
