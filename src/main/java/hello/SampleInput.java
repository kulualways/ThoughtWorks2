/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 *
 * @author Kuldeep S Chauhan | Date:18 Feb, 2018
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SampleInput {
    
    public List<Input> inputList;

    public List<Input> getInputList() {
        return inputList;
    }

    public void setInputList(List<Input> inputList) {
        this.inputList = inputList;
    }
    
    

}
