package alvarado.ms.transaction.businesses.service.impl;

import alvarado.ms.transaction.businesses.controller.dto.BusinessListItemDTO;
import alvarado.ms.transaction.businesses.controller.mapper.BusinessMapper;
import alvarado.ms.transaction.businesses.repository.BusinessRepository;
import alvarado.ms.transaction.businesses.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<BusinessListItemDTO> findAll() {
        return businessRepository.findAllByDeletedFalse().stream()
                .map(mapper::toListItemDTO)
                .collect(Collectors.toList());
    }
}
