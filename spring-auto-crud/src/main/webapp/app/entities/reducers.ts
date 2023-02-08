import pricing from 'app/entities/pricing/pricing.reducer';
import userSubscription from 'app/entities/user-subscription/user-subscription.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  userSubscription,
  pricing,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
