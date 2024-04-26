import type { Component } from 'solid-js';
import {StockEquityCalculationConfig} from "../types/StockEquityCalculationConfig";

const StockEquityCalculation: Component = (stock: StockEquityCalculationConfig) => {
  return (
    <div>
      <h2>Stock equity calculation</h2>
      <p>Days till vest { stock.daysTillVest }</p>
      <p>Vesting share value yesterday:</p>
      <ul>
          <li>
              opened @ { formatNumber(stock.valueOpenDay * stock.exchangeRate, stock.targetCurrency) }
              ({ formatNumber(stock.valueOpenDay, stock.originalCurrency) })
          </li>
          <li>
              closed @ { formatNumber(stock.valueCloseDay * stock.exchangeRate, stock.targetCurrency) }
              ({ formatNumber(stock.valueCloseDay, stock.originalCurrency) })
          </li>
      </ul>
    </div>
  );
};

function formatNumber(value: number, currency: string) {
    return value.toLocaleString('en-GB', {
        style: 'currency',
        currency: currency
    });
}

export default StockEquityCalculation;
