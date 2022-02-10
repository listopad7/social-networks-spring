package hu.listopad.socialnetworks.spring.web.mapstruct.mappers;

import hu.listopad.socialnetworks.spring.web.mapstruct.dtos.CommunityDetectionResultDTO;
import hu.listopad.socialnetworks.spring.dynamo.CommunityDetectionResult;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface MapStructMapper {

    CommunityDetectionResultDTO communityDetectionResultToDto(CommunityDetectionResult communityDetectionResult);

    CommunityDetectionResult dtoToCommunityDetectionResult(CommunityDetectionResultDTO communityDetectionResultDTO);

    List<CommunityDetectionResultDTO> communityDetectionResultListToDtoList(List<CommunityDetectionResult> communityDetectionResults);
}
