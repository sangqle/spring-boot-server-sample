import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import JSubscription from './j-subscription';
import JSubscriptionDetail from './j-subscription-detail';
import JSubscriptionUpdate from './j-subscription-update';
import JSubscriptionDeleteDialog from './j-subscription-delete-dialog';

const JSubscriptionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<JSubscription />} />
    <Route path="new" element={<JSubscriptionUpdate />} />
    <Route path=":id">
      <Route index element={<JSubscriptionDetail />} />
      <Route path="edit" element={<JSubscriptionUpdate />} />
      <Route path="delete" element={<JSubscriptionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default JSubscriptionRoutes;
