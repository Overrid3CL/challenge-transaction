package alvarado.ms.transaction.businesses.controller.mapper;

import alvarado.ms.transaction.businesses.controller.dto.BusinessListItemDTO;
import alvarado.ms.transaction.businesses.domain.entity.Business;
import org.springframework.stereotype.Component;

@Component
public class BusinessMapper {

    public BusinessListItemDTO toListItemDTO(Business entity) {
        return BusinessListItemDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
