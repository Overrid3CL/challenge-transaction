package alvarado.ms.transaction.businesses.service;

import alvarado.ms.transaction.businesses.controller.dto.BusinessListItemDTO;

import java.util.List;

public interface BusinessService {

    List<BusinessListItemDTO> findAll();
}
