import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-subscription.reducer';

export const UserSubscriptionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userSubscriptionEntity = useAppSelector(state => state.userSubscription.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userSubscriptionDetailsHeading">User Subscription</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{userSubscriptionEntity.id}</dd>
          <dt>
            <span id="userId">User Id</span>
          </dt>
          <dd>{userSubscriptionEntity.userId}</dd>
          <dt>
            <span id="startDate">Start Date</span>
          </dt>
          <dd>
            {userSubscriptionEntity.startDate ? (
              <TextFormat value={userSubscriptionEntity.startDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">End Date</span>
          </dt>
          <dd>
            {userSubscriptionEntity.endDate ? (
              <TextFormat value={userSubscriptionEntity.endDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="pricingId">Pricing Id</span>
          </dt>
          <dd>{userSubscriptionEntity.pricingId}</dd>
        </dl>
        <Button tag={Link} to="/user-subscription" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-subscription/${userSubscriptionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserSubscriptionDetail;
