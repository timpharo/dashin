import type { Component } from 'solid-js';

import {createResource} from "solid-js";
import {DisplayItem} from "../types/DisplayConfig";
import TodoItem from "../component/TodoItem";
import StockEquityCalculation from "./StockEquityCalculation";
import {TodoItemConfig} from "../types/TodoItemConfig";
import {StockEquityCalculationConfig} from "../types/StockEquityCalculationConfig";
import Loading from "./Loading";
import CountdownItem from "./CountdownItem";
import {Countdown} from "../types/Countdown";


const DisplayItemConfigFetcher: Component = (displayItem: DisplayItem) => {
  const fetchDisplayItemConfig = async() => (
      await fetch(`http://localhost:8080${displayItem.location}`)).json();

  const [displayItemConfig, { _, refetchDisplayItemConfig }] = createResource(fetchDisplayItemConfig);
    const type = displayItem.type;

    return (
      <div>
          <Show
              when={displayItemConfig()}
              fallback={<Loading />}>
                <Switch>
                    <Match when={type === 'Todo'}>
                        { TodoItem(displayItem.name, displayItemConfig() as TodoItemConfig[]) }
                    </Match>
                    <Match when={type === 'StockEquityCalculation'}>
                        { StockEquityCalculation( displayItemConfig() as StockEquityCalculationConfig) }
                    </Match>
                    <Match when={type === 'Countdown'}>
                        { CountdownItem( displayItemConfig() as Countdown) }
                    </Match>

                </Switch>
          </Show>
    </div>
  );
};

export default DisplayItemConfigFetcher;
