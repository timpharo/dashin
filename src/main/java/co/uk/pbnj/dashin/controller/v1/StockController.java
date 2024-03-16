package co.uk.pbnj.dashin.controller.v1;

import co.uk.pbnj.dashin.dto.StockEquityCalculation;
import co.uk.pbnj.dashin.service.StockService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class StockController {
    private static final String V_1_STOCK_EQUITY = "/v1/stock-equity";
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping(V_1_STOCK_EQUITY)
    public StockEquityCalculation getEquity() {
        return stockService.getStockEquityCalculation();
    }

    public static String getV1StockEquityPath() {
        return V_1_STOCK_EQUITY;
    }
}
