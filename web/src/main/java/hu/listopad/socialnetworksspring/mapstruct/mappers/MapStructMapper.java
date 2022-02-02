package hu.listopad.socialnetworksspring.mapstruct.mappers;

import hu.listopad.socialnetworksspring.mapstruct.dtos.CommunityDetectionResultDTO;
import hu.listopad.socialnetworksspring.persistence.CommunityDetectionResult;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper(componentModel = "spring")
public interface MapStructMapper {

    CommunityDetectionResultDTO communityDetectionResultToDto(CommunityDetectionResult communityDetectionResult);

    CommunityDetectionResult dtoToCommunityDetectionResult(CommunityDetectionResultDTO communityDetectionResultDTO);

    List<CommunityDetectionResultDTO> communityDetectionResultListToDtoList(List<CommunityDetectionResult> communityDetectionResults);
}
