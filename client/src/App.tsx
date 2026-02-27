import type { Component } from 'solid-js';

import {createResource, For} from "solid-js";
import {DisplayConfig} from "./types/DisplayConfig";
import DisplayItemConfigFetcher from "./component/DisplayItemFetcher";
import Loading from "./component/Loading";
import TVSportGuide from "./component/TVSportGuide";

const fetchDisplayConfig = async() =>
    (await fetch('http://localhost:8080/v1/display-config')).json();


const App: Component = () => {
  const [displayConfig, { _, refetchDisplayConfig }] = createResource(fetchDisplayConfig) as DisplayConfig;

    return (
      <div>
          <div className="navbar bg-base-300">
              <div className="navbar-start"></div>
              <div className="navbar-center">
                  <p className="font-bold text-2xl">Da⚡hin</p>
              </div>
              <div className="navbar-end"></div>
          </div>
          <div className="container mx-auto">
              <Show
                  when={displayConfig()}
                  fallback={<Loading />}>

                  <For each={displayConfig().displayItems}>
                      {(item) =>
                          <div>
                            { DisplayItemConfigFetcher(item) }
                          </div>
                      }
                  </For>
                  <TVSportGuide />
              </Show>
          </div>
      </div>
  );
};

export default App;
