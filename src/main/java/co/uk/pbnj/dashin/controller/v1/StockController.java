package co.uk.pbnj.dashin.controller.v1;

import co.uk.pbnj.dashin.dto.StockEquityCalculation;
import co.uk.pbnj.dashin.dto.StockEquityCalculationHistoric;
import co.uk.pbnj.dashin.dto.StockEquityCalculationWithHistory;
import co.uk.pbnj.dashin.service.StockService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@CrossOrigin
@RestController
public class StockController {
    private static final String V_1_STOCK_EQUITY = "/v1/stock-equity";
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    //TODO change frontend to display new 'historic' fields
    @GetMapping(V_1_STOCK_EQUITY)
    public StockEquityCalculationWithHistory getEquity() {
        StockEquityCalculation stockEquityCalculation = stockService.getStockEquityCalculation();
        List<StockEquityCalculationHistoric> historic = stockService.getStockEquityCalculationHistoric();

        return StockEquityCalculationWithHistory.from(stockEquityCalculation, historic);
    }

    public static String getV1StockEquityPath() {
        return V_1_STOCK_EQUITY;
    }
}
