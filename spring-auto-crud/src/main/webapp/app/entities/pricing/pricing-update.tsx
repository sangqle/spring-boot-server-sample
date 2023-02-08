import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPricing } from 'app/shared/model/pricing.model';
import { getEntity, updateEntity, createEntity, reset } from './pricing.reducer';

export const PricingUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pricingEntity = useAppSelector(state => state.pricing.entity);
  const loading = useAppSelector(state => state.pricing.loading);
  const updating = useAppSelector(state => state.pricing.updating);
  const updateSuccess = useAppSelector(state => state.pricing.updateSuccess);

  const handleClose = () => {
    navigate('/pricing' + location.search);
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
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...pricingEntity,
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
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...pricingEntity,
          updatedAt: convertDateTimeFromServer(pricingEntity.updatedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="springAutoCrudApp.pricing.home.createOrEditLabel" data-cy="PricingCreateUpdateHeading">
            Create or edit a Pricing
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="pricing-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Title" id="pricing-title" name="title" data-cy="title" type="text" />
              <ValidatedField label="Desc" id="pricing-desc" name="desc" data-cy="desc" type="text" />
              <ValidatedField label="Price" id="pricing-price" name="price" data-cy="price" type="text" />
              <ValidatedField
                label="Updated At"
                id="pricing-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Is Deleted" id="pricing-isDeleted" name="isDeleted" data-cy="isDeleted" check type="checkbox" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pricing" replace color="info">
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

export default PricingUpdate;
