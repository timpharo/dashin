export interface StockEquityCalculationConfig {
    valueOpenDay: number
    valueCloseDay: number
    exchangeRate: number
    originalCurrency: String
    targetCurrency: String
    daysTillVest: number
}