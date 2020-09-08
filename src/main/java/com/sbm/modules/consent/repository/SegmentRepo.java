package com.sbm.modules.consent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbm.modules.consent.model.Segment;

@Repository
public interface SegmentRepo extends JpaRepository<Segment, Long >{

}
