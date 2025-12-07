package com.wings.WingsSecurity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wings.WingsSecurity.entity.Leave;
import com.wings.WingsSecurity.entity.User;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Integer> {
    List<Leave> findByUser(User user);

    List<Leave> findByUserEmail(String email);

    List<Leave> findByUserId(int id);

    List<Leave> findByNoOfDaysGreaterThan(int noOfDays);
}
