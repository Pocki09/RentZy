package com.repository.custom.impl;


import com.model.entity.AppointmentEntity;
import com.repository.custom.CustomAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;

@Repository
public class AppointmentRepositoryImpl implements CustomAppointmentRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<AppointmentEntity> findByDateBetween(Date startDate, Date endDate) {
        Query query = new Query();
        Criteria criteria = new Criteria();

        // Xử lý trường hợp có hoặc không có startDate, endDate
        if (startDate != null && endDate != null) {
            if (startDate.after(endDate)) {
                throw new IllegalArgumentException("startDate phải trước endDate");
            }
            criteria = Criteria.where("appointmentDate").gte(startDate).lte(endDate);
        } else if (startDate != null) {
            criteria = Criteria.where("appointmentDate").gte(startDate);
        } else if (endDate != null) {
            criteria = Criteria.where("appointmentDate").lte(endDate);
        }

        if (!criteria.getCriteriaObject().isEmpty()) {
            query.addCriteria(criteria);
        }

        return mongoTemplate.find(query, AppointmentEntity.class);
    }
}
