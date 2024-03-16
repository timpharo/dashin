import type { Component } from 'solid-js';
import {StockEquityCalculationConfig} from "../types/StockEquityCalculationConfig";

const StockEquityCalculation: Component = (stock: StockEquityCalculationConfig) => {
  return (
    <div>
      <h2>Stock equity calculation</h2>
      <p>Days till vest { stock.daysTillVest }</p>
      <ul>
          <li>open: { stock.valueOpenDay } { stock.originalCurrency }</li>
          <li>close: { stock.valueCloseDay } { stock.originalCurrency }</li>
      </ul>
      <ul>
          <li>open: { stock.valueOpenDay * stock.exchangeRate } { stock.targetCurrency }</li>
          <li>close: { stock.valueCloseDay * stock.exchangeRate } { stock.targetCurrency }</li>
      </ul>
    </div>
  );
};

export default StockEquityCalculation;
