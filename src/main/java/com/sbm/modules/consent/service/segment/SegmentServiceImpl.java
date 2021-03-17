package com.sbm.modules.consent.service.segment;

import com.sbm.common.utils.MapperHelper;
import com.sbm.modules.consent.dto.SegmentDto;
import com.sbm.modules.consent.model.Segment;
import com.sbm.modules.consent.repository.SegmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SegmentServiceImpl implements SegmentService{

    @Autowired
    private SegmentRepo segmentRepo;

    @Autowired
    private MapperHelper mapperHelper;

    @Override
    public List<SegmentDto> getAllSegments() {

        List<Segment> segments = segmentRepo.findAll();
        return mapperHelper.transform(segments, SegmentDto.class);
    }

    @Override
    public Optional<Segment> getSegmentById(Long id) {
        return segmentRepo.findById(id);
    }
}
