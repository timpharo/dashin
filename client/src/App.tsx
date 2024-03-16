import type { Component } from 'solid-js';

import styles from './App.module.css';
import {createResource, For} from "solid-js";
import {DisplayConfig} from "./types/DisplayConfig";
import DisplayItemConfigFetcher from "./component/DisplayItemFetcher";

const fetchDisplayConfig = async() =>
    (await fetch('http://localhost:8080/v1/display-config')).json();


const App: Component = () => {
  const [displayConfig, { _, refetchDisplayConfig }] = createResource(fetchDisplayConfig) as DisplayConfig;

  return (
    <div class={styles.App}>
      <span>{displayConfig.loading && "Loading..."}</span>

      <Show
          when={displayConfig()}
          fallback={<p>Loading...</p>}>

          <For each={displayConfig().displayItems}>{(item) =>
              <div>
                <p>{DisplayItemConfigFetcher(item)}</p>
              </div>
          }</For>
      </Show>
    </div>
  );
};

export default App;
