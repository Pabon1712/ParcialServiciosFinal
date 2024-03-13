package co.com.pabon.microservice.resolveEnigmaApi.api;

import co.com.pabon.microservice.resolveEnigmaApi.model.GetEnigmaRequest;
import co.com.pabon.microservice.resolveEnigmaApi.model.GetEnigmaStepResponse;
import co.com.pabon.microservice.resolveEnigmaApi.model.Header;
import co.com.pabon.microservice.resolveEnigmaApi.model.JsonApiBodyRequest;
import co.com.pabon.microservice.resolveEnigmaApi.model.JsonApiBodyResponseSuccess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GetStepApiController {

    @PostMapping("/getStep")
    public ResponseEntity<List<JsonApiBodyResponseSuccess>> getStep(@Valid @RequestBody JsonApiBodyRequest body) {
        List<GetEnigmaRequest> enigmas = body.getData();
        List<JsonApiBodyResponseSuccess> responseList = new ArrayList<>();

        for (GetEnigmaRequest enigma : enigmas) {
            Header header = enigma.getHeader();
            String id = header.getId();
            String type = header.getType();
            String enigmaQuestion = enigma.getEnigma();

            String solution = solveEnigma(enigmaQuestion);

            GetEnigmaStepResponse enigmaStepResponse = new GetEnigmaStepResponse();
            enigmaStepResponse.setId(id);
            enigmaStepResponse.setType(type);
            enigmaStepResponse.setSolution(solution);

            JsonApiBodyResponseSuccess responseBody = new JsonApiBodyResponseSuccess();
            responseBody.addDataItem(enigmaStepResponse);
            responseList.add(responseBody);
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping("/getEnigmaFromAllApis")
    public ResponseEntity<List<String>> getEnigmaFromAllApis() {
        List<String> enigmaResponses = new ArrayList<>();

        // Llamada a la primera API
        String responseFromApi1 = makeGetRequest("http://localhost:8080/v1/getOneEnigma/getStep");
        enigmaResponses.add(responseFromApi1);

        // Llamada a la segunda API
        String responseFromApi2 = makeGetRequest("http://localhost:8081/v1/getOneEnigma/getStep");
        enigmaResponses.add(responseFromApi2);

        // Llamada a la tercera API
        String responseFromApi3 = makeGetRequest("http://localhost:8082/v1/getOneEnigma/getStep");
        enigmaResponses.add(responseFromApi3);

        return new ResponseEntity<>(enigmaResponses, HttpStatus.OK);
    }

    private String solveEnigma(String enigmaQuestion) {
        if (enigmaQuestion.equals("4")) {
            return "full";
        } else {
            return "Respuesta no valida ";
        }
    }

    private String makeGetRequest(String apiUrl) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(apiUrl, String.class);
    }
}
