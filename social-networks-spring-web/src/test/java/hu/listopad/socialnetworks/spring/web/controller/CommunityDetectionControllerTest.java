package hu.listopad.socialnetworks.spring.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.listopad.socialnetworks.spring.data.communitydetection.CommunityDetectionResult;
import hu.listopad.socialnetworks.spring.data.communitydetection.SlimCommunityDetectionResult;
import hu.listopad.socialnetworks.spring.data.communitydetection.Status;
import hu.listopad.socialnetworks.spring.web.responseData.CommunityDetectionToResponseMapper;
import hu.listopad.socialnetworks.spring.web.service.CommunityDetectionServiceWebDynamo;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class CommunityDetectionControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommunityDetectionServiceWebDynamo communityDetectionServiceWebDynamo;

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private CommunityDetectionToResponseMapper communityDetectionToResponseMapper;

    @Test
    public void testPostMappingWhenValidRequest() throws Exception {

        String json = "{\"nodes\":[{\"id\":\"1\"}],\"edges\":[{\"from\":\"1\",\"to\":\"1\"},{\"from\":\"1\",\"to\":\"1\"}]}";


        when(communityDetectionServiceWebDynamo
                    .newCalculation(any(String.class), any(String.class), any(String.class)))
                .thenReturn("Accepted");

        this.mockMvc.perform(
                post("/sajnos/hatodik")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAccepted());

        verify(communityDetectionServiceWebDynamo, times(1))
                .newCalculation(any(String.class), any(String.class), any(String.class));
    }

    @Test
    public void testPostMappingWhenInvalidRequest() throws Exception{

        when(communityDetectionServiceWebDynamo
                .newCalculation(any(String.class), any(String.class), any(String.class)))
                .thenThrow(JsonProcessingException.class);

        this.mockMvc.perform(
                        post("/sajnos/hatodik")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("akarmi"))
                .andExpect(status().isBadRequest());

        verify(communityDetectionServiceWebDynamo, times(1))
                .newCalculation(any(String.class), any(String.class), any(String.class));

    }

    @Test
    public void testGetMappingOfIndividualItem() throws Exception {


        when(communityDetectionServiceWebDynamo.getOneResponse(any(String.class), any(String.class)))
                .thenAnswer((Answer<CommunityDetectionResult>) invocation -> {
                    CommunityDetectionResult communityDetectionResult = new CommunityDetectionResult();
                    communityDetectionResult.setUserId("sajnos");
                    communityDetectionResult.setGraphName("negyedik");
                    communityDetectionResult.setStatus(Status.DONE);
                    communityDetectionResult.setCommunityList(new ArrayList<>());
                    communityDetectionResult.setOriginalGraph(new HashMap<>());
                    communityDetectionResult.setModularityList(new ArrayList<>());
                    System.out.println(communityDetectionResult.getUserId());
                    return communityDetectionResult;
                });

        this.mockMvc.perform(get("/sajnos/negyedik"))
               .andExpect(status().isOk()).andExpect(jsonPath("$.userId", is("sajnos")));

        verify(communityDetectionServiceWebDynamo, times(1))
                .getOneResponse(any(String.class), any(String.class));
        verify(communityDetectionToResponseMapper, times(1))
                .mapToResponse(any(CommunityDetectionResult.class));
    }

    @Test
    public void testIndividualItemNotFind() throws Exception {
        when(communityDetectionServiceWebDynamo.getOneResponse(any(String.class), any(String.class)))
                .thenThrow(NoSuchElementException.class);

        this.mockMvc.perform(get("/sajnos/negyedik"))
                .andExpect(status().isNotFound());

        verify(communityDetectionServiceWebDynamo, times(1))
                .getOneResponse(any(String.class), any(String.class));
        verify(communityDetectionToResponseMapper, never())
                .mapToResponse(any(CommunityDetectionResult.class));
    }

    @Test
    public void testDeleteGraph() throws Exception {

                doAnswer(new Answer<Void>(){
                    @Override
                    public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                        return null;
                    }

                }).when(communityDetectionServiceWebDynamo).deleteGraph(any(String.class), any(String.class));


        this.mockMvc.perform(delete("/sajnos/negyedik"))
                .andExpect(status().isNoContent());

        verify(communityDetectionServiceWebDynamo, times(1))
                .deleteGraph(any(String.class), any(String.class));


    }

    @Test
    public void testGetGraphs() throws Exception {

        List<SlimCommunityDetectionResult> resultList= new ArrayList<>();
        for (int i= 0; i < 5; i++){
            SlimCommunityDetectionResult slimcommunityDetectionResult = new SlimCommunityDetectionResult();
            slimcommunityDetectionResult.setStatus(Status.DONE);
            slimcommunityDetectionResult.setGraphName("graph"+i);
            resultList.add(slimcommunityDetectionResult);
        }
        when(communityDetectionServiceWebDynamo.getGraphs(any(String.class)))
                .thenReturn(resultList);

        this.mockMvc.perform(get("/sajnos/graphs"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].graphName", is("graph0")));;

        verify(communityDetectionServiceWebDynamo, times(1))
                .getGraphs(any(String.class));
    }
}
