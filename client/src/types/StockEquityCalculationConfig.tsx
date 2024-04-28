export interface StockEquityCalculationConfig {
    ticker: String
    valueOpenDay: number
    valueCloseDay: number
    exchangeRate: number
    originalCurrency: String
    targetCurrency: String
    daysTillVest: number
}