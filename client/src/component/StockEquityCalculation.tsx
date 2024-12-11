import {Component, For, Match, Switch} from 'solid-js';
import {StockEquityCalculationConfig} from "../types/StockEquityCalculationConfig";

const StockEquityCalculation: Component = (stock: StockEquityCalculationConfig) => {
    let targetValueCloseDayYesterday = stock.targetValueCloseDay;
    let targetValueOpenDayYesterday = stock.targetValueOpenDay;
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
                            {formatNumber(targetValueCloseDayYesterday, stock.targetCurrency)}
                        </div>
                        <div className="stat-desc text-success-content">
                            {formatNumber(stock.valueCloseDay, stock.originalCurrency)}
                        </div>
                        <div className="stat-desc text-success-content">
                            (After tax: {formatNumber((targetValueCloseDayYesterday * 0.58), stock.targetCurrency)})
                        </div>
                    </div>

                    <div className="stat">
                        <div className="stat-title">Yesterdays open</div>
                        <div className="stat-value">
                            {formatNumber(targetValueOpenDayYesterday, stock.targetCurrency)}
                        </div>
                        <div className="stat-desc">
                            {formatNumber(stock.valueOpenDay, stock.originalCurrency)}
                        </div>
                    </div>
                </div>

                <div className="overflow-x-auto">
                    <table className="table">
                        <tbody>
                        <tr>
                            <th>Ticker</th>
                            <td>{stock.ticker}</td>
                        </tr>
                        <tr>
                            <th>Days till vest</th>
                            <td>{stock.daysTillVest}</td>
                        </tr>
                        <tr>
                            <td colSpan={2}>
                                <progress className="progress progress-success w-56" value={365 - stock.daysTillVest}
                                          max="365"></progress>
                            </td>
                        </tr>
                        </tbody>
                    </table>

                    <div className="overflow-x-auto">
                        <table className="table table-xs">
                            <thead>
                            <tr>
                                <th>Date</th>
                                <th>Open</th>
                                <th>Close</th>
                            </tr>
                            </thead>
                            <tbody>
                            <Switch>
                                <Match when={stock.history.length == 0}>
                                    <tr>
                                        <td colSpan="2">No historic data</td>
                                    </tr>
                                </Match>
                                <Match when={stock.history.length > 0}>
                                    <For each={stock.history}>{(historicItem) =>
                                        <tr>
                                            <th>{historicItem.date}</th>
                                            <td>{formatNumber(historicItem.targetValueOpenDay, stock.targetCurrency)}</td>
                                            <td>{formatNumber(historicItem.targetValueCloseDay, stock.targetCurrency)}</td>
                                        </tr>
                                    }</For>
                                </Match>
                            </Switch>
                            </tbody>
                        </table>
                        {/*TODO this is brittle as hardcodes the stock market*/}
                        <div className="overflow-x-auto">
                            <a
                                href={"https://www.google.com/finance/quote/" + stock.ticker + ":NASDAQ?window=5Y"}
                                target="_blank">
                                View stock history
                            </a>
                        </div>
                    </div>


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
