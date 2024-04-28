package co.uk.pbnj.dashin.repository.entity;

import co.uk.pbnj.dashin.dto.CurrencyLatest;

import java.util.Map;

public record CurrencyLatestResponse(Map<String, Double> data){

    public CurrencyLatest toCurrencyLatest(){
        return new CurrencyLatest(data);
    }
}
