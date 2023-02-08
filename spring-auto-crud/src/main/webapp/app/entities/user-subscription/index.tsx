import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserSubscription from './user-subscription';
import UserSubscriptionDetail from './user-subscription-detail';
import UserSubscriptionUpdate from './user-subscription-update';
import UserSubscriptionDeleteDialog from './user-subscription-delete-dialog';

const UserSubscriptionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserSubscription />} />
    <Route path="new" element={<UserSubscriptionUpdate />} />
    <Route path=":id">
      <Route index element={<UserSubscriptionDetail />} />
      <Route path="edit" element={<UserSubscriptionUpdate />} />
      <Route path="delete" element={<UserSubscriptionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserSubscriptionRoutes;
