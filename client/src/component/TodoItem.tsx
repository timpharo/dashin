import type { Component } from 'solid-js';
import { TodoItemConfig } from "../types/TodoItemConfig";
import {For} from "solid-js";

const TodoItem: Component = (name: String, todo: TodoItemConfig[]) => {
  return (
      <div className="m-2">
        <div className="card w-1/4 bg-neutral text-neutral-content card-compact">
            <div className="card-body">
                <h2 className="card-title">
                    📋 { name.charAt(0).toUpperCase() + name.slice(1).toLowerCase() } todo list
                </h2>
                <Switch>
                    <Match when={todo.length == 0}>
                        <span>All done 👍🏽</span>
                    </Match>
                    <Match when={todo.length > 0}>
                        <For each={todo}>{(item) =>
                            <div className="form-control">
                                <label className="cursor-pointer label">
                                    <span className="label-text">
                                        { item.content }
                                        { item.description }
                                    </span>
                                    <input type="checkbox" className="checkbox" disabled />
                                </label>
                            </div>
                        }</For>
                    </Match>
                </Switch>
            </div>
        </div>
    </div>
  );
};

export default TodoItem;
