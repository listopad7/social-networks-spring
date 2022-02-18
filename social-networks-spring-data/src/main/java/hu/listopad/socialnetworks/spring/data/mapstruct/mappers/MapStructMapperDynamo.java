package hu.listopad.socialnetworks.spring.data.mapstruct.mappers;

import hu.listopad.socialnetworks.spring.data.communitydetection.SlimCommunityDetectionResult;
import hu.listopad.socialnetworks.spring.data.dynamo.CommunityDetectionResultDynamo;
import hu.listopad.socialnetworks.spring.data.communitydetection.CommunityDetectionResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapStructMapperDynamo {

    CommunityDetectionResultDynamo communityDetectionResultToDynamo(CommunityDetectionResult communityDetectionResult);

    CommunityDetectionResult dynamoToCommunityDetectionResult(CommunityDetectionResultDynamo communityDetectionResultDynamo);

    Iterable<SlimCommunityDetectionResult> communityDetectionResultDynamoListToSlimResultList(Iterable<CommunityDetectionResultDynamo> DynamoCommunityDetectionResults);

    SlimCommunityDetectionResult communityDetectionResultDynamoToSlimResult(CommunityDetectionResultDynamo communityDetectionResultDynamo);
}

