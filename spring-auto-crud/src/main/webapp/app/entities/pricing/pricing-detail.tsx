import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pricing.reducer';

export const PricingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pricingEntity = useAppSelector(state => state.pricing.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pricingDetailsHeading">Pricing</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{pricingEntity.id}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{pricingEntity.title}</dd>
          <dt>
            <span id="desc">Desc</span>
          </dt>
          <dd>{pricingEntity.desc}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{pricingEntity.price}</dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>{pricingEntity.updatedAt ? <TextFormat value={pricingEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="isDeleted">Is Deleted</span>
          </dt>
          <dd>{pricingEntity.isDeleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/pricing" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pricing/${pricingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PricingDetail;
