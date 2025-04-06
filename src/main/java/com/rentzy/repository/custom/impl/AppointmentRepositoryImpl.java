package com.rentzy.repository.custom.impl;


import com.rentzy.converter.AppointmentConverter;
import com.rentzy.model.dto.AppointmentSearchDTO;
import com.rentzy.model.entity.AppointmentEntity;
import com.rentzy.repository.custom.CustomAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class AppointmentRepositoryImpl implements CustomAppointmentRepository {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private AppointmentConverter appointmentConverter;

    @Override
    public List<AppointmentEntity> findByDateBetween(Date startDate, Date endDate) {
        Query query = new Query();
        Criteria criteria = new Criteria();

        // Xử lý trường hợp có hoặc không có startDate, endDate
        if (startDate != null && endDate != null) {
            if (startDate.after(endDate)) {
                throw new IllegalArgumentException("startDate phải trước endDate");
            }
            criteria = Criteria.where("date").gte(startDate).lte(endDate);
        } else if (startDate != null) {
            criteria = Criteria.where("date").gte(startDate);
        } else if (endDate != null) {
            criteria = Criteria.where("date").lte(endDate);
        }

        if (!criteria.getCriteriaObject().isEmpty()) {
            query.addCriteria(criteria);
        }

        return mongoTemplate.find(query, AppointmentEntity.class);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTimeSlotAvailable(String postId, Date startTime, Date endTime) {
        if (postId == null || startTime == null || endTime == null) {
            throw new IllegalArgumentException("postId, startTime và endTime không được null");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("postId").is(postId));
        query.addCriteria(Criteria.where("date").gte(startTime).lte(endTime));
        List<AppointmentEntity> overlappingAppointments = mongoTemplate.find(query, AppointmentEntity.class);
        return overlappingAppointments.isEmpty();
    }

    @Override
    public Page<AppointmentEntity> searchAppointments(AppointmentSearchDTO searchCriteria, Pageable pageable) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        // Lọc theo Post ID
        if (searchCriteria.getPostId() != null && !searchCriteria.getPostId().isBlank()) {
            criteriaList.add(Criteria.where("postId").is(searchCriteria.getPostId()));
        }

        // Lọc theo User ID (người đặt lịch)
        if (searchCriteria.getUserId() != null && !searchCriteria.getUserId().isBlank()) {
            criteriaList.add(Criteria.where("userId").is(searchCriteria.getUserId()));
        }

        // Lọc theo Owner ID (người đăng bài)
        if (searchCriteria.getOwnerId() != null && !searchCriteria.getOwnerId().isBlank()) {
            criteriaList.add(Criteria.where("ownerId").is(searchCriteria.getOwnerId()));
        }

        // Lọc theo trạng thái cuộc hẹn
        if (searchCriteria.getStatus() != null) {
            criteriaList.add(Criteria.where("status").is(searchCriteria.getStatus()));
        }

        // Lọc theo khoảng thời gian (startDate - endDate)
        if (searchCriteria.getStartDate() != null || searchCriteria.getEndDate() != null) {
            if (searchCriteria.getStartDate() != null && searchCriteria.getEndDate() != null &&
                    searchCriteria.getStartDate().after(searchCriteria.getEndDate())) {
                throw new IllegalArgumentException("Start date must be before End date");
            }

            Criteria dateCriteria = Criteria.where("startDate");
            if (searchCriteria.getStartDate() != null) {
                dateCriteria = dateCriteria.gte(searchCriteria.getStartDate());
            }
            if (searchCriteria.getEndDate() != null) {
                dateCriteria = dateCriteria.lte(searchCriteria.getEndDate());
            }
            criteriaList.add(dateCriteria);
        }

        // Nếu có điều kiện tìm kiếm, áp dụng vào query
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        // Nếu pageable bị null, dùng Pageable.unpaged()
        if (pageable == null) {
            pageable = Pageable.unpaged();
        }

        // Xử lý sắp xếp nếu có
        if (pageable.getSort().isSorted()) {
            query.with(pageable.getSort());
        }

        // Tính tổng số lượng bản ghi trước khi phân trang
        long total = mongoTemplate.count(query, AppointmentEntity.class);

        // Áp dụng phân trang
        query.with(pageable);

        // Truy vấn danh sách cuộc hẹn theo điều kiện lọc
        List<AppointmentEntity> appointments = mongoTemplate.find(query, AppointmentEntity.class);


        return new PageImpl<>(appointments, pageable, total);
    }
}
