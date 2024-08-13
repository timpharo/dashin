export interface StockEquityCalculationConfig {
    ticker: String
    valueOpenDay: number
    valueCloseDay: number
    exchangeRate: number
    originalCurrency: String
    targetCurrency: String
    daysTillVest: number
    history: Array<StockEquityHistoric>
}

export interface StockEquityHistoric {
    date: string,
    valueOpenDay: number,
    valueCloseDay: number,
    exchangeRate: number,
    targetValueOpenDay: number,
    targetValueCloseDay: number,
}