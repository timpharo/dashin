package co.uk.pbnj.dashin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class CurrencyLatest {
    private Map<String, Double> currencyExchanges;
}
