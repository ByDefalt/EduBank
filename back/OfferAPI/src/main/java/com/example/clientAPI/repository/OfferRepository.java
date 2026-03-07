package com.example.clientAPI.repository;

import com.example.clientAPI.entity.OfferEntity;
import com.example.clientAPI.entity.OfferInputEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OfferRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OfferRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_INSERT_OFFER =
            "INSERT INTO offer (picture_path, title, description, state, start_date, end_date) " +
                    "VALUES (:picturePath, :title, :description, :state, :startDate, :endDate)";

    private static final String SQL_GET_ALL_OFFERS =
            "SELECT id, picture_path, title, description, state, start_date, end_date " +
                    "FROM offer";

    private static final String SQL_GET_OFFER_BY_ID =
            "SELECT id, picture_path, title, description, state, start_date, end_date " +
                    "FROM offer WHERE id = :id";

    private static final String SQL_GET_ACTIVE_OFFERS =
            "SELECT id, picture_path, title, description, state, start_date, end_date " +
                    "FROM offer WHERE state = 'active'";

    private static final String SQL_UPDATE_OFFER =
            "UPDATE offer SET " +
                    "picture_path = :picturePath, " +
                    "title = :title, " +
                    "description = :description, " +
                    "state = :state, " +
                    "start_date = :startDate, " +
                    "end_date = :endDate " +
                    "WHERE id = :id";

    private static final String SQL_DELETE_OFFER =
            "DELETE FROM offer WHERE id = :id";

    public OfferEntity createOffer(OfferInputEntity offer) {
        Map<String, Object> params = new HashMap<>();
        params.put("picturePath", offer.getPicturePath());
        params.put("title", offer.getTitle());
        params.put("description", offer.getDescription());
        params.put("state", offer.getState());
        params.put("startDate", offer.getStartDate());
        params.put("endDate", offer.getEndDate());

        jdbcTemplate.update(SQL_INSERT_OFFER, params);

        return getOfferByInput(offer);
    }

    public List<OfferEntity> getAllOffers() {
        return jdbcTemplate.query(SQL_GET_ALL_OFFERS, new HashMap<>(), (rs, rowNum) -> {
            OfferEntity o = new OfferEntity();
            o.setId(rs.getInt("id"));
            o.setPicturePath(rs.getString("picture_path"));
            o.setTitle(rs.getString("title"));
            o.setDescription(rs.getString("description"));
            o.setState(rs.getString("state"));
            o.setStartDate(rs.getDate("start_date").toLocalDate());
            o.setEndDate(rs.getDate("end_date").toLocalDate());
            return o;
        });
    }

    public OfferEntity getOfferById(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        return jdbcTemplate.queryForObject(SQL_GET_OFFER_BY_ID, params, (rs, rowNum) -> {
            OfferEntity o = new OfferEntity();
            o.setId(rs.getInt("id"));
            o.setPicturePath(rs.getString("picture_path"));
            o.setTitle(rs.getString("title"));
            o.setDescription(rs.getString("description"));
            o.setState(rs.getString("state"));
            o.setStartDate(rs.getDate("start_date").toLocalDate());
            o.setEndDate(rs.getDate("end_date").toLocalDate());
            return o;
        });
    }

    public List<OfferEntity> getActiveOffers() {
        return jdbcTemplate.query(SQL_GET_ACTIVE_OFFERS, new HashMap<>(), (rs, rowNum) -> {
            OfferEntity o = new OfferEntity();
            o.setId(rs.getInt("id"));
            o.setPicturePath(rs.getString("picture_path"));
            o.setTitle(rs.getString("title"));
            o.setDescription(rs.getString("description"));
            o.setState(rs.getString("state"));
            o.setStartDate(rs.getDate("start_date").toLocalDate());
            o.setEndDate(rs.getDate("end_date").toLocalDate());
            return o;
        });
    }

    public OfferEntity updateOffer(int id, OfferInputEntity offer) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("picturePath", offer.getPicturePath());
        params.put("title", offer.getTitle());
        params.put("description", offer.getDescription());
        params.put("state", offer.getState());
        params.put("startDate", offer.getStartDate());
        params.put("endDate", offer.getEndDate());

        jdbcTemplate.update(SQL_UPDATE_OFFER, params);

        return getOfferById(id);
    }

    public void deleteOffer(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        jdbcTemplate.update(SQL_DELETE_OFFER, params);
    }

    private static final String SQL_GET_OFFER_BY_TITLE =
            "SELECT id, picture_path, title, description, state, start_date, end_date " +
                    "FROM offer WHERE title = :title";

    private OfferEntity getOfferByInput(OfferInputEntity offer) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", offer.getTitle());

        return jdbcTemplate.queryForObject(SQL_GET_OFFER_BY_TITLE, params, (rs, rowNum) -> {
            OfferEntity o = new OfferEntity();
            o.setId(rs.getInt("id"));
            o.setPicturePath(rs.getString("picture_path"));
            o.setTitle(rs.getString("title"));
            o.setDescription(rs.getString("description"));
            o.setState(rs.getString("state"));
            o.setStartDate(rs.getDate("start_date").toLocalDate());
            o.setEndDate(rs.getDate("end_date").toLocalDate());
            return o;
        });
    }
}