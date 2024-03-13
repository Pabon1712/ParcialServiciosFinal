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
    
    @GetMapping("/getStep")
    public ResponseEntity<String> getStepSimple() {
        return new ResponseEntity<>("meter la jirafa a el refrigerador", HttpStatus.OK);
    }

    private String solveEnigma(String enigmaQuestion) {
    	if (enigmaQuestion.equals("2"))
    		{return "Step2: Meter la girafa a el refrigerador";
    		}else {
    			return "Solucion implementada para enigma: " + enigmaQuestion;
    		}
    }
}


