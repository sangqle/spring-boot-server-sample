import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserSubscription } from 'app/shared/model/user-subscription.model';
import { getEntity, updateEntity, createEntity, reset } from './user-subscription.reducer';

export const UserSubscriptionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userSubscriptionEntity = useAppSelector(state => state.userSubscription.entity);
  const loading = useAppSelector(state => state.userSubscription.loading);
  const updating = useAppSelector(state => state.userSubscription.updating);
  const updateSuccess = useAppSelector(state => state.userSubscription.updateSuccess);

  const handleClose = () => {
    navigate('/user-subscription' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);

    const entity = {
      ...userSubscriptionEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          startDate: displayDefaultDateTime(),
          endDate: displayDefaultDateTime(),
        }
      : {
          ...userSubscriptionEntity,
          startDate: convertDateTimeFromServer(userSubscriptionEntity.startDate),
          endDate: convertDateTimeFromServer(userSubscriptionEntity.endDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="springAutoCrudApp.userSubscription.home.createOrEditLabel" data-cy="UserSubscriptionCreateUpdateHeading">
            Create or edit a User Subscription
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="user-subscription-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="User Id" id="user-subscription-userId" name="userId" data-cy="userId" type="text" />
              <ValidatedField
                label="Start Date"
                id="user-subscription-startDate"
                name="startDate"
                data-cy="startDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="End Date"
                id="user-subscription-endDate"
                name="endDate"
                data-cy="endDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Pricing Id" id="user-subscription-pricingId" name="pricingId" data-cy="pricingId" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-subscription" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default UserSubscriptionUpdate;
