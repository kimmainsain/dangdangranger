package com.shield.dangdangranger.domain.patrol.repo;

import com.shield.dangdangranger.domain.patrol.entity.PatrolReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatrolReportRepository extends JpaRepository<PatrolReport, Integer> {

    public List<PatrolReport> findAllByCanceledOrderByCreateDateDesc(Integer isCanceled);

    public Optional<PatrolReport> findPatrolReportByPatrolReportNoAndCanceled(Integer patrolReportNo, Integer isCanceled);

    public List<PatrolReport> findAllByUserNoAndCanceledOrOrderByCreateDateDesc(Integer userNo, Integer isCanceled);



}
