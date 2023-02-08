import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Pricing from './pricing';
import PricingDetail from './pricing-detail';
import PricingUpdate from './pricing-update';
import PricingDeleteDialog from './pricing-delete-dialog';

const PricingRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Pricing />} />
    <Route path="new" element={<PricingUpdate />} />
    <Route path=":id">
      <Route index element={<PricingDetail />} />
      <Route path="edit" element={<PricingUpdate />} />
      <Route path="delete" element={<PricingDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PricingRoutes;
