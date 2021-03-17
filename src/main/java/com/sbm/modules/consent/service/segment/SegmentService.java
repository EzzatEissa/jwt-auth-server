package com.sbm.modules.consent.service.segment;

import com.sbm.modules.consent.dto.SegmentDto;
import com.sbm.modules.consent.model.Segment;

import java.util.List;
import java.util.Optional;

public interface SegmentService {

    public List<SegmentDto> getAllSegments();

    public Optional<Segment> getSegmentById(Long id);
}
