import type { Component } from 'solid-js';

import styles from '../App.module.css';
import {createResource, Show} from "solid-js";
import {DisplayItem} from "../types/DisplayConfig";
import TodoItem from "../component/TodoItem";
import StockEquityCalculation from "./StockEquityCalculation";
import {TodoItemConfig} from "../types/TodoItemConfig";
import {StockEquityCalculationConfig} from "../types/StockEquityCalculationConfig";


const DisplayItemConfigFetcher: Component = (displayItem: DisplayItem) => {
  const fetchDisplayItemConfig = async() => (
      await fetch(`http://localhost:8080${displayItem.location}`)).json();

  const [displayItemConfig, { _, refetchDisplayItemConfig }] = createResource(fetchDisplayItemConfig);

  return (
    <div className={styles.App}>
        <Switch>
            <Match when={displayItemConfig() && displayItem.type === 'Todo'}>
                { TodoItem(displayItem.name, displayItemConfig() as TodoItemConfig[]) }
            </Match>

            <Match when={displayItemConfig() && displayItem.type === 'StockEquityCalculation'}>
                { StockEquityCalculation( displayItemConfig() as StockEquityCalculationConfig) }
            </Match>
        </Switch>
    </div>
  );
};

export default DisplayItemConfigFetcher;
