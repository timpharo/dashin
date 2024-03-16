package co.uk.pbnj.dashin.dto;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.Map;

@RecordBuilder
public record CurrencyLatest(Map<String, Double> currencyExchanges) {

}
