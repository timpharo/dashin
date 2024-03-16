package co.uk.pbnj.dashin.repository.entity;

import co.uk.pbnj.dashin.dto.CurrencyLatest;
import co.uk.pbnj.dashin.dto.CurrencyLatestBuilder;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.Map;

@RecordBuilder
public record CurrencyLatestResponse(Map<String, Double> data){

    public CurrencyLatest toCurrencyLatest(){
        return CurrencyLatestBuilder.builder()
                .currencyExchanges(data)
                .build();
    }
}
