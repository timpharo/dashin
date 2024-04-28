import type { Component } from 'solid-js';
import {StockEquityCalculationConfig} from "../types/StockEquityCalculationConfig";

const StockEquityCalculation: Component = (stock: StockEquityCalculationConfig) => {
  return (
    <div className="m-2 float-start">
        <div className="card bg-neutral text-neutral-content card-compact">

            <div className="card-body">
                <h2 className="card-title">💰 Stock equity calculation</h2>

                <p className="text-xs">Equity value if vesting today</p>

                <div className="stats stats-vertical shadow">
                    <div className="stat bg-success text-success-content">
                        <div className="stat-title text-success-content">Yesterdays close</div>
                        <div className="stat-value">
                            { formatNumber(stock.valueCloseDay * stock.exchangeRate, stock.targetCurrency) }
                        </div>
                        <div className="stat-desc text-success-content">
                            { formatNumber(stock.valueCloseDay, stock.originalCurrency)}
                        </div>
                    </div>

                    <div className="stat">
                        <div className="stat-title">Yesterdays open</div>
                        <div className="stat-value">
                            { formatNumber(stock.valueOpenDay * stock.exchangeRate, stock.targetCurrency) }
                        </div>
                        <div className="stat-desc">
                            { formatNumber(stock.valueOpenDay, stock.originalCurrency)}
                        </div>
                    </div>
                </div>

                <div className="overflow-x-auto">
                    <table className="table">
                        <tbody>
                        <tr>
                            <th>Ticker</th>
                            <td>{ stock.ticker }</td>
                        </tr>
                        <tr>
                            <th>Days till vest</th>
                            <td>{ stock.daysTillVest }</td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
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
